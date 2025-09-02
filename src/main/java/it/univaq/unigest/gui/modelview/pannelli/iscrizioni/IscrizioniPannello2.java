package it.univaq.unigest.gui.modelview.pannelli.iscrizioni;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.LocalDateUtil;
import it.univaq.unigest.util.ParametrizzazioneHelper;
import it.univaq.unigest.util.PdfHelper;
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

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaIscrizioniPannello2;

/**
 * Pannello grafico per la gestione delle iscrizioni agli esami.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuove iscrizioni</li>
 *   <li>Modificare iscrizioni esistenti</li>
 *   <li>Eliminare iscrizioni selezionate</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class IscrizioniPannello2 {

    /**
     * Lista delle iscrizioni attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getIscrizioneManager()}.
     */
    private static List<Iscrizione> iscrizioni = Main.getIscrizioneManager().getAll();

    /**
     * Builder grafico di {@link Iscrizione}.
     */
    private static VistaConDettagliBuilder<Iscrizione> builder = new VistaConDettagliBuilder<>(iscrizioni);

    /**
     * Costruisce e restituisce la vista principale per la gestione delle iscrizioni.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella delle iscrizioni correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare iscrizioni</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello iscrizioni.
     */
    public static VBox getView() {

        // Selezione tabellare, variabili visibili nella tabella
        LinkedHashMap<String, Function<Iscrizione, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.id"), a -> String.valueOf(a.getId()));
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.dataIscrizione"), a -> LocalDateUtil.toString(a.getDataIscrizione()));
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.ritirato"), a -> String.valueOf(a.getRitirato()));

        // Selezione dettagliata
        LinkedHashMap<String, Function<Iscrizione, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.idStudente"), a -> String.valueOf(a.getRidStudenteCf()));
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.idAppello"), a -> String.valueOf(a.getRidAppello()));

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.iscrizione.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.iscrizione.descrizione") + " " + iscrizione.getId()));

        dettagli.put("Visualizza Esame", s -> "Visualizza Esame");
        builder.setLinkAction("Visualizza Esame", iscrizione -> mostraEsameDaIscrizione(iscrizione));

        // Builder creazione oggetto
        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.titolo"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Iscrizione> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.finestraAggiungi.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.finestraAggiungi.header"),
                            campi -> {
                                try {

                                    // ID Studente
                                    @SuppressWarnings("unchecked")
                                    TableView<Studente> tableStudenti = (TableView<Studente>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.idStudente"));
                                    tableStudenti.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                                    Studente studenteSelezionato = tableStudenti.getSelectionModel().getSelectedItem();
                                    if (studenteSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.idStudente.error"));
                                    }
                                    String studenteSelezionatoStr = studenteSelezionato.getCf();                                        // ID del corso selezionato

                                    // ID Appello
                                    @SuppressWarnings("unchecked")
                                    TableView<Appello> tableAppelli = (TableView<Appello>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.idAppello"));
                                    tableAppelli.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                                    Appello appelloSelezionato = tableAppelli.getSelectionModel().getSelectedItem();
                                    if (appelloSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.idAppello.error"));
                                    }
                                    int appelloSelezionatoStr = appelloSelezionato.getId();                                             // ID del corso selezionatto

                                    LocalDate dataIscrizione = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataIscrizione"))).getValue();                  // Data Iscrizione


                                    return new Iscrizione(
                                            Main.getIscrizioneManager().assegnaIndiceCorrente(),     // Ultimo valore maggiore degli ID
                                            studenteSelezionatoStr,
                                            appelloSelezionatoStr,
                                            dataIscrizione,
                                            false);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I CFU devono essere un numero valido.");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            iscrizione -> {
                                Main.getIscrizioneManager().aggiungi(iscrizione);
                                ricaricaInterfacciaGraficaIscrizioniPannello2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.success"), Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.success.message"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.idStudente"), TabelleHelper.generaTabellaFkStudenti(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.idAppello"), TabelleHelper.generaTabellaFkAppelli(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataIscrizione"), new DatePicker());
                    dialogBuilder.mostra();

                },
                iscrizione -> {
                    mostraDialogModifica(iscrizione, builder);
                },
                iscrizione -> {
                    Main.getIscrizioneManager().rimuovi(iscrizione);
                    ricaricaInterfacciaGraficaIscrizioniPannello2();
                }
        );
    }

    /**
     * Crea la finestra di modifica di un singolo record.
     * @param iscrizione Oggetto selezionato nella tabella del gestionale
     * @param builder il {@link VistaConDettagliBuilder} associato alla vista corrente
     */
    private static void mostraDialogModifica(Iscrizione iscrizione, VistaConDettagliBuilder<Iscrizione> builder) {
        DialogBuilder<Iscrizione> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.finestraModifica.titolo"),
                Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.finestraModifica.header"),
                campi -> {
                    TableView<Studente> tableStudenti = (TableView<Studente>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.idStudente"));
                    Studente studenteSelezionato = tableStudenti.getSelectionModel().getSelectedItem();
                    if (studenteSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.idStudente.error"));

                    TableView<Appello> tableAppelli = (TableView<Appello>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.idAppello"));
                    Appello appelloSelezionato = tableAppelli.getSelectionModel().getSelectedItem();
                    if (appelloSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.idAppello.error"));

                    LocalDate dataIscrizione = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataIscrizione"))).getValue();

                    iscrizione.setRidStudenteCf(studenteSelezionato.getCf());
                    iscrizione.setRidAppello(appelloSelezionato.getId());
                    iscrizione.setDataIscrizione(dataIscrizione);

                    return iscrizione;
                },
                is -> {
                    ricaricaInterfacciaGraficaIscrizioniPannello2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.success"), Main.getParametrizzazioneHelper().getBundle().getString("iscrizioniPannello2.builder.success.message"));
                }
        );

        TableView<Studente> tableStudenti = TabelleHelper.generaTabellaFkStudenti(SelectionMode.SINGLE);
        tableStudenti.getItems().stream()
                .filter(s -> s.getCf().equals(iscrizione.getRidStudenteCf()))
                .findFirst().ifPresent(s -> tableStudenti.getSelectionModel().select(s));

        TableView<Appello> tableAppelli = TabelleHelper.generaTabellaFkAppelli(SelectionMode.SINGLE);
        tableAppelli.getItems().stream()
                .filter(a -> a.getId() == iscrizione.getRidAppello())
                .findFirst().ifPresent(a -> tableAppelli.getSelectionModel().select(a));

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.idStudente"), tableStudenti);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.idAppello"), tableAppelli);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataIscrizione"), new DatePicker(iscrizione.getDataIscrizione()));

        dialogBuilder.mostra();
    }

    public static void mostraEsameDaIscrizione(Iscrizione iscrizione) {
        // 1) Recupera l'esame associato all'iscrizione
        Esame esame = Main.getEsameManager().getEsameDaIscrizione(String.valueOf(iscrizione.getId()));

        // 2) Crea una lista (anche se è solo uno, la TableView vuole una lista)
        List<Esame> esami = new ArrayList<>();
        if (esame != null) {
            esami.add(esame);
        }

        // 3) Costruisci la finestra
        Stage stage = new Stage();
        TableView<Esame> table = new TableView<>();

        // Colonna ID Esame
        TableColumn<Esame, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        // Colonna Voto
        TableColumn<Esame, String> colVoto = new TableColumn<>("Voto");
        colVoto.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getVoto())));

        // Colonna Lode
        TableColumn<Esame, String> colLode = new TableColumn<>("Lode");
        colLode.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().isLode() ?
                        Main.getParametrizzazioneHelper().getBundle().getString("si") :
                        Main.getParametrizzazioneHelper().getBundle().getString("no")
        ));

        table.getColumns().addAll(colId, colVoto, colLode);
        table.setItems(FXCollections.observableArrayList(esami));

        // Pulsante PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Esame dell'iscrizione " + iscrizione.getId(),
                "Esame_Iscrizione_" + iscrizione.getId()
        ));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 300);
        stage.setScene(scene);
        stage.setTitle("Esame Iscrizione: " + iscrizione.getId());
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
        Iscrizione selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModifica(selezionato, builder);

        //
        Main.getIscrizioneManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Iscrizione selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getIscrizioneManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaIscrizioniPannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista delle iscrizioni.
     *
     * @return il {@link VistaConDettagliBuilder} associato alle iscrizioni.
     */
    public static VistaConDettagliBuilder<Iscrizione> getBuilder() {
        return builder;
    }
}
