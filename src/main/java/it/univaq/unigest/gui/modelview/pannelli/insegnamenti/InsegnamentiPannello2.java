package it.univaq.unigest.gui.modelview.pannelli.insegnamenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.model.Docente;
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

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaInsegnamentiPannello2;

/**
 * Pannello grafico per la gestione degli insegnamenti.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi insegnamenti</li>
 *   <li>Modificare insegnamenti esistenti</li>
 *   <li>Eliminare insegnamenti selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class InsegnamentiPannello2 {

    /**
     * Lista degli insegnamenti attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getInsegnamentoManager()}.
     */
    private static List<Insegnamento> insegnamenti = Main.getInsegnamentoManager().getAll();

    /**
     * Builder grafico di {@link Insegnamento}.
     */
    private static VistaConDettagliBuilder<Insegnamento> builder = new VistaConDettagliBuilder<>(insegnamenti);

    /**
     * Costruisce e restituisce la vista principale per la gestione degli insegnamenti.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella degli insegnamenti correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare insegnamenti</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello insegnamenti.
     */
    public static VBox getView() {

        LinkedHashMap<String, Function<Insegnamento, String>> colonne = new LinkedHashMap<>();
        colonne.put("ID", Insegnamento::getId);
        colonne.put("Nome", Insegnamento::getNome);
        colonne.put("CFU", i -> String.valueOf(i.getCfu()));
        colonne.put("Corso di Laurea", s-> s.getCorsoDiLaureaId() != null ? Main.getCorsoDiLaureaManager().getNomeCorsoDiLauraDaID(s.getCorsoDiLaureaId()) : "");
        colonne.put("Anno", i -> String.valueOf(i.getAnno()));
        colonne.put("Semestre", i -> String.valueOf(i.getSemestre()));

        LinkedHashMap<String, Function<Insegnamento, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Docenti", i -> String.join(", ", i.getDocenti()));

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.insegnamento.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.insegnamento.descrizione") + " " + iscrizione.getId()));

        dettagli.put("Visualizza Appelli", s -> "Visualizza Appelli");
        builder.setLinkAction("Visualizza Appelli", iscrizione -> mostraAppelliPerInsegnamento(iscrizione));

        return builder.build(
                "Gestione Insegnamenti",
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Insegnamento> dialogBuilder = new DialogBuilder<>(
                            "Nuovo Insegnamento",
                            "Inserisci i dati del nuovo insegnamento",
                            campi -> {
                                try {
                                    // Nome
                                    String nome = DialogsParser.validaCampo(campi, "Nome");

                                    // CFU
                                    String cfuText = DialogsParser.validaCampo(campi, "CFU");
                                    int cfu = Integer.parseInt(cfuText);

                                    // Corso di laurea
                                    @SuppressWarnings("unchecked")
                                    TableView<CorsoDiLaurea> tableCDL = (TableView<CorsoDiLaurea>) campi.get("Corso di Laurea");
                                    tableCDL.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                            // La scelta di elementi è solo 1
                                    CorsoDiLaurea cdlSelezionato = tableCDL.getSelectionModel().getSelectedItem();
                                    if (cdlSelezionato == null) {
                                        throw new IllegalArgumentException("Devi selezionare un corso di laurea!");
                                    }
                                    String cdlId = cdlSelezionato.getId();

                                    // Anno
                                    String annoText = DialogsParser.validaCampo(campi, "Anno");
                                    int anno = Integer.parseInt(annoText);

                                    // Semestre
                                    @SuppressWarnings("unchecked")
                                    ComboBox<Integer> comboSemestre = (ComboBox<Integer>) campi.get("Semestre");
                                    Integer semestre = comboSemestre.getValue();
                                    if (semestre == null) {
                                        throw new IllegalArgumentException("Devi selezionare un semestre!");
                                    }

                                    // Docenti
                                    @SuppressWarnings("unchecked")
                                    TableView<Docente> tableDocenti = (TableView<Docente>) campi.get("Docenti");
                                    List<Docente> docentiSelezionati = new ArrayList<>(tableDocenti.getSelectionModel().getSelectedItems());
                                    List<String> docenti = docentiSelezionati.stream()
                                            .map(d -> d.getNome() + " " + d.getCognome() + " (" + d.getCodiceDocente() + ")")
                                            .toList();

                                    return new Insegnamento(String.valueOf(Main.getInsegnamentoManager().assegnaIndiceCorrente()), nome, cfu, cdlId, docenti, anno, semestre);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I numeri devono essere scritti correttamente!");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            insegnamento -> {
                                Main.getInsegnamentoManager().aggiungi(insegnamento);
                                ricaricaInterfacciaGraficaInsegnamentiPannello2();
                                Dialogs.showInfo("Successo", "Insegnamento aggiunto con successo!");
                            }
                    );

                    dialogBuilder.aggiungiCampo("Nome", new TextField());
                    dialogBuilder.aggiungiCampo("CFU", new TextField());
                    dialogBuilder.aggiungiCampo("Corso di Laurea", TabelleHelper.generaTabellaFkCorsiDiLaurea(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo("Anno", new TextField());

                    ComboBox<Integer> comboSemestre = new ComboBox<>();
                    comboSemestre.setItems(FXCollections.observableArrayList(1, 2));
                    comboSemestre.setPrefWidth(150);
                    dialogBuilder.aggiungiCampo("Semestre", comboSemestre);

                    dialogBuilder.aggiungiCampo("Docenti", TabelleHelper.generaTabellaFkDocenti(SelectionMode.MULTIPLE));
                    dialogBuilder.mostra();

                },
                insegnamento -> {

                    mostraDialogModificaInsegnamento(insegnamento, builder);

                    Dialogs.showInfo("Successo", "Insegnamento modificato con successo!");
                },
                insegnamento -> {
                    Main.getInsegnamentoManager().rimuovi(insegnamento);
                    ricaricaInterfacciaGraficaInsegnamentiPannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un insegnamento selezionato sfruttando la logica di creazione.
     * @param insegnamento L'insegnamento a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista degli insegnamenti.
     */
    private static void mostraDialogModificaInsegnamento(Insegnamento insegnamento, VistaConDettagliBuilder<Insegnamento> builder) {
        DialogBuilder<Insegnamento> dialogBuilder = new DialogBuilder<>(
                "Modifica Insegnamento",
                "Modifica i dati dell'insegnamento",
                campi -> {
                    String nome = ((TextField) campi.get("Nome")).getText();
                    int cfu = Integer.parseInt(((TextField) campi.get("CFU")).getText());

                    // Corso di laurea
                    @SuppressWarnings("unchecked")
                    TableView<CorsoDiLaurea> tableCDL = (TableView<CorsoDiLaurea>) campi.get("Corso di Laurea");
                    CorsoDiLaurea cdlSelezionato = tableCDL.getSelectionModel().getSelectedItem();
                    if (cdlSelezionato == null) throw new IllegalArgumentException("Devi selezionare un corso di laurea!");

                    int anno = Integer.parseInt(((TextField) campi.get("Anno")).getText());

                    @SuppressWarnings("unchecked")
                    ComboBox<Integer> comboSemestre = (ComboBox<Integer>) campi.get("Semestre");
                    Integer semestre = comboSemestre.getValue();
                    if (semestre == null) throw new IllegalArgumentException("Devi selezionare un semestre!");

                    @SuppressWarnings("unchecked")
                    TableView<Docente> tableDocenti = (TableView<Docente>) campi.get("Docenti");
                    List<Docente> docentiSelezionati = new ArrayList<>(tableDocenti.getSelectionModel().getSelectedItems());
                    List<String> docenti = docentiSelezionati.stream()
                            .map(d -> d.getNome() + " " + d.getCognome() + " (" + d.getCodiceDocente() + ")")
                            .toList();

                    // Aggiorna l'oggetto
                    insegnamento.setNome(nome);
                    insegnamento.setCfu(cfu);
                    insegnamento.setCorsoDiLaureaId(cdlSelezionato.getId());
                    insegnamento.setAnno(anno);
                    insegnamento.setSemestre(semestre);
                    insegnamento.setDocenti(docenti);

                    return insegnamento;
                },
                i -> {
                    ricaricaInterfacciaGraficaInsegnamentiPannello2();
                    Dialogs.showInfo("Successo", "Insegnamento modificato con successo!");
                }
        );

        // Pre-seleziona i valori esistenti
        TableView<CorsoDiLaurea> tableCDL = TabelleHelper.generaTabellaFkCorsiDiLaurea(SelectionMode.SINGLE);
        tableCDL.getItems().stream()
                .filter(cdl -> cdl.getId().equals(insegnamento.getCorsoDiLaureaId()))
                .findFirst().ifPresent(cdl -> tableCDL.getSelectionModel().select(cdl));

        ComboBox<Integer> comboSemestre = new ComboBox<>(FXCollections.observableArrayList(1, 2));
        comboSemestre.setValue(insegnamento.getSemestre());

        TableView<Docente> tableDocenti = TabelleHelper.generaTabellaFkDocenti(SelectionMode.MULTIPLE);
        tableDocenti.getItems().forEach(doc -> {
            boolean selected = insegnamento.getDocenti().stream()
                    .anyMatch(d -> d.contains(doc.getCodiceDocente()));
            if (selected) tableDocenti.getSelectionModel().select(doc);
        });

        dialogBuilder.aggiungiCampo("Nome", new TextField(insegnamento.getNome()));
        dialogBuilder.aggiungiCampo("CFU", new TextField(String.valueOf(insegnamento.getCfu())));
        dialogBuilder.aggiungiCampo("Corso di Laurea", tableCDL);
        dialogBuilder.aggiungiCampo("Anno", new TextField(String.valueOf(insegnamento.getAnno())));
        dialogBuilder.aggiungiCampo("Semestre", comboSemestre);
        dialogBuilder.aggiungiCampo("Docenti", tableDocenti);

        dialogBuilder.mostra();
    }

    private static void mostraAppelliPerInsegnamento(Insegnamento insegnamento) {
        // 1) Recupera appelli legati all'insegnamento
        List<Appello> appelli = Main.getAppelloManager()
                .getAppelliDaInsegnamenti(insegnamento.getId());

        // 2) Creazione della finestra e tabella
        Stage stage = new Stage();
        TableView<Appello> table = new TableView<>();

        TableColumn<Appello, String> colId = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.appelli.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Appello, String> colData = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.appelli.data"));
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));

        TableColumn<Appello, String> colOra = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.appelli.ora"));
        colOra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOra().toString()));

        TableColumn<Appello, String> colAula = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.appelli.aula"));
        colAula.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRidAula()));

        TableColumn<Appello, String> colDocente = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.appelli.docente"));
        colDocente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRidDocente()));

        table.getColumns().addAll(colId, colData, colOra, colAula, colDocente);
        table.setItems(FXCollections.observableArrayList(appelli));

        // Pulsante PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Appelli dell'insegnamento: " + insegnamento.getNome(),
                insegnamento.getNome() + "_appelli"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("insegnamentiPannello2.appelliPannello.titolo") + insegnamento.getNome());
        stage.show();
    }

    /**
     * Apre la finestra di aggiunta studente (usa la stessa logica di getView()).
     */
    public static void apriDialogAggiungi() {
        // Riutilizziamo direttamente la logica del builder
        if (builder != null && builder.getAggiungiAction() != null) {
            builder.getAggiungiAction().run();
        }
    }

    /**
     * Modifica lo studente selezionato nella tabella.
     */
    public static void modificaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Insegnamento selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaInsegnamento(selezionato, builder);

        //
        Main.getInsegnamentoManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Insegnamento selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getInsegnamentoManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaInsegnamentiPannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista degli insegnamenti.
     *
     * @return il {@link VistaConDettagliBuilder} associato agli insegnamenti.
     */
    public static VistaConDettagliBuilder<Insegnamento> getBuilder() {
        return builder;
    }
}
