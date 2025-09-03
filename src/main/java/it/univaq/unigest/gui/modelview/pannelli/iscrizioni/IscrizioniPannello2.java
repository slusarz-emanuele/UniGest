package it.univaq.unigest.gui.modelview.pannelli.iscrizioni;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.*;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.StudenteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class IscrizioniPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID               = "ID Iscrizione";
    private static final String L_DATA_ISCRIZIONE  = "Data iscrizione";
    private static final String L_RITIRATO         = "Ritirato";
    private static final String L_STUDENTE         = "Studente (CF)";
    private static final String L_APPELLO          = "Appello";

    // Dipendenze
    private final IscrizioneService iscrizioneService;
    private final VistaConDettagliBuilder<Iscrizione> builder;

    // Loader esterni
    private final Supplier<List<Studente>> loadStudenti;
    private final Supplier<List<Appello>> loadAppelli;

    public IscrizioniPannello2(IscrizioneService iscrizioneService,
                               Supplier<List<Studente>> loadStudenti,
                               Supplier<List<Appello>> loadAppelli) {
        this.iscrizioneService = iscrizioneService;
        this.loadStudenti = loadStudenti;
        this.loadAppelli = loadAppelli;
        this.builder = new VistaConDettagliBuilder<>(iscrizioneService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Iscrizione, String>> colonne = colonne();
        LinkedHashMap<String, Function<Iscrizione, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Visualizza Esame", i -> "Visualizza Esame");
        builder.setLinkAction("Visualizza Esame", this::mostraEsameDaIscrizione);

        return builder.build(
                "Gestione Iscrizioni",
                colonne,
                dettagli,
                this::apriDialogAggiungi,
                this::mostraDialogModifica,
                this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'iscrizione."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'iscrizione."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(Main.getIscrizioneManager().getAll());
    }

    public VistaConDettagliBuilder<Iscrizione> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Iscrizione, String>> colonne() {
        LinkedHashMap<String, Function<Iscrizione, String>> map = new LinkedHashMap<>();
        map.put(L_ID, i -> i.getId() != null ? i.getId().toString() : "");
        map.put(L_DATA_ISCRIZIONE, i -> i.getDataIscrizione() != null ? i.getDataIscrizione().toString() : "");
        map.put(L_RITIRATO, i -> Boolean.TRUE.equals(i.getRitirato()) ? "Sì" : "No");
        map.put(L_STUDENTE, Iscrizione::getRidStudenteCf);
        map.put(L_APPELLO, i -> i.getRidAppello() != 0 ? String.valueOf(i.getRidAppello()) : "");
        return map;
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        DialogBuilder<Iscrizione> dialog = new DialogBuilder<>(
                "Nuova Iscrizione",
                "Inserisci i dati dell'iscrizione",
                campi -> {
                    @SuppressWarnings("unchecked")
                    TableView<Studente> tabStudenti = (TableView<Studente>) campi.get(L_STUDENTE);
                    tabStudenti.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Studente stud = tabStudenti.getSelectionModel().getSelectedItem();
                    if (stud == null) throw new IllegalArgumentException("Seleziona uno studente.");

                    @SuppressWarnings("unchecked")
                    TableView<Appello> tabAppelli = (TableView<Appello>) campi.get(L_APPELLO);
                    tabAppelli.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Appello app = tabAppelli.getSelectionModel().getSelectedItem();
                    if (app == null) throw new IllegalArgumentException("Seleziona un appello.");

                    LocalDate data = ((DatePicker) campi.get(L_DATA_ISCRIZIONE)).getValue();
                    boolean ritirato = ((CheckBox) campi.get(L_RITIRATO)).isSelected();

                    return new Iscrizione(
                            Main.getIscrizioneManager().assegnaIndiceCorrente(),
                            stud.getCf(),
                            Integer.parseInt(app.getId()),
                            data,
                            ritirato
                    );
                },
                i -> {
                    Main.getIscrizioneManager().aggiungi(i);
                    refresh();
                    Dialogs.showInfo("Successo", "Iscrizione aggiunta correttamente!");
                }
        );

        dialog.aggiungiCampo(L_STUDENTE, generaTabellaStudenti());
        dialog.aggiungiCampo(L_APPELLO, generaTabellaAppelli());
        dialog.aggiungiCampo(L_DATA_ISCRIZIONE, new DatePicker());
        dialog.aggiungiCampo(L_RITIRATO, new CheckBox(L_RITIRATO));
        dialog.mostra();
    }

    private void mostraDialogModifica(Iscrizione iscrizione) {
        DialogBuilder<Iscrizione> dialog = new DialogBuilder<>(
                "Modifica Iscrizione",
                "Aggiorna i dati dell'iscrizione",
                campi -> {
                    @SuppressWarnings("unchecked")
                    TableView<Studente> tabStudenti = (TableView<Studente>) campi.get(L_STUDENTE);
                    Studente stud = tabStudenti.getSelectionModel().getSelectedItem();
                    if (stud == null) throw new IllegalArgumentException("Seleziona uno studente.");

                    @SuppressWarnings("unchecked")
                    TableView<Appello> tabAppelli = (TableView<Appello>) campi.get(L_APPELLO);
                    Appello app = tabAppelli.getSelectionModel().getSelectedItem();
                    if (app == null) throw new IllegalArgumentException("Seleziona un appello.");

                    LocalDate data = ((DatePicker) campi.get(L_DATA_ISCRIZIONE)).getValue();
                    boolean ritirato = ((CheckBox) campi.get(L_RITIRATO)).isSelected();

                    // aggiorno l'oggetto
                    iscrizione.setRidStudenteCf(stud.getCf());
                    iscrizione.setRidAppello(Integer.valueOf(app.getId()));
                    iscrizione.setDataIscrizione(data);
                    iscrizione.setRitirato(ritirato);

                    // salva tramite manager
                    Main.getIscrizioneManager().aggiorna(iscrizione);
                    return iscrizione;
                },
                i -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Iscrizione modificata con successo!");
                }
        );

        TableView<Studente> tabStudenti = generaTabellaStudenti();
        tabStudenti.getItems().stream()
                .filter(s -> s.getCf().equals(iscrizione.getRidStudenteCf()))
                .findFirst().ifPresent(s -> tabStudenti.getSelectionModel().select(s));

        TableView<Appello> tabAppelli = generaTabellaAppelli();
        tabAppelli.getItems().stream()
                .filter(a -> Integer.parseInt(a.getId()) == iscrizione.getRidAppello())
                .findFirst().ifPresent(a -> tabAppelli.getSelectionModel().select(a));

        dialog.aggiungiCampo(L_STUDENTE, tabStudenti);
        dialog.aggiungiCampo(L_APPELLO, tabAppelli);
        dialog.aggiungiCampo(L_DATA_ISCRIZIONE, new DatePicker(iscrizione.getDataIscrizione()));
        dialog.aggiungiCampo(L_RITIRATO, new CheckBox(L_RITIRATO) {{ setSelected(Boolean.TRUE.equals(iscrizione.getRitirato())); }});
        dialog.mostra();
    }

    private void elimina(Iscrizione i) {
        Main.getIscrizioneManager().rimuovi(i);
        refresh();
    }

    // ===== Supporto UI =====
    private TableView<Studente> generaTabellaStudenti() {
        TableView<Studente> table = new TableView<>();
        table.setPrefHeight(220);

        TableColumn<Studente, String> colCf = new TableColumn<>("CF");
        colCf.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCf()));

        TableColumn<Studente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));

        TableColumn<Studente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCognome()));

        table.getColumns().addAll(colCf, colNome, colCognome);
        table.setItems(FXCollections.observableArrayList(Main.getStudenteManager().getAll()));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return table;
    }

    private TableView<Appello> generaTabellaAppelli() {
        TableView<Appello> table = new TableView<>();
        table.setPrefHeight(220);

        TableColumn<Appello, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));

        TableColumn<Appello, String> colIns = new TableColumn<>("Insegnamento");
        colIns.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRidInsegnamento()));

        TableColumn<Appello, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getData().toString()));

        table.getColumns().addAll(colId, colIns, colData);
        table.setItems(FXCollections.observableArrayList(Main.getAppelloManager().getAll()));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return table;
    }

    private void mostraEsameDaIscrizione(Iscrizione iscrizione) {
        Esame esame = Main.getEsameManager().getEsameDaIscrizione(String.valueOf(iscrizione.getId()));

        List<Esame> esami = new ArrayList<>();
        if (esame != null) esami.add(esame);

        Stage stage = new Stage();
        TableView<Esame> table = new TableView<>();

        TableColumn<Esame, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Esame, String> colVoto = new TableColumn<>("Voto");
        colVoto.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getVoto())));

        TableColumn<Esame, String> colLode = new TableColumn<>("Lode");
        colLode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isLode() ? "Sì" : "No"));

        table.getColumns().addAll(colId, colVoto, colLode);
        table.setItems(FXCollections.observableArrayList(esami));

        VBox layout = new VBox(10, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 300);
        stage.setScene(scene);
        stage.setTitle("Esame dell'iscrizione " + iscrizione.getId());
        stage.show();
    }
}
