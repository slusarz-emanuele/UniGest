package it.univaq.unigest.gui.modelview.pannelli.appelli;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.service.AppelloService;
import it.univaq.unigest.util.PdfHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AppelliPannello2 implements CrudPanel {

    // Etichette centralizzate
    private static final String L_ID           = "ID";
    private static final String L_INSEGNAMENTO = "Insegnamento";
    private static final String L_DATA         = "Data";
    private static final String L_ORA          = "Ora (HH:mm)";
    private static final String L_AULA         = "Aula";
    private static final String L_DOCENTE      = "Docente";
    private static final String L_VERBALE      = "Verbale";

    // Dipendenze
    private final VistaConDettagliBuilder<Appello> builder;
    private final AppelloService appelloService;

    // Loader esterni
    private final Supplier<List<Insegnamento>> loadInsegnamenti;
    private final Supplier<List<Aula>> loadAula;
    private final Supplier<List<Docente>> loadDocenti;

    public AppelliPannello2(AppelloService appelloService,
                            Supplier<List<Insegnamento>> loadInsegnamenti,
                            Supplier<List<Aula>> loadAula,
                            Supplier<List<Docente>> loadDocenti) {
        this.appelloService = appelloService;
        this.loadInsegnamenti = loadInsegnamenti;
        this.loadAula = loadAula;
        this.loadDocenti = loadDocenti;
        this.builder = new VistaConDettagliBuilder<>(appelloService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Appello, String>> colonne = colonne();

        // Dettagli = colonne + link azioni
        LinkedHashMap<String, Function<Appello, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Esporta in PDF", a -> "Esporta in PDF");
        builder.setLinkAction("Esporta in PDF",
                a -> PdfHelper.esportaEntita(a, "Appello " + a.getId(), "Appello_" + a.getId()));

        dettagli.put("Iscrizioni", a -> "Apri");
        builder.setLinkAction("Iscrizioni", this::mostraIscrizioniDaAppello);

        dettagli.put("Verbali", a -> "Apri");
        builder.setLinkAction("Verbali", this::mostraVerbaliDaAppello);

        return builder.build(
                "Gestione Appelli",
                colonne,
                dettagli,
                this::apriDialogAggiungi,
                this::mostraDialogModificaAppello,
                this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un appello."); return; }
        mostraDialogModificaAppello(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un appello."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(Main.getAppelloManager().getAll());
    }

    public VistaConDettagliBuilder<Appello> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Appello, String>> colonne() {
        LinkedHashMap<String, Function<Appello, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Appello::getId);
        // Mostriamo l'ID dell'insegnamento; se hai un helper per il nome, sostituisci qui.
        map.put(L_INSEGNAMENTO, Appello::getRidInsegnamento);
        map.put(L_DATA, a -> a.getData() != null ? a.getData().toString() : "");
        map.put(L_ORA,  a -> a.getOra() != null ? a.getOra().toString() : "");
        map.put(L_AULA, a -> a.getRidAula() != null ? Main.getAulaManager().getAulaNomeDaId(a.getRidAula()) : "");
        map.put(L_DOCENTE, a -> a.getRidDocente() != null ? Main.getDocenteManager().getGeneralitaDaCf(a.getRidDocente()) : "");
        map.put(L_VERBALE, a -> a.getRidVerbale() != null ? a.getRidVerbale() : "-");
        return map;
    }

    // ===== Dialoghi CRUD =====

    private void apriDialogAggiungi() {
        DialogBuilder<Appello> dialog = new DialogBuilder<>(
                "Nuovo Appello",
                "Inserisci i dati dell'appello",
                campi -> {
                    // Insegnamento (tabella single)
                    @SuppressWarnings("unchecked")
                    TableView<Insegnamento> tblIns = (TableView<Insegnamento>) campi.get(L_INSEGNAMENTO);
                    tblIns.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Insegnamento insSel = tblIns.getSelectionModel().getSelectedItem();
                    if (insSel == null) throw new IllegalArgumentException("Seleziona un insegnamento.");

                    // Data
                    LocalDate data = ((DatePicker) campi.get(L_DATA)).getValue();
                    if (data == null) throw new IllegalArgumentException("Seleziona una data.");

                    // Ora
                    String oraTxt = DialogsParser.validaCampo(campi, L_ORA);
                    LocalTime ora;
                    try {
                        ora = LocalTime.parse(oraTxt);
                    } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException("Ora non valida. Usa formato HH:mm.");
                    }

                    // Aula (tabella single)
                    @SuppressWarnings("unchecked")
                    TableView<Aula> tblAule = (TableView<Aula>) campi.get(L_AULA);
                    tblAule.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Aula aulaSel = tblAule.getSelectionModel().getSelectedItem();
                    if (aulaSel == null) throw new IllegalArgumentException("Seleziona un'aula.");

                    // Docente (tabella single)
                    @SuppressWarnings("unchecked")
                    TableView<Docente> tblDoc = (TableView<Docente>) campi.get(L_DOCENTE);
                    tblDoc.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Docente docSel = tblDoc.getSelectionModel().getSelectedItem();
                    if (docSel == null) throw new IllegalArgumentException("Seleziona un docente.");

                    Appello nuovo = new Appello(
                            Main.getAppelloManager().assegnaIndiceCorrente(),
                            insSel.getId(),
                            data,
                            ora,
                            aulaSel.getId(),
                            docSel.getCf(),
                            null // ridVerbale inizialmente nullo
                    );
                    Main.getAppelloManager().aggiungi(nuovo);
                    return nuovo;
                },
                a -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Appello aggiunto con successo!");
                }
        );

        dialog.aggiungiCampo(L_INSEGNAMENTO, TabelleHelper.generaTabellaFkInsegnamento(SelectionMode.SINGLE));
        dialog.aggiungiCampo(L_DATA, new DatePicker());
        dialog.aggiungiCampo(L_ORA, new TextField());
        dialog.aggiungiCampo(L_AULA, TabelleHelper.generaTabellaFkAule(SelectionMode.SINGLE));
        dialog.aggiungiCampo(L_DOCENTE, TabelleHelper.generaTabellaFkDocenti(SelectionMode.SINGLE));
        dialog.mostra();
    }

    private void mostraDialogModificaAppello(Appello appello) {
        DialogBuilder<Appello> dialog = new DialogBuilder<>(
                "Modifica Appello",
                "Aggiorna i dati dell'appello",
                campi -> {
                    // Insegnamento
                    @SuppressWarnings("unchecked")
                    TableView<Insegnamento> tblIns = (TableView<Insegnamento>) campi.get(L_INSEGNAMENTO);
                    Insegnamento insSel = tblIns.getSelectionModel().getSelectedItem();
                    if (insSel == null) throw new IllegalArgumentException("Seleziona un insegnamento.");

                    // Aula
                    @SuppressWarnings("unchecked")
                    TableView<Aula> tblAule = (TableView<Aula>) campi.get(L_AULA);
                    Aula aulaSel = tblAule.getSelectionModel().getSelectedItem();
                    if (aulaSel == null) throw new IllegalArgumentException("Seleziona un'aula.");

                    // Docente
                    @SuppressWarnings("unchecked")
                    TableView<Docente> tblDoc = (TableView<Docente>) campi.get(L_DOCENTE);
                    Docente docSel = tblDoc.getSelectionModel().getSelectedItem();
                    if (docSel == null) throw new IllegalArgumentException("Seleziona un docente.");

                    // Data/Ora
                    LocalDate data = ((DatePicker) campi.get(L_DATA)).getValue();
                    if (data == null) throw new IllegalArgumentException("Seleziona una data.");

                    String oraTxt = DialogsParser.validaCampo(campi, L_ORA);
                    LocalTime ora;
                    try {
                        ora = LocalTime.parse(oraTxt);
                    } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException("Ora non valida. Usa formato HH:mm.");
                    }

                    // Aggiorna
                    appello.setRidInsegnamento(insSel.getId());
                    appello.setRidAula(aulaSel.getId());
                    appello.setRidDocente(docSel.getCf());
                    appello.setData(data);
                    appello.setOra(ora);

                    Main.getAppelloManager().aggiorna(appello);
                    return appello;
                },
                a -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Appello modificato con successo!");
                }
        );

        // Preselezioni
        TableView<Insegnamento> tblIns = TabelleHelper.generaTabellaFkInsegnamento(SelectionMode.SINGLE);
        tblIns.getItems().stream()
                .filter(i -> i.getId().equals(appello.getRidInsegnamento()))
                .findFirst().ifPresent(i -> tblIns.getSelectionModel().select(i));

        TableView<Aula> tblAule = TabelleHelper.generaTabellaFkAule(SelectionMode.SINGLE);
        tblAule.getItems().stream()
                .filter(a -> a.getId().equals(appello.getRidAula()))
                .findFirst().ifPresent(a -> tblAule.getSelectionModel().select(a));

        TableView<Docente> tblDoc = TabelleHelper.generaTabellaFkDocenti(SelectionMode.SINGLE);
        tblDoc.getItems().stream()
                .filter(d -> d.getCf().equals(appello.getRidDocente()))
                .findFirst().ifPresent(d -> tblDoc.getSelectionModel().select(d));

        dialog.aggiungiCampo(L_INSEGNAMENTO, tblIns);
        dialog.aggiungiCampo(L_DATA, new DatePicker(appello.getData()));
        dialog.aggiungiCampo(L_ORA, new TextField(appello.getOra() != null ? appello.getOra().toString() : ""));
        dialog.aggiungiCampo(L_AULA, tblAule);
        dialog.aggiungiCampo(L_DOCENTE, tblDoc);

        dialog.mostra();
    }

    private void elimina(Appello a) {
        Main.getAppelloManager().rimuovi(a);
        refresh();
    }

    // ===== Finestrine di dettaglio =====

    private void mostraIscrizioniDaAppello(Appello appello) {
        List<Iscrizione> iscrizioni = Main.getIscrizioneManager()
                .getIscrizioniDaAppello(String.valueOf(appello.getId()));

        Stage stage = new Stage();
        TableView<Iscrizione> table = new TableView<>();

        TableColumn<Iscrizione, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));

        TableColumn<Iscrizione, String> colStudente = new TableColumn<>("Studente");
        colStudente.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getRidStudenteCf() != null
                        ? Main.getStudenteManager().getGeneralitaDaCf(d.getValue().getRidStudenteCf())
                        : "-"
        ));

        TableColumn<Iscrizione, String> colData = new TableColumn<>("Data Iscrizione");
        colData.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataIscrizione() != null ? d.getValue().getDataIscrizione().toString() : "-"
        ));

        TableColumn<Iscrizione, String> colRitirato = new TableColumn<>("Ritirato");
        colRitirato.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRitirato() ? "Sì" : "No"));

        table.getColumns().addAll(colId, colStudente, colData, colRitirato);
        table.setItems(FXCollections.observableArrayList(iscrizioni));

        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Iscrizioni all'appello ID: " + appello.getId(),
                "Iscrizioni_Appello_" + appello.getId()
        ));

        VBox root = new VBox(10, table, exportBtn);
        root.setStyle("-fx-padding: 10;");

        stage.setScene(new Scene(root, 700, 400));
        stage.setTitle("Iscrizioni — Appello " + appello.getId());
        stage.show();
    }

    private void mostraVerbaliDaAppello(Appello appello) {
        List<Verbale> verbali = Main.getVerbaleManager().getVerbaliDaAppello(String.valueOf(appello.getId()));

        Stage stage = new Stage();
        TableView<Verbale> table = new TableView<>();

        TableColumn<Verbale, String> colId = new TableColumn<>("ID Verbale");
        colId.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));

        TableColumn<Verbale, String> colDataCh = new TableColumn<>("Data Chiusura");
        colDataCh.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getDataChiusura() != null ? d.getValue().getDataChiusura().toString() : "-"
        ));

        TableColumn<Verbale, String> colStato = new TableColumn<>("Stato");
        colStato.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getChiuso() ? "Chiuso" : "Aperto"));

        TableColumn<Verbale, String> colFirmato = new TableColumn<>("Firmato");
        colFirmato.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFirmato() ? "Sì" : "No"));

        TableColumn<Verbale, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getNote() != null ? d.getValue().getNote() : "-"
        ));

        table.getColumns().addAll(colId, colDataCh, colStato, colFirmato, colNote);
        table.setItems(FXCollections.observableArrayList(verbali));

        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Verbali dell'appello ID: " + appello.getId(),
                "Verbali_Appello_" + appello.getId()
        ));

        VBox root = new VBox(10, table, exportBtn);
        root.setStyle("-fx-padding: 10;");

        stage.setScene(new Scene(root, 700, 400));
        stage.setTitle("Verbali — Appello " + appello.getId());
        stage.show();
    }
}
