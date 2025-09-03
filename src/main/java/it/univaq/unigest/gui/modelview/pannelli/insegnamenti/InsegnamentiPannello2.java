package it.univaq.unigest.gui.modelview.pannelli.insegnamenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.InsegnamentoService;
import it.univaq.unigest.util.PdfHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class InsegnamentiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID        = "ID";
    private static final String L_NOME      = "Nome";
    private static final String L_CFU       = "CFU";
    private static final String L_CDL       = "Corso di Laurea";
    private static final String L_ANNO      = "Anno";
    private static final String L_SEMESTRE  = "Semestre";
    private static final String L_DOCENTI   = "Docenti";

    // Dipendenze
    private final InsegnamentoService insegnamentoService;

    // Loader Liste esterne
    private final Supplier<List<CorsoDiLaurea>> loadCorsi;
    private final Supplier<List<Docente>> loadDocenti;

    private final VistaConDettagliBuilder<Insegnamento> builder;


    public InsegnamentiPannello2(InsegnamentoService insegnamentoService,
                                 Supplier<List<CorsoDiLaurea>> loadCorsi,
                                 Supplier<List<Docente>> loadDocenti) {
        this.insegnamentoService = insegnamentoService;
        this.loadCorsi = loadCorsi;
        this.loadDocenti = loadDocenti;
        this.builder = new VistaConDettagliBuilder<>(insegnamentoService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Insegnamento, String>> colonne = colonne();
        LinkedHashMap<String, Function<Insegnamento, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put(L_DOCENTI, i -> String.join(", ", i.getDocenti()));
        dettagli.put("Visualizza Appelli", i -> "Visualizza Appelli");
        builder.setLinkAction("Visualizza Appelli", this::mostraAppelliPerInsegnamento);

        return builder.build(
                "Gestione Insegnamenti",
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un insegnamento."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un insegnamento."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(Main.getInsegnamentoManager().getAll());
    }

    public VistaConDettagliBuilder<Insegnamento> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Insegnamento, String>> colonne() {
        LinkedHashMap<String, Function<Insegnamento, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Insegnamento::getId);
        map.put(L_NOME, Insegnamento::getNome);
        map.put(L_CFU, i -> String.valueOf(i.getCfu()));
        map.put(L_CDL, i -> {
            String id = i.getCorsoDiLaureaId();
            return id != null ? Main.getCorsoDiLaureaManager().getNomeCorsoDiLauraDaID(id) : "";
        });
        map.put(L_ANNO, i -> String.valueOf(i.getAnno()));
        map.put(L_SEMESTRE, i -> String.valueOf(i.getSemestre()));
        return map;
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        DialogBuilder<Insegnamento> dialog = new DialogBuilder<>(
                "Nuovo Insegnamento",
                "Inserisci i dati del nuovo insegnamento",
                campi -> {
                    String nome = DialogsParser.validaCampo(campi, L_NOME);

                    int cfu;
                    try { cfu = Integer.parseInt(DialogsParser.validaCampo(campi, L_CFU)); }
                    catch (NumberFormatException ex) { throw new IllegalArgumentException("CFU non valido."); }

                    @SuppressWarnings("unchecked")
                    TableView<CorsoDiLaurea> tabCdl = (TableView<CorsoDiLaurea>) campi.get(L_CDL);
                    tabCdl.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    CorsoDiLaurea cdl = tabCdl.getSelectionModel().getSelectedItem();
                    if (cdl == null) throw new IllegalArgumentException("Seleziona un corso di laurea.");

                    int anno;
                    try { anno = Integer.parseInt(DialogsParser.validaCampo(campi, L_ANNO)); }
                    catch (NumberFormatException ex) { throw new IllegalArgumentException("Anno non valido."); }

                    @SuppressWarnings("unchecked")
                    ComboBox<Integer> cbSem = (ComboBox<Integer>) campi.get(L_SEMESTRE);
                    Integer semestre = cbSem.getValue();
                    if (semestre == null) throw new IllegalArgumentException("Seleziona un semestre.");

                    @SuppressWarnings("unchecked")
                    TableView<Docente> tabDoc = (TableView<Docente>) campi.get(L_DOCENTI);
                    List<Docente> selDoc = new ArrayList<>(tabDoc.getSelectionModel().getSelectedItems());
                    List<String> docenti = selDoc.stream()
                            .map(d -> d.getNome() + " " + d.getCognome() + " (" + d.getCodiceDocente() + ")")
                            .toList();

                    Insegnamento nuovo = new Insegnamento(
                            String.valueOf(Main.getInsegnamentoManager().assegnaIndiceCorrente()),
                            nome, cfu, cdl.getId(), docenti, anno, semestre
                    );

                    Main.getInsegnamentoManager().aggiungi(nuovo);
                    return nuovo;
                },
                i -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Insegnamento aggiunto con successo!");
                }
        );

        dialog.aggiungiCampo(L_NOME, new TextField());
        dialog.aggiungiCampo(L_CFU, new TextField());
        dialog.aggiungiCampo(L_CDL, generaTabellaCdl());
        dialog.aggiungiCampo(L_ANNO, new TextField());
        dialog.aggiungiCampo(L_SEMESTRE, new ComboBox<>(FXCollections.observableArrayList(1, 2)));
        dialog.aggiungiCampo(L_DOCENTI, generaTabellaDocenti(true));
        dialog.mostra();
    }

    private void mostraDialogModifica(Insegnamento ins) {
        DialogBuilder<Insegnamento> dialog = new DialogBuilder<>(
                "Modifica Insegnamento",
                "Aggiorna i dati dell'insegnamento",
                campi -> {
                    String nome = ((TextField) campi.get(L_NOME)).getText();

                    int cfu;
                    try { cfu = Integer.parseInt(((TextField) campi.get(L_CFU)).getText()); }
                    catch (NumberFormatException ex) { throw new IllegalArgumentException("CFU non valido."); }

                    @SuppressWarnings("unchecked")
                    TableView<CorsoDiLaurea> tabCdl = (TableView<CorsoDiLaurea>) campi.get(L_CDL);
                    CorsoDiLaurea cdl = tabCdl.getSelectionModel().getSelectedItem();
                    if (cdl == null) throw new IllegalArgumentException("Seleziona un corso di laurea.");

                    int anno;
                    try { anno = Integer.parseInt(((TextField) campi.get(L_ANNO)).getText()); }
                    catch (NumberFormatException ex) { throw new IllegalArgumentException("Anno non valido."); }

                    @SuppressWarnings("unchecked")
                    ComboBox<Integer> cbSem = (ComboBox<Integer>) campi.get(L_SEMESTRE);
                    Integer semestre = cbSem.getValue();
                    if (semestre == null) throw new IllegalArgumentException("Seleziona un semestre.");

                    @SuppressWarnings("unchecked")
                    TableView<Docente> tabDoc = (TableView<Docente>) campi.get(L_DOCENTI);
                    List<Docente> selDoc = new ArrayList<>(tabDoc.getSelectionModel().getSelectedItems());
                    List<String> docenti = selDoc.stream()
                            .map(d -> d.getNome() + " " + d.getCognome() + " (" + d.getCodiceDocente() + ")")
                            .toList();

                    // aggiorna e salva
                    ins.setNome(nome);
                    ins.setCfu(cfu);
                    ins.setCorsoDiLaureaId(cdl.getId());
                    ins.setAnno(anno);
                    ins.setSemestre(semestre);
                    ins.setDocenti(docenti);

                    Main.getInsegnamentoManager().aggiorna(ins);
                    return ins;
                },
                i -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Insegnamento modificato con successo!");
                }
        );

        // Pre-popola i controlli
        TableView<CorsoDiLaurea> tabCdl = generaTabellaCdl();
        tabCdl.getItems().stream()
                .filter(c -> c.getId().equals(ins.getCorsoDiLaureaId()))
                .findFirst().ifPresent(c -> tabCdl.getSelectionModel().select(c));

        ComboBox<Integer> cbSem = new ComboBox<>(FXCollections.observableArrayList(1, 2));
        cbSem.setValue(ins.getSemestre());

        TableView<Docente> tabDoc = generaTabellaDocenti(true);
        tabDoc.getItems().forEach(doc -> {
            boolean selected = ins.getDocenti() != null &&
                    ins.getDocenti().stream().anyMatch(s -> s.contains(doc.getCodiceDocente()));
            if (selected) tabDoc.getSelectionModel().select(doc);
        });

        dialog.aggiungiCampo(L_ID, new TextField(ins.getId()) {{ setEditable(false); }});
        dialog.aggiungiCampo(L_NOME, new TextField(ins.getNome()));
        dialog.aggiungiCampo(L_CFU, new TextField(String.valueOf(ins.getCfu())));
        dialog.aggiungiCampo(L_CDL, tabCdl);
        dialog.aggiungiCampo(L_ANNO, new TextField(String.valueOf(ins.getAnno())));
        dialog.aggiungiCampo(L_SEMESTRE, cbSem);
        dialog.aggiungiCampo(L_DOCENTI, tabDoc);
        dialog.mostra();
    }

    private void elimina(Insegnamento i) {
        Main.getInsegnamentoManager().rimuovi(i);
        refresh();
    }

    // ===== Supporto UI: tabelle di scelta =====
    private TableView<CorsoDiLaurea> generaTabellaCdl() {
        TableView<CorsoDiLaurea> table = new TableView<>();
        table.setPrefHeight(220);

        TableColumn<CorsoDiLaurea, String> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));

        TableColumn<CorsoDiLaurea, String> c2 = new TableColumn<>("Nome");
        c2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));

        TableColumn<CorsoDiLaurea, String> c3 = new TableColumn<>("CFU totali");
        c3.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCfuTotali())));

        table.getColumns().addAll(c1, c2, c3);
        table.setItems(FXCollections.observableArrayList(Main.getCorsoDiLaureaManager().getAll()));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return table;
    }

    private TableView<Docente> generaTabellaDocenti(boolean multiple) {
        TableView<Docente> table = new TableView<>();
        table.setPrefHeight(240);

        TableColumn<Docente, String> c1 = new TableColumn<>("CF");
        c1.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCf()));

        TableColumn<Docente, String> c2 = new TableColumn<>("Nome");
        c2.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));

        TableColumn<Docente, String> c3 = new TableColumn<>("Cognome");
        c3.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCognome()));

        TableColumn<Docente, String> c4 = new TableColumn<>("Codice");
        c4.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodiceDocente()));

        table.getColumns().addAll(c1, c2, c3, c4);
        table.setItems(FXCollections.observableArrayList(Main.getDocenteManager().getAll()));
        table.getSelectionModel().setSelectionMode(multiple ? SelectionMode.MULTIPLE : SelectionMode.SINGLE);
        return table;
    }

    // ===== Link: Visualizza Appelli =====
    private void mostraAppelliPerInsegnamento(Insegnamento insegnamento) {
        List<Appello> appelli = Main.getAppelloManager().getAppelliDaInsegnamenti(insegnamento.getId());

        Stage stage = new Stage();
        TableView<Appello> table = new TableView<>();

        TableColumn<Appello, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Appello, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));

        TableColumn<Appello, String> colOra = new TableColumn<>("Ora");
        colOra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOra().toString()));

        TableColumn<Appello, String> colAula = new TableColumn<>("Aula");
        colAula.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRidAula() != null
                        ? Main.getAulaManager().getAulaNomeDaId(data.getValue().getRidAula())
                        : "-"
        ));

        TableColumn<Appello, String> colDocente = new TableColumn<>("Docente");
        colDocente.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRidDocente() != null
                        ? Main.getDocenteManager().getGeneralitaDaCf(data.getValue().getRidDocente())
                        : "-"
        ));

        table.getColumns().addAll(colId, colData, colOra, colAula, colDocente);
        table.setItems(FXCollections.observableArrayList(appelli));

        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Appelli dell'insegnamento: " + insegnamento.getNome(),
                insegnamento.getNome() + "_appelli"
        ));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 760, 420);
        stage.setScene(scene);
        stage.setTitle("Appelli • " + insegnamento.getNome());
        stage.show();
    }
}
