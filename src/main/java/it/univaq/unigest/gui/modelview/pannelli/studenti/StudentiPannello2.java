package it.univaq.unigest.gui.modelview.pannelli.studenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.LocalDateUtil;
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

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaStudentiPannello2;

/**
 * Pannello grafico per la gestione degli studenti.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi studenti</li>
 *   <li>Modificare studenti esistenti</li>
 *   <li>Eliminare studenti selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class StudentiPannello2 {

    /**
     * Lista degli studenti attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getStudenteManager()}.
     */
    private static List<Studente> studenti = Main.getStudenteManager().getAll();

    /**
     * Builder grafico di {@link Studente}.
     */
    private static VistaConDettagliBuilder<Studente> builder = new VistaConDettagliBuilder<>(studenti);

    /**
     * Costruisce e restituisce la vista principale per la gestione degli studenti.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella degli studenti correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare studenti</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello iscrizioni.
     */
    public static VBox getView() {

        // Selezione tabellare, variabili visibili nella tabella
        LinkedHashMap<String, Function<Studente, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.codiceFiscale"), Studente::getCf);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.nome"), Studente::getNome);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.cognome"), Studente::getCognome);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.email"), Studente::getEmail);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.matricola"), Studente::getMatricola);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.corsoDiLaurea"), s -> s.getCorsoDiLaurea() != null ? Main.getCorsoDiLaureaManager().getNomeCorsoDiLauraDaID(s.getCorsoDiLaurea()) : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.cfu"), s -> s.getCfu() != null ? s.getCfu().toString() : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.mediaPonderata"), s -> s.getMediaPonderata() != null ? String.format("%.2f", s.getMediaPonderata()) : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("field.mediaAritmetica"), s -> s.getMediaAritmetica() != null ? String.format("%.2f", s.getMediaAritmetica()) : "");

        // Selezione dettagliata
        LinkedHashMap<String, Function<Studente, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.dataImmatricolazione"), s -> s.getDataImmatricolazione() != null ? s.getDataImmatricolazione().toString() : "");
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.dataDiNascita"), Studente::getDataNascita);

        // Selezione dettagliata che punta ai pulsanti
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esami"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.visualizzaEsami"));
        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        dettagli.put("Visualizza Iscrizioni", s -> "Visualizza Iscrizioni");

        // Link alla visione di tutti gli esami di uno studente
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esami"), studente -> mostraEsamiStudente(studente));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), studente -> PdfHelper.esportaEntita(studente, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.situazione") + " " + studente.getNome() + " " + studente.getCognome(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.studente") + " " + studente.getMatricola()));
        builder.setLinkAction("Visualizza Iscrizioni", studente -> mostraIscrizioniStudente(studente));


        // Builder creazione oggetto
        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.titolo"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Studente> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.finestraAggiungi.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.finestraAggiungi.header"),
                            campi -> {
                                try {

                                    String cf = DialogsParser.validaCampo(campi, Main.getParametrizzazioneHelper().getBundle().getString("field.codiceFiscale"));

                                    String nome = DialogsParser.validaCampo(campi,Main.getParametrizzazioneHelper().getBundle().getString("field.nome"));                                            // Nome
                                    String cognome = DialogsParser.validaCampo(campi,Main.getParametrizzazioneHelper().getBundle().getString("field.cognome"));                                      // Cognome

                                    LocalDate dataNascita = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataDiNascita"))).getValue();                     // Data di nascita
                                    LocalDate ingressoUniv = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.ingressoUniversita"))).getValue();                // Ingresso università

                                    String matricola = DialogsParser.validaCampo(campi,Main.getParametrizzazioneHelper().getBundle().getString("field.matricola"));                                  // Matricola

                                    // Corso di Laurea
                                    @SuppressWarnings("unchecked")
                                    TableView<CorsoDiLaurea> tableCDL = (TableView<CorsoDiLaurea>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.corsoDiLaurea"));
                                    tableCDL.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                                // La scelta di elementi è solo 1
                                    CorsoDiLaurea cdlSelezionato = tableCDL.getSelectionModel().getSelectedItem();
                                    if (cdlSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.error.cdl.null"));
                                    }
                                    String cdlId = cdlSelezionato.getId();                                                              // ID del corso selezionato

                                    LocalDate dataImmatricolazione = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataImmatricolazione"))).getValue();      // Data immatricolazione

                                    return new Studente(cf, nome, cognome, dataNascita, LocalDateUtil.toString(ingressoUniv),
                                            matricola, cdlId, dataImmatricolazione, null);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I numeri devono essere scritti correttamente!");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            studente -> {
                                Main.getStudenteManager().aggiungi(studente);                                                           // Aggiunta dello studente al manager + salvataggio sul file JSON (database)
                                ricaricaInterfacciaGraficaStudentiPannello2();                                                          // Ricarica dell'interfaccia grafica con il nuovo studente
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.success"), Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.success.message"));                       // Dialogo di conferma
                            }
                    );

                    // Aggiunta dei campi al dialogBuilder
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.codiceFiscale"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.nome"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.cognome"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataDiNascita"), new DatePicker());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.ingressoUniversita"), new DatePicker());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.matricola"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.corsoDiLaurea"), TabelleHelper.generaTabellaFkCorsiDiLaurea(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataImmatricolazione"), new DatePicker());
                    dialogBuilder.mostra();

                },
                studente -> {
                    mostraDialogModificaStudente(studente, builder);                                                                    // Modifica dei campi di uno studente selezionato nella tabella
                    Main.getStudenteManager().salvaSuFile(); //TODO: Questo lo fa in automatico, dal manager lo butta nel file però se vogliamo un control+s possiamo metterlo in un listener!
                    System.out.println("Modifica Studente: " + studente.getCf());
                },
                studente -> {
                    Main.getStudenteManager().rimuovi(studente);                                                                        // Rimozione studente dal manager + rimozione sul file JSON (database)
                    ricaricaInterfacciaGraficaStudentiPannello2();                                                                      // Ricarica dell'interfaccia grafica con la rimozione di uno studente
                }
        );
    }

    /**
     * Mostra in una finestra a scomparsa tutti gli esami tenuti da uno studente selezionato.
     * @param studente Studente selezionato.
     */
    private static void mostraEsamiStudente(Studente studente) {
        Stage stage = new Stage();
        TableView<Esame> table = new TableView<>();

        TableColumn<Esame, String> colInsegnamento = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("field.mostraEsami.iscrizione.id"));
        colInsegnamento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIscrizioneId()));

        TableColumn<Esame, String> colVoto = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("field.mostraEsami.voto"));
        colVoto.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getVoto())));

        TableColumn<Esame, String> colLode = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("field.mostraEsami.lode"));
        colLode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isLode() ? Main.getParametrizzazioneHelper().getBundle().getString("si") : Main.getParametrizzazioneHelper().getBundle().getString("no")));

        TableColumn<Esame, String> colRifiutato = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("field.mostraEsami.rifiutato"));
        colRifiutato.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isRifiutato() ? Main.getParametrizzazioneHelper().getBundle().getString("si") : Main.getParametrizzazioneHelper().getBundle().getString("no")));

        TableColumn<Esame, String> colVerbalizzato = new TableColumn<>(Main.getParametrizzazioneHelper().getBundle().getString("field.mostraEsami.verbalizzato"));
        colVerbalizzato.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isVerbalizzato() ? Main.getParametrizzazioneHelper().getBundle().getString("si") : Main.getParametrizzazioneHelper().getBundle().getString("no")));

        table.getColumns().addAll(colInsegnamento, colVoto, colLode, colRifiutato, colVerbalizzato);

        table.setItems(FXCollections.observableArrayList(
                studente.getEsami() != null ? studente.getEsami() : List.of()
        ));

        // Pulsante per esportazione PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(table,
                "Report Esami Studente: " + studente.getNome() + " " + studente.getCognome(),
                studente.getNome() + "_" + studente.getCognome() + "_esami"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("studentiPannello2.esamiPannello.titolo") + studente.getNome() + " " + studente.getCognome());
        stage.show();
    }

    private static void mostraIscrizioniStudente(Studente studente) {
        // Recupera il codice fiscale
        String cfStudente = studente.getCf();

        // Ottieni la lista delle iscrizioni
        List<Iscrizione> iscrizioni = Main.getIscrizioneManager().getIscrizioniDaStudente(cfStudente);

        // Creazione della finestra
        Stage stage = new Stage();
        TableView<Iscrizione> table = new TableView<>();

        // Colonna ID
        TableColumn<Iscrizione, String> colId = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        // Colonna Appello
        TableColumn<Iscrizione, String> colAppello = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.appello"));
        colAppello.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getRidAppello())));

        // Colonna Data Iscrizione
        TableColumn<Iscrizione, String> colData = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.dataIscrizione"));
        colData.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDataIscrizione())));

        // Colonna Ritirato
        TableColumn<Iscrizione, String> colRitirato = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.iscrizioni.ritirato"));
        colRitirato.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRitirato()
                        ? Main.getParametrizzazioneHelper().getBundle().getString("si")
                        : Main.getParametrizzazioneHelper().getBundle().getString("no")));

        // Aggiunge tutte le colonne
        table.getColumns().addAll(colId, colAppello, colData, colRitirato);

        // Popola la tabella
        table.setItems(FXCollections.observableArrayList(iscrizioni));

        // Pulsante di esportazione PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Report Iscrizioni Studente: " + studente.getNome() + " " + studente.getCognome(),
                studente.getNome() + "_" + studente.getCognome() + "_iscrizioni"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("studentiPannello2.iscrizioniPannello.titolo") + studente.getNome() + " " + studente.getCognome());
        stage.show();
    }




    /**
     * Crea una finestra di modifica per uno studente selezionato sfruttando la logica di creazione.
     * @param studente Lo studente a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista degli studenti
     */
    private static void mostraDialogModificaStudente(Studente studente, VistaConDettagliBuilder<Studente> builder) {
        DialogBuilder<Studente> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.editor.titolo"),
                Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.editor.header"),
                campi -> {

                    String cf = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.codiceFiscale"))).getText();
                    String nome = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.nome"))).getText();
                    String cognome = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.cognome"))).getText();
                    LocalDate dataNascita = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataDiNascita"))).getValue();
                    LocalDate ingressoUniv = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.ingressoUniversita"))).getValue();
                    String matricola = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.matricola"))).getText();

                    @SuppressWarnings("unchecked")
                    TableView<CorsoDiLaurea> tableCDL = (TableView<CorsoDiLaurea>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.corsoDiLaurea"));
                    CorsoDiLaurea cdlSelezionato = tableCDL.getSelectionModel().getSelectedItem();
                    if (cdlSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.error.cdl.null"));

                    LocalDate dataImmatricolazione = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("field.dataImmatricolazione"))).getValue();

                    // Aggiornamento dell'oggetto studente TODO: aggiorna metodo?
                    studente.setCf(cf);
                    studente.setNome(nome);
                    studente.setCognome(cognome);
                    studente.setDataNascita(LocalDateUtil.toString(dataNascita));
                    studente.setDataIngressoUniversita(LocalDateUtil.toString(ingressoUniv));
                    studente.setMatricola(matricola);
                    studente.setCorsoDiLaurea(cdlSelezionato.getId());
                    studente.setDataImmatricolazione(dataImmatricolazione);

                    return studente;
                },
                s -> {
                    ricaricaInterfacciaGraficaStudentiPannello2();                                                                      // Ricarica dell'interfaccia grafica a seguito della modifica
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.success"), Main.getParametrizzazioneHelper().getBundle().getString("studentiPannello2.builder.success.message"));
                }
        );

        // Pre-selezioni campi
        TableView<CorsoDiLaurea> tableCDL = TabelleHelper.generaTabellaFkCorsiDiLaurea(SelectionMode.SINGLE);
        tableCDL.getItems().stream()
                .filter(cdl -> cdl.getId().equals(studente.getCorsoDiLaurea()))
                .findFirst().ifPresent(cdl -> tableCDL.getSelectionModel().select(cdl));

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.codiceFiscale"), new TextField(studente.getCf()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.nome"), new TextField(studente.getNome()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.cognome"), new TextField(studente.getCognome()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataDiNascita"), new DatePicker(LocalDateUtil.fromString(studente.getDataNascita())));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.ingressoUniversita"), new DatePicker(LocalDateUtil.fromString(studente.getDataIngressoUniversita())));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.matricola"), new TextField(studente.getMatricola()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.corsoDiLaurea"), tableCDL);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("field.dataImmatricolazione"), new DatePicker(studente.getDataImmatricolazione()));

        dialogBuilder.mostra();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista degli studenti.
     *
     * @return il {@link VistaConDettagliBuilder} associato agli studenti.
     */
    public static VistaConDettagliBuilder<Studente> getBuilder() {
        return builder;
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

        Studente selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da modificare.");
            return;
        }
        mostraDialogModificaStudente(selezionato, builder);
        Main.getStudenteManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        Studente selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }
        Main.getStudenteManager().rimuovi(selezionato);
        ricaricaInterfacciaGraficaStudentiPannello2();
    }



}
