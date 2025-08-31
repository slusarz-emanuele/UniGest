package it.univaq.unigest.gui.modelview.pannelli.appelli;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.Reloader;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.model.*;
import it.univaq.unigest.util.PdfHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaStudentiPannello2;

/**
 * Pannello grafico per la gestione degli appelli.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi appelli</li>
 *   <li>Modificare appelli esistenti</li>
 *   <li>Eliminare appelli selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class AppelliPannello2 {

    /**
     * Lista degli appelli attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getAppelloManager()}.
     */
    private static List<Appello> appelli = Main.getAppelloManager().getAll();

    /**
     * Builder grafico di {@link Appello}.
     */
    private static VistaConDettagliBuilder<Appello> builder = new VistaConDettagliBuilder<>(appelli);

    /**
     * Costruisce e restituisce la vista principale per la gestione degli appelli.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella degli appelli correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare appelli</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello appelli.
     */
    public static VBox getView() {
        //List<Appello> appelli = Main.getAppelloManager().getAll();

        LinkedHashMap<String, Function<Appello, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.id"), a -> String.valueOf(a.getId()));
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.insegnamento"), Appello::getRidInsegnamento);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.data"), a -> a.getData().toString());
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.ora"), a -> a.getOra().toString());
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.aula"), s-> s.getRidAula() != null ? Main.getAulaManager().getAulaNomeDaId(s.getRidAula()) : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.docente"), s-> s.getRidDocente() != null ? Main.getDocenteManager().getGeneralitaDaCf(s.getRidDocente()) : "");

        LinkedHashMap<String, Function<Appello, String>> dettagli = new LinkedHashMap<>(colonne);

        colonne.put("Verbale", a -> a.getRidVerbale() != null ? a.getRidVerbale() : "-");   // Quello nella colonna

        dettagli.put("Verbale", a -> a.getRidVerbale() != null ? a.getRidVerbale() : "-");  // QUell0 nei dettagli

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), a -> PdfHelper.esportaEntita(a, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.appello.descrizione") + " " + a.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.appello.descrizione") + " " + a.getId()));

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("button.link.iscrizioni"), s -> Main.getParametrizzazioneHelper().getBundle().getString("button.link.iscrizioni"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("button.link.iscrizioni"), a -> mostraIscrizioniDaAppello(a));

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("button.link.verbali"), s -> Main.getParametrizzazioneHelper().getBundle().getString("button.link.verbali"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("button.link.verbali"), a -> mostraVerbaliDaAppello(a));

        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Appello> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2.nuovo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2.header"),
                            campi -> {
                                try {

                                    // Insegnamento
                                    @SuppressWarnings("unchecked")
                                    TableView<Insegnamento> tableInsegnamento = (TableView<Insegnamento>) campi.get("Insegnamento");
                                    tableInsegnamento.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                                    Insegnamento insegnamentoSelezionato = tableInsegnamento.getSelectionModel().getSelectedItem();
                                    if (insegnamentoSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.insegnamento.error"));
                                    }
                                    String insegnamentoSelezionatoStr = insegnamentoSelezionato.getId();

                                    // IDK
                                    LocalDate data = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.data"))).getValue();
                                    LocalTime ora = LocalTime.parse(((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.ora"))).getText());          // da modificare con un ora picker

                                    // Aula
                                    @SuppressWarnings("unchecked")
                                    TableView<Aula> tableAula = (TableView<Aula>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.aula"));
                                    tableAula.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                            // La scelta di elementi è solo 1
                                    Aula aulaSelezionata = tableAula.getSelectionModel().getSelectedItem();
                                    if (aulaSelezionata == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.aula.error"));
                                    }
                                    String aulaSelezionataStr = aulaSelezionata.getId();

                                    // Docenti
                                    @SuppressWarnings("unchecked")
                                    TableView<Docente> tableDocenti = (TableView<Docente>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.docente"));
                                    tableDocenti.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                            // La scelta di elementi è solo 1 ?! --> dovrebbe essere di 2 //TODO
                                    Docente docenteSelezioanto = tableDocenti.getSelectionModel().getSelectedItem();
                                    if (docenteSelezioanto == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.docente.error"));
                                    }
                                    String docenteSelezionatoID = docenteSelezioanto.getCf();

                                    return new Appello(Main.getAppelloManager().assegnaIndiceCorrente(), insegnamentoSelezionatoStr, data, ora, aulaSelezionataStr,
                                            docenteSelezionatoID,
                                            null);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            appello -> {
                                Main.getAppelloManager().aggiungi(appello);
                                Reloader.ricaricaInterfacciaGraficaAppelliPannello2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.infoSuccess1"), Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.infoSuccess2"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.insegnamento"), TabelleHelper.generaTabellaFkInsegnamento(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.data"), new DatePicker());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.ora"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.aula"), TabelleHelper.generaTabellaFkAule(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.docente"), TabelleHelper.generaTabellaFkDocenti(SelectionMode.SINGLE));
                    dialogBuilder.mostra();

                },
                appello -> {
                    mostraDialogModificaAppello(appello, builder);
                    System.out.println(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.modifica.dialog") + " " + appello.getId());
                },
                appello -> {
                    Main.getAppelloManager().rimuovi(appello);
                    Reloader.ricaricaInterfacciaGraficaAppelliPannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un appello selezionato sfruttando la logica di creazione.
     * @param appello L'appello a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista degli appelli
     */
    private static void mostraDialogModificaAppello(Appello appello, VistaConDettagliBuilder<Appello> builder) {
        DialogBuilder<Appello> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.modifica"),
                Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.modifica.header"),
                campi -> {
                    // Insegnamento
                    @SuppressWarnings("unchecked")
                    TableView<Insegnamento> tableInsegnamento = (TableView<Insegnamento>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.insegnamento"));
                    Insegnamento insegnamentoSelezionato = tableInsegnamento.getSelectionModel().getSelectedItem();
                    if (insegnamentoSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.insegnamento.error2"));

                    // Aula
                    @SuppressWarnings("unchecked")
                    TableView<Aula> tableAula = (TableView<Aula>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.aula"));
                    Aula aulaSelezionata = tableAula.getSelectionModel().getSelectedItem();
                    if (aulaSelezionata == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.insegnamento.error"));

                    // Docente
                    @SuppressWarnings("unchecked")
                    TableView<Docente> tableDocenti = (TableView<Docente>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.docente"));
                    Docente docenteSelezionato = tableDocenti.getSelectionModel().getSelectedItem();
                    if (docenteSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.docente.error"));

                    LocalDate data = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.data"))).getValue();
                    LocalTime ora = LocalTime.parse(((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.ora"))).getText());

                    // Aggiorna l'oggetto
                    appello.setRidInsegnamento(insegnamentoSelezionato.getId());
                    appello.setRidAula(aulaSelezionata.getId());
                    appello.setRidDocente(docenteSelezionato.getCf());
                    appello.setData(data);
                    appello.setOra(ora);

                    return appello;
                },
                a -> {
                    Reloader.ricaricaInterfacciaGraficaAppelliPannello2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.infoSuccess1"), Main.getParametrizzazioneHelper().getBundle().getString("builder.appelliPannello2.editSuccess"));
                }
        );

        // Tabelle con selezioni preimpostate
        TableView<Insegnamento> tableInsegnamento = TabelleHelper.generaTabellaFkInsegnamento(SelectionMode.SINGLE);
        tableInsegnamento.getItems().stream()
                .filter(i -> i.getId().equals(appello.getRidInsegnamento()))
                .findFirst().ifPresent(i -> tableInsegnamento.getSelectionModel().select(i));

        TableView<Aula> tableAula = TabelleHelper.generaTabellaFkAule(SelectionMode.SINGLE);
        tableAula.getItems().stream()
                .filter(a -> a.getId().equals(appello.getRidAula()))
                .findFirst().ifPresent(a -> tableAula.getSelectionModel().select(a));

        TableView<Docente> tableDocenti = TabelleHelper.generaTabellaFkDocenti(SelectionMode.SINGLE);
        tableDocenti.getItems().stream()
                .filter(d -> d.getCf().equals(appello.getRidDocente()))
                .findFirst().ifPresent(d -> tableDocenti.getSelectionModel().select(d));

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.insegnamento"), tableInsegnamento);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.data"), new DatePicker(appello.getData()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.ora"), new TextField(appello.getOra().toString()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.aula"), tableAula);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("campo.appelliPannello2.docente"), tableDocenti);

        dialogBuilder.mostra();
    }

    public static void mostraIscrizioniDaAppello(Appello appello) {
        // 1) Recupera tutte le iscrizioni di quell'appello
        List<Iscrizione> iscrizioni = Main.getIscrizioneManager()
                .getIscrizioniDaAppello(String.valueOf(appello.getId()));

        // 2) Crea la finestra
        Stage stage = new Stage();
        TableView<Iscrizione> table = new TableView<>();

        // Colonna ID
        TableColumn<Iscrizione, String> colId = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        // Colonna Studente
        TableColumn<Iscrizione, String> colStudente = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.studente"));
        colStudente.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRidStudenteCf() != null
                        ? Main.getStudenteManager().getGeneralitaDaCf(data.getValue().getRidStudenteCf())
                        : "-"));

        // Colonna Data iscrizione
        TableColumn<Iscrizione, String> colData = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.dataIscrizione"));
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDataIscrizione().toString()));

        // Colonna Ritirato
        TableColumn<Iscrizione, String> colRitirato = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.ritirato"));
        colRitirato.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRitirato()
                        ? Main.getParametrizzazioneHelper().getBundle().getString("si")
                        : Main.getParametrizzazioneHelper().getBundle().getString("no")));

        table.getColumns().addAll(colId, colStudente, colData, colRitirato);
        table.setItems(FXCollections.observableArrayList(iscrizioni));

        // Pulsante esportazione
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Iscrizioni all'appello ID: " + appello.getId(),
                "Iscrizioni_Appello_" + appello.getId()));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("appelliPannello2.iscrizioniPannello.titolo") + appello.getId());
        stage.show();
    }

    public static void mostraVerbaliDaAppello(Appello appello) {
        // 1) Recupera i verbali di quell'appello
        List<Verbale> verbali = Main.getVerbaleManager().getVerbaliDaAppello(String.valueOf(appello.getId()));

        // 2) Crea tabella GUI
        Stage stage = new Stage();
        TableView<Verbale> table = new TableView<>();

        // Colonna ID Verbale
        TableColumn<Verbale, String> colId = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("colonna.appelliPannello2.verbale.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        // Colonna Data Chiusura
        TableColumn<Verbale, String> colDataChiusura = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("colonna.appelliPannello2.verbale.dataChiusura"));
        colDataChiusura.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataChiusura() != null ? data.getValue().getDataChiusura().toString() : "-"
        ));

        // Colonna Stato (Aperto/Chiuso)
        TableColumn<Verbale, String> colStato = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("colonna.appelliPannello2.verbale.stato"));
        colStato.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getChiuso() ? Main.getParametrizzazioneHelper().getBundle().getString("valore.appelliPannello2.verbale.chiuso") : Main.getParametrizzazioneHelper().getBundle().getString("valore.appelliPannello2.verbale.aperto")
        ));

        // Colonna Firmato
        TableColumn<Verbale, String> colFirmato = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("colonna.appelliPannello2.verbale.firmato"));
        colFirmato.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFirmato() ? Main.getParametrizzazioneHelper().getBundle().getString("valore.appelliPannello2.verbale.si") : Main.getParametrizzazioneHelper().getBundle().getString("valore.appelliPannello2.verbale.no")
        ));

        // Colonna Note
        TableColumn<Verbale, String> colNote = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("colonna.appelliPannello2.verbale.note"));
        colNote.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNote() != null ? data.getValue().getNote() : "-"
        ));

        table.getColumns().addAll(colId, colDataChiusura, colStato, colFirmato, colNote);
        table.setItems(FXCollections.observableArrayList(verbali));

        // Pulsante PDF
        Button exportBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.appelliPannello2.verbali.esporta"));
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2.verbaliFinestra") + appello.getId(),
                Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2.verbaliFinestra2") + appello.getId()));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle().getString("titolo.appelliPannello2.verbaliFinestra") + appello.getId());
        stage.show();
    }


    // Funzione helper per creare una tabella con colonne standard
    private static TableView<Verbale> creaTabellaVerbali(List<Verbale> verbali) {
        TableView<Verbale> table = new TableView<>();

        TableColumn<Verbale, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Verbale, String> colDataChiusura = new TableColumn<>("Data Chiusura");
        colDataChiusura.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataChiusura() != null ? data.getValue().getDataChiusura().toString() : "-"
        ));

        TableColumn<Verbale, String> colChiuso = new TableColumn<>("Chiuso");
        colChiuso.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getChiuso() ? "Sì" : "No"
        ));

        TableColumn<Verbale, String> colFirmato = new TableColumn<>("Firmato");
        colFirmato.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFirmato() ? "Sì" : "No"
        ));

        TableColumn<Verbale, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNote() != null ? data.getValue().getNote() : "-"
        ));

        table.getColumns().addAll(colId, colDataChiusura, colChiuso, colFirmato, colNote);
        table.setItems(FXCollections.observableArrayList(verbali));

        return table;
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
        Appello selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaAppello(selezionato, builder);

        //
        Main.getAppelloManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Appello selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getAppelloManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaAppelliPannello2();
    }


    /**
     * Restituisce il builder grafico utilizzato per costruire la vista degli appelli.
     *
     * @return il {@link VistaConDettagliBuilder} associato agli appelli.
     */
    public static VistaConDettagliBuilder<Appello> getBuilder() {
        return builder;
    }
}
