package it.univaq.unigest.gui.modelview.pannelli.docenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.model.Verbale;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaDocentiPannello2;

/**
 * Pannello grafico per la gestione dei docenti.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi docenti</li>
 *   <li>Modificare docenti esistenti</li>
 *   <li>Eliminare docenti selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class DocentiPannello2 {

    /**
     * Lista dei docenti attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getDocenteManager()}.
     */
    private static List<Docente> docenti = Main.getDocenteManager().getAll();

    /**
     * Builder grafico di {@link Docente}.
     */
    private static VistaConDettagliBuilder<Docente> builder = new VistaConDettagliBuilder<>(docenti);

    /**
     * Costruisce e restituisce la vista principale per la gestione dei docenti.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella dei docenti correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare docenti</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello docenti.
     */
    public static VBox getView() {

        LinkedHashMap<String, Function<Docente, String>> colonne = new LinkedHashMap<>();
        colonne.put("CF", Docente::getCf);  //ok
        colonne.put("Nome", Docente::getNome);  //ok
        colonne.put("Cognome", Docente::getCognome);       //ok
        colonne.put("Email", Docente::getEmail);        // autocalcolata - togliere
        colonne.put("Ruolo", d -> d.isRuolo() ? "Di ruolo" : "Esterno");
        colonne.put("Dipartimento", Docente::getDipartimento);  // ok
        colonne.put("Qualifica", Docente::getQualifica);    // ok

        LinkedHashMap<String, Function<Docente, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Data di Nascita", Docente::getDataNascita);
        dettagli.put("Ingresso Università", d -> d.getDataIngressoUniversitaDocente().toString());
        dettagli.put("Codice Docente", Docente::getCodiceDocente);

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.docente.descrizione") + " " + iscrizione.getCf(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.docente.descrizione") + " " + iscrizione.getCf()));

        dettagli.put("Visualizza Insegnamenti", s -> "Visualizza Insegnamenti");
        builder.setLinkAction("Visualizza Insegnamenti", iscrizione -> mostraInsegnamenti(iscrizione));

        dettagli.put("Visualizza Appelli", s -> "Visualizza Appelli");
        builder.setLinkAction("Visualizza Appelli", iscrizione -> mostraAppelliDocente(iscrizione));

        dettagli.put("Visualizza Verbali", s -> "Visualizza Verbali");
        builder.setLinkAction("Visualizza Verbali", iscrizione -> mostraVerbaliDocente(iscrizione));

        return builder.build(
                "Gestione Docenti",
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Docente> dialogBuilder = new DialogBuilder<>(
                            "Nuovo Docente",
                            "Inserisci i dati del docente",
                            campi -> {
                                try {
                                    String cf = DialogsParser.validaCampo(campi, "CF");

                                    String nome = DialogsParser.validaCampo(campi, "Nome");

                                    String cognome = DialogsParser.validaCampo(campi, "Nome");

                                    LocalDate dataNascita = ((DatePicker) campi.get("Data di Nascita")).getValue();
                                    String codiceDocente = DialogsParser.validaCampo(campi, "Codice Docente");

                                    boolean isRuolo = ((CheckBox) campi.get("Ruolo")).isSelected();
                                    LocalDate ingressoDocente = ((DatePicker) campi.get("Ingresso Univ. Docente")).getValue();
                                    String dipartimento = DialogsParser.validaCampo(campi, "Dipartimento");

                                    @SuppressWarnings("unchecked")
                                    ComboBox<String> comboQualifica = (ComboBox<String>) campi.get("Qualifica");
                                    String qualifica = comboQualifica.getValue();

                                    return new Docente(cf, nome, cognome, dataNascita, null,
                                            codiceDocente, isRuolo, ingressoDocente, dipartimento, qualifica);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            docente -> {
                                Main.getDocenteManager().aggiungi(docente);
                                ricaricaInterfacciaGraficaDocentiPannello2();
                                Dialogs.showInfo("Successo", "Docente aggiunto correttamente!");
                            }
                    );

                    dialogBuilder.aggiungiCampo("CF", new TextField());
                    dialogBuilder.aggiungiCampo("Nome", new TextField());
                    dialogBuilder.aggiungiCampo("Cognome", new TextField());
                    dialogBuilder.aggiungiCampo("Data di Nascita", new DatePicker());

                    dialogBuilder.aggiungiCampo("Codice Docente", new TextField());
                    dialogBuilder.aggiungiCampo("Ruolo", new CheckBox("Docente di ruolo"));
                    dialogBuilder.aggiungiCampo("Ingresso Univ. Docente", new DatePicker());
                    dialogBuilder.aggiungiCampo("Dipartimento", new TextField());

                    ComboBox<String> comboSemestre = new ComboBox<>();
                    comboSemestre.setItems(FXCollections.observableArrayList("Ordinario", "Associato", "Ricercatore", "Contratto"));
                    comboSemestre.setPrefWidth(150);

                    dialogBuilder.aggiungiCampo("Qualifica", comboSemestre);

                    dialogBuilder.mostra();
                },
                docente -> {
                    mostraDialogModificaDocente(docente, builder);
                    System.out.println("Modifica Docente: " + docente.getCf());
                },
                docente -> {
                    Main.getDocenteManager().rimuovi(docente);
                    ricaricaInterfacciaGraficaDocentiPannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un docente selezionato sfruttando la logica di creazione.
     * @param docente Il docente a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista dei docenti
     */
    private static void mostraDialogModificaDocente(Docente docente, VistaConDettagliBuilder<Docente> builder) {
        DialogBuilder<Docente> dialogBuilder = new DialogBuilder<>(
                "Modifica Docente",
                "Modifica i dati del docente",
                campi -> {
                    String cf = ((TextField) campi.get("CF")).getText();
                    String nome = ((TextField) campi.get("Nome")).getText();
                    String cognome = ((TextField) campi.get("Cognome")).getText();
                    LocalDate dataNascita = ((DatePicker) campi.get("Data di Nascita")).getValue();
                    String codiceDocente = ((TextField) campi.get("Codice Docente")).getText();
                    boolean isRuolo = ((CheckBox) campi.get("Ruolo")).isSelected();
                    LocalDate ingressoDocente = ((DatePicker) campi.get("Ingresso Univ. Docente")).getValue();
                    String dipartimento = ((TextField) campi.get("Dipartimento")).getText();

                    @SuppressWarnings("unchecked")
                    ComboBox<String> comboQualifica = (ComboBox<String>) campi.get("Qualifica");
                    String qualifica = comboQualifica.getValue();

                    // Aggiorna l'oggetto
                    docente.setCf(cf);
                    docente.setNome(nome);
                    docente.setCognome(cognome);
                    docente.setDataNascita(dataNascita.toString());
                    docente.setCodiceDocente(codiceDocente);
                    docente.setRuolo(isRuolo);
                    docente.setDataIngressoUniversitaDocente(ingressoDocente);
                    docente.setDipartimento(dipartimento);
                    docente.setQualifica(qualifica);

                    return docente;
                },
                d -> {
                    ricaricaInterfacciaGraficaDocentiPannello2();
                    Dialogs.showInfo("Successo", "Docente modificato con successo!");
                }
        );

        dialogBuilder.aggiungiCampo("CF", new TextField(docente.getCf()));
        dialogBuilder.aggiungiCampo("Nome", new TextField(docente.getNome()));
        dialogBuilder.aggiungiCampo("Cognome", new TextField(docente.getCognome()));
        dialogBuilder.aggiungiCampo("Data di Nascita", new DatePicker(LocalDate.parse(docente.getDataNascita())));
        dialogBuilder.aggiungiCampo("Codice Docente", new TextField(docente.getCodiceDocente()));

        CheckBox ruoloCheck = new CheckBox("Docente di ruolo");
        ruoloCheck.setSelected(docente.isRuolo());
        dialogBuilder.aggiungiCampo("Ruolo", ruoloCheck);

        dialogBuilder.aggiungiCampo("Ingresso Univ. Docente", new DatePicker(docente.getDataIngressoUniversitaDocente()));
        dialogBuilder.aggiungiCampo("Dipartimento", new TextField(docente.getDipartimento()));

        ComboBox<String> comboQualifica = new ComboBox<>();
        comboQualifica.setItems(FXCollections.observableArrayList("Ordinario", "Associato", "Ricercatore", "Contratto"));
        comboQualifica.setValue(docente.getQualifica());
        comboQualifica.setPrefWidth(150);
        dialogBuilder.aggiungiCampo("Qualifica", comboQualifica);

        dialogBuilder.mostra();
    }

    private static void mostraInsegnamenti(Docente docente) {
        // Recupera gli insegnamenti per il docente
        List<Insegnamento> insegnamenti = Main.getInsegnamentoManager()
                .getInsegnamentiDaDocente(docente.getCodiceDocente());

        Stage stage = new Stage();
        TableView<Insegnamento> table = new TableView<>();

        // Colonna ID Insegnamento
        TableColumn<Insegnamento, String> colId = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.id"));
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));

        // Colonna Nome Insegnamento
        TableColumn<Insegnamento, String> colNome = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.nome"));
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        // Colonna CFU
        TableColumn<Insegnamento, String> colCfu = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.cfu"));
        colCfu.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getCfu())));

        // Colonna Corso di Laurea
        TableColumn<Insegnamento, String> colCdL = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.cdl"));
        colCdL.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorsoDiLaureaId()));

        // Colonna Anno
        TableColumn<Insegnamento, String> colAnno = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.anno"));
        colAnno.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAnno())));

        // Colonna Semestre
        TableColumn<Insegnamento, String> colSemestre = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.semestre"));
        colSemestre.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getSemestre())));

        // Colonna Docenti (concatenati)
        TableColumn<Insegnamento, String> colDocenti = new TableColumn<>(
                Main.getParametrizzazioneHelper().getBundle().getString("field.insegnamenti.docenti"));
        colDocenti.setCellValueFactory(data -> {
            List<String> docentiList = data.getValue().getDocenti();
            String docentiStr = (docentiList != null && !docentiList.isEmpty())
                    ? String.join(", ", docentiList)
                    : Main.getParametrizzazioneHelper().getBundle().getString("nessun.docente");
            return new SimpleStringProperty(docentiStr);
        });

        table.getColumns().addAll(colId, colNome, colCfu, colCdL, colAnno, colSemestre, colDocenti);
        table.setItems(FXCollections.observableArrayList(insegnamenti));

        // Pulsante PDF
        Button exportBtn = new Button("Esporta in PDF");
        exportBtn.setOnAction(e -> PdfHelper.esportaTabellaInPdf(
                table,
                "Insegnamenti del docente: " + docente.getNome() + " " + docente.getCognome(),
                docente.getNome() + "_" + docente.getCognome() + "_insegnamenti"));

        VBox layout = new VBox(10, table, exportBtn);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 400);
        stage.setScene(scene);
        stage.setTitle(Main.getParametrizzazioneHelper().getBundle()
                .getString("docentiPannello2.insegnamentiPannello.titolo") + docente.getNome() + " " + docente.getCognome());
        stage.show();
    }

    private static void mostraAppelliDocente(Docente docente) {
        // 1) Ottieni insegnamenti del docente
        List<Insegnamento> insegnamenti = Main.getInsegnamentoManager()
                .getInsegnamentiDaDocente(docente.getCodiceDocente());

        // 2) Recupera tutti gli appelli degli insegnamenti
        List<Appello> appelli = new ArrayList<>();
        for (Insegnamento ins : insegnamenti) {
            appelli.addAll(Main.getAppelloManager().getAppelliDaInsegnamento(ins.getId()));
        }

        // 3) Ordina per data
        appelli.sort(Comparator.comparing(Appello::getData));

        // 4) Dividi in base alla data
        LocalDate oggi = LocalDate.now();
        List<Appello> appelliOggi = appelli.stream()
                .filter(a -> a.getData().isEqual(oggi))
                .collect(Collectors.toList());

        List<Appello> passati = appelli.stream()
                .filter(a -> a.getData().isBefore(oggi))
                .collect(Collectors.toList());

        List<Appello> futuri = appelli.stream()
                .filter(a -> a.getData().isAfter(oggi))
                .collect(Collectors.toList());

        // --- Creazione della finestra ---
        Stage stage = new Stage();
        TabPane tabPane = new TabPane();

        // Crea le tabelle per ogni tab
        Tab tabOggi = new Tab("Oggi", creaTabellaAppelli(appelliOggi));
        Tab tabDisponibili = new Tab("Disponibili", creaTabellaAppelli(
                Stream.concat(appelliOggi.stream(), futuri.stream()).collect(Collectors.toList())));
        Tab tabPassati = new Tab("Passati", creaTabellaAppelli(passati));

        tabOggi.setClosable(false);
        tabDisponibili.setClosable(false);
        tabPassati.setClosable(false);

        tabPane.getTabs().addAll(tabOggi, tabDisponibili, tabPassati);

        VBox layout = new VBox(10, tabPane);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Appelli Docente: " + docente.getNome() + " " + docente.getCognome());
        stage.show();
    }

    /**
     * Crea una TableView per una lista di appelli
     */
    private static TableView<Appello> creaTabellaAppelli(List<Appello> appelli) {
        TableView<Appello> table = new TableView<>();

        TableColumn<Appello, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Appello, String> colInsegnamento = new TableColumn<>("Insegnamento");
        colInsegnamento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRidInsegnamento()));

        TableColumn<Appello, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));

        TableColumn<Appello, String> colAula = new TableColumn<>("Aula");
        colAula.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRidAula()));

        table.getColumns().addAll(colId, colInsegnamento, colData, colAula);
        table.setItems(FXCollections.observableArrayList(appelli));

        return table;
    }

    private static void mostraVerbaliDocente(Docente docente) {
        // 1) Ottieni gli insegnamenti del docente
        List<Insegnamento> insegnamenti = Main.getInsegnamentoManager()
                .getInsegnamentiDaDocente(docente.getCodiceDocente());

        // 2) Recupera tutti gli appelli degli insegnamenti
        List<Appello> appelli = new ArrayList<>();
        for (Insegnamento ins : insegnamenti) {
            appelli.addAll(Main.getAppelloManager().getAppelliDaInsegnamento(ins.getId()));
        }

        // 3) Recupera i verbali relativi a questi appelli
        List<Verbale> verbali = Main.getVerbaleManager().getAll().stream()
                .filter(v -> appelli.stream().anyMatch(a -> String.valueOf(a.getId()).equals(v.getAppelloId())))
                .collect(Collectors.toList());

        // 4) Separazione aperti/chiusi
        List<Verbale> aperti = verbali.stream().filter(v -> !v.getChiuso()).collect(Collectors.toList());
        List<Verbale> chiusi = verbali.stream().filter(Verbale::getChiuso).collect(Collectors.toList());

        // --- UI ---
        Stage stage = new Stage();
        TabPane tabPane = new TabPane();

        Tab tabAperti = new Tab("Verbali Aperti", creaTabellaVerbali(aperti));
        Tab tabChiusi = new Tab("Verbali Chiusi", creaTabellaVerbali(chiusi));

        tabAperti.setClosable(false);
        tabChiusi.setClosable(false);

        tabPane.getTabs().addAll(tabAperti, tabChiusi);

        VBox layout = new VBox(10, tabPane);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 400);
        stage.setScene(scene);
        stage.setTitle("Verbali del docente: " + docente.getNome() + " " + docente.getCognome());
        stage.show();
    }

    private static TableView<Verbale> creaTabellaVerbali(List<Verbale> verbali) {
        TableView<Verbale> table = new TableView<>();

        TableColumn<Verbale, String> colId = new TableColumn<>("ID Verbale");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Verbale, String> colAppello = new TableColumn<>("Appello");
        colAppello.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAppelloId()));

        TableColumn<Verbale, String> colData = new TableColumn<>("Data Chiusura");
        colData.setCellValueFactory(data -> {
            LocalDate d = data.getValue().getDataChiusura();
            return new SimpleStringProperty(d != null ? d.toString() : "-");
        });

        TableColumn<Verbale, String> colChiuso = new TableColumn<>("Chiuso");
        colChiuso.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChiuso() ? "Sì" : "No"));

        TableColumn<Verbale, String> colFirmato = new TableColumn<>("Firmato");
        colFirmato.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirmato() ? "Sì" : "No"));

        TableColumn<Verbale, String> colNote = new TableColumn<>("Note");
        colNote.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNote()));

        table.getColumns().addAll(colId, colAppello, colData, colChiuso, colFirmato, colNote);
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
        Docente selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaDocente(selezionato, builder);

        //
        Main.getDocenteManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Docente selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getDocenteManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaDocentiPannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista dei docenti.
     *
     * @return il {@link VistaConDettagliBuilder} associato ai docenti.
     */
    public static VistaConDettagliBuilder<Docente> getBuilder() {
        return builder;
    }
}
