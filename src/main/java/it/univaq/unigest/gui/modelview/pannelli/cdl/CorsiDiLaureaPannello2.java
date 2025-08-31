package it.univaq.unigest.gui.modelview.pannelli.cdl;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.PdfHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaCorsiDiLaureaPannello2;

/**
 * Pannello grafico per la gestione dei corsi di laurea.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi corsi di laurea</li>
 *   <li>Modificare corsi di laurea esistenti</li>
 *   <li>Eliminare corsi di laurea selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class CorsiDiLaureaPannello2 {

    /**
     * Lista dei corsi di laurea attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getCorsoDiLaureaManager()}.
     */
    private static List<CorsoDiLaurea> corsiDiLaurea = Main.getCorsoDiLaureaManager().getAll();

    /**
     * Builder grafico di {@link CorsoDiLaurea}.
     */
    private static VistaConDettagliBuilder<CorsoDiLaurea> builder = new VistaConDettagliBuilder<>(corsiDiLaurea);

    /**
     * Costruisce e restituisce la vista principale per la gestione dei corsi di laurea.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella dei corsi di laurea correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare corsi di laurea</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello corsi di laurea.
     */
    public static VBox getView() {

        LinkedHashMap<String, Function<CorsoDiLaurea, String>> colonne = new LinkedHashMap<>();
        colonne.put("ID", a -> String.valueOf(a.getId()));
        colonne.put("Nome", CorsoDiLaurea::getNome);
        colonne.put("Cfu Totali", a -> String.valueOf(a.getCfuTotali()));
        colonne.put("Dipartimento", CorsoDiLaurea::getDipartimento);
        colonne.put("Coordinatore ID", CorsoDiLaurea::getCoordinatoreId);

        LinkedHashMap<String, Function<CorsoDiLaurea, String>> dettagli = new LinkedHashMap<>(colonne);

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.cdl.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.cdl.descrizione") + " " + iscrizione.getId()));

        dettagli.put("Visualizza Insegnamenti", s -> "Visualizza Insegnamenti");
        builder.setLinkAction("Visualizza Insegnamenti", iscrizione -> mostraInsegnamentiPerCorso(iscrizione));

        dettagli.put("Visualizza Studenti", s -> "Visualizza Studenti");
        builder.setLinkAction("Visualizza Studenti", iscrizione -> mostraStudentiPerCorso(iscrizione));

        return builder.build(
                "Gestione Corsi Di Laurea",
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<CorsoDiLaurea> dialogBuilder = new DialogBuilder<>(
                            "Nuovo Corso di Laurea",
                            "Inserisci i dati del nuovo corso",
                            campi -> {
                                try {
                                    String nome = DialogsParser.validaCampo(campi,"Nome");

                                    String cfuText = DialogsParser.validaCampo(campi,"CFU Totali");
                                    int cfu = Integer.parseInt(cfuText);

                                    String dipartimento = DialogsParser.validaCampo(campi,"Dipartimento");
                                    String coordinatoreId = DialogsParser.validaCampo(campi,"Coordinatore ID");

                                    return new CorsoDiLaurea(
                                            String.valueOf(Main.getCorsoDiLaureaManager().assegnaIndiceCorrente()),
                                            nome, cfu, dipartimento, coordinatoreId
                                    );

                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I CFU devono essere un numero valido.");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }

                            },
                            corso -> {
                                Main.getCorsoDiLaureaManager().aggiungi(corso);
                                ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
                                Dialogs.showInfo("Successo", "Corso di Laurea aggiunto con successo!");
                            }
                    );

                    dialogBuilder.aggiungiCampo("Nome", new TextField());
                    dialogBuilder.aggiungiCampo("CFU Totali", new TextField());
                    dialogBuilder.aggiungiCampo("Dipartimento", new TextField());
                    dialogBuilder.aggiungiCampo("Coordinatore ID", new TextField());

                    dialogBuilder.mostra();
                },
                corso -> {
                    mostraDialogModificaCorso(corso, builder);
                    System.out.println("Modifica Corso di Laurea: " + corso.getId());
                },
                corso -> {
                    Main.getCorsoDiLaureaManager().rimuovi(corso);
                    ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un corso di laurea selezionato sfruttando la logica di creazione.
     * @param corso Il corso a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista dei corsi di laurea.
     */
    private static void mostraDialogModificaCorso(CorsoDiLaurea corso, VistaConDettagliBuilder<CorsoDiLaurea> builder) {
        DialogBuilder<CorsoDiLaurea> dialogBuilder = new DialogBuilder<>(
                "Modifica Corso di Laurea",
                "Modifica i dati del corso di laurea",
                campi -> {
                    String nome = ((TextField) campi.get("Nome")).getText();
                    int cfuTotali = Integer.parseInt(((TextField) campi.get("CFU Totali")).getText());
                    String dipartimento = ((TextField) campi.get("Dipartimento")).getText();
                    String coordinatoreId = ((TextField) campi.get("Coordinatore ID")).getText();

                    // Aggiorna l'oggetto
                    corso.setNome(nome);
                    corso.setCfuTotali(cfuTotali);
                    corso.setDipartimento(dipartimento);
                    corso.setCoordinatoreId(coordinatoreId);

                    return corso;
                },
                c -> {
                    ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
                    Dialogs.showInfo("Successo", "Corso di Laurea modificato con successo!");
                }
        );

        dialogBuilder.aggiungiCampo("Nome", new TextField(corso.getNome()));
        dialogBuilder.aggiungiCampo("CFU Totali", new TextField(String.valueOf(corso.getCfuTotali())));
        dialogBuilder.aggiungiCampo("Dipartimento", new TextField(corso.getDipartimento()));
        dialogBuilder.aggiungiCampo("Coordinatore ID", new TextField(corso.getCoordinatoreId()));

        dialogBuilder.mostra();
    }

    private static void mostraInsegnamentiPerCorso(CorsoDiLaurea corsoDiLaurea) {
        // 1) Recupera insegnamenti dal manager
        List<Insegnamento> insegnamenti = Main.getInsegnamentoManager()
                .getInsegnamentoDaCorsoDiLaurea(corsoDiLaurea.getId());

        // 2) Creazione stage e tabella
        Stage stage = new Stage();
        TableView<Insegnamento> table = new TableView<>();

        TableColumn<Insegnamento, String> colId = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));

        TableColumn<Insegnamento, String> colNome = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.nome"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Insegnamento, String> colCfu = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.cfu"));
        colCfu.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCfu())));

        TableColumn<Insegnamento, String> colAnno = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.anno"));
        colAnno.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAnno())));

        TableColumn<Insegnamento, String> colSemestre = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.semestre"));
        colSemestre.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSemestre())));

        TableColumn<Insegnamento, String> colDocenti = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.docenti"));
        colDocenti.setCellValueFactory(data -> {
            List<String> docentiList = data.getValue().getDocenti();
            String docentiStr = (docentiList != null && !docentiList.isEmpty())
                    ? String.join(", ", docentiList)
                    : Main.getParametrizzazioneHelper().getBundle().getString("nessun.docente");
            return new SimpleStringProperty(docentiStr);
        });

        table.getColumns().addAll(colId, colNome, colCfu, colAnno, colSemestre, colDocenti);
        table.setItems(FXCollections.observableArrayList(insegnamenti));

        // Pulsante per esportazione PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Insegnamenti del Corso di Laurea: " + corsoDiLaurea.getNome(),
                corsoDiLaurea.getNome() + "_insegnamenti"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("corsiDiLaureaPannello2.insegnamentiPannello.titolo") + corsoDiLaurea.getNome());
        stage.show();
    }

    private static void mostraStudentiPerCorso(CorsoDiLaurea corsoDiLaurea) {
        // 1) Recupera gli studenti dal manager
        List<Studente> studenti = Main.getStudenteManager()
                .getStudentiDaUnCDL(corsoDiLaurea.getId());

        // 2) Creazione stage e tabella
        Stage stage = new Stage();
        TableView<Studente> table = new TableView<>();

        TableColumn<Studente, String> colMatricola = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.studenti.matricola"));
        colMatricola.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricola()));

        TableColumn<Studente, String> colNome = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.studenti.nome"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Studente, String> colCognome = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.studenti.cognome"));
        colCognome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCognome()));

        TableColumn<Studente, String> colEmail = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.studenti.email"));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Studente, String> colAnnoIscrizione = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.studenti.annoIscrizione"));
        colAnnoIscrizione.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getDataImmatricolazione())));

        table.getColumns().addAll(colMatricola, colNome, colCognome, colEmail, colAnnoIscrizione);
        table.setItems(FXCollections.observableArrayList(studenti));

        // Pulsante per esportazione PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Studenti del Corso di Laurea: " + corsoDiLaurea.getNome(),
                corsoDiLaurea.getNome() + "_studenti"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("corsiDiLaureaPannello2.studentiPannello.titolo") + corsoDiLaurea.getNome());
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
        CorsoDiLaurea selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaCorso(selezionato, builder);

        //
        Main.getCorsoDiLaureaManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        CorsoDiLaurea selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getCorsoDiLaureaManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
    }


    /**
     * Restituisce il builder grafico utilizzato per costruire la vista dei corsi di laurea.
     *
     * @return il {@link VistaConDettagliBuilder} associato ai corsi di laurea.
     */
    public static VistaConDettagliBuilder<CorsoDiLaurea> getBuilder() {
        return builder;
    }
}