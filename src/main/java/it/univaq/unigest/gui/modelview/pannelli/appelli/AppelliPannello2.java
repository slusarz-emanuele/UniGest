package it.univaq.unigest.gui.modelview.pannelli.appelli;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.AppelloService;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class AppelliPannello2 implements CrudPanel {

    // Etichette
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
    private final Supplier<List<Aula>>         loadAule;
    private final Supplier<List<Docente>>      loadDocenti;

    public AppelliPannello2(AppelloService appelloService,
                            Supplier<List<Insegnamento>> loadInsegnamenti,
                            Supplier<List<Aula>> loadAule,
                            Supplier<List<Docente>> loadDocenti) {
        this.appelloService   = appelloService;
        this.loadInsegnamenti = loadInsegnamenti;
        this.loadAule         = loadAule;
        this.loadDocenti      = loadDocenti;
        this.builder          = new VistaConDettagliBuilder<>(appelloService.findAll());
    }

    // Blocchiamo il costruttore di default
    private AppelliPannello2() {
        this.appelloService = null;
        this.builder = null;
        this.loadInsegnamenti = null;
        this.loadAule = null;
        this.loadDocenti = null;
    }

    // ===== CrudPanel API =====
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Appelli",
                colonne(),
                dettagli(),
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un appello."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un appello."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(appelloService.findAll());
    }

    public VistaConDettagliBuilder<Appello> getBuilder() { return builder; }

    // ===== Colonne / Dettagli =====
    private LinkedHashMap<String, Function<Appello, String>> colonne() {
        LinkedHashMap<String, Function<Appello, String>> map = new LinkedHashMap<>();
        map.put(L_ID,           Appello::getId);                    // mostra String id
        map.put(L_INSEGNAMENTO, Appello::getRidInsegnamento);       // qui mostriamo l'id dell'insegnamento
        map.put(L_DATA,         a -> a.getData() != null ? a.getData().toString() : "");
        map.put(L_ORA,          a -> a.getOra()  != null ? a.getOra().toString()  : "");
        map.put(L_AULA,         a -> a.getRidAula()    != null ? a.getRidAula()    : "");
        map.put(L_DOCENTE,      a -> a.getRidDocente() != null ? a.getRidDocente() : "");
        map.put(L_VERBALE,      a -> a.getRidVerbale() != null ? a.getRidVerbale() : "-");
        return map;
    }

    private LinkedHashMap<String, Function<Appello, String>> dettagli() {
        return new LinkedHashMap<>(colonne());
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Appello",
                "Inserisci i dati dell'appello",
                null,
                aCreato -> appelloService.create(aCreato), // id gestito dal repository
                "Successo",
                "Appello aggiunto correttamente!"
        );
    }

    private void mostraDialogModifica(Appello appello) {
        mostraDialogoCrud(
                "Modifica Appello",
                "Modifica i dati dell'appello",
                appello,
                aAgg -> appelloService.update(aAgg),
                "Successo",
                "Appello modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Appello iniziale,
                                   Function<Appello, Appello> persister,
                                   String successTitle,
                                   String successMessage) {
        DialogBuilder<Appello> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Appello target = estraiAppelloDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                a -> { refresh(); Dialogs.showInfo(successTitle, successMessage); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // ===== Configurazione campi (ordine richiesto: Insegnamento -> Data -> Ora -> Aula -> Docente) =====
    private void configuraCampi(DialogBuilder<Appello> dialog, Appello iniziale) {
        // Insegnamento
        TableView<Insegnamento> tabIns = TableMiniFactory.creaTabella(
                loadInsegnamenti,
                SelectionMode.SINGLE,
                240,
                new LinkedHashMap<>() {{
                    put("ID",   Insegnamento::getId);
                    put("Nome", Insegnamento::getNome);
                }}
        );
        if (iniziale != null && iniziale.getRidInsegnamento() != null) {
            tabIns.getItems().stream()
                    .filter(i -> i.getId().equals(iniziale.getRidInsegnamento()))
                    .findFirst().ifPresent(i -> tabIns.getSelectionModel().select(i));
        }

        // Data
        DatePicker dpData = new DatePicker(iniziale != null ? iniziale.getData() : null);

        // Ora (TextField in formato HH:mm)
        TextField tfOra = new TextField(iniziale != null && iniziale.getOra() != null ? iniziale.getOra().toString() : "");
        tfOra.setPromptText("HH:mm");

        // Aula
        TableView<Aula> tabAule = TableMiniFactory.creaTabella(
                loadAule,
                SelectionMode.SINGLE,
                220,
                new LinkedHashMap<>() {{
                    put("ID",       Aula::getId);
                    put("Capienza", a -> Integer.toString(a.getCapienza()));
                }}
        );
        if (iniziale != null && iniziale.getRidAula() != null) {
            tabAule.getItems().stream()
                    .filter(a -> a.getId().equals(iniziale.getRidAula()))
                    .findFirst().ifPresent(a -> tabAule.getSelectionModel().select(a));
        }

        // Docente
        TableView<Docente> tabDoc = TableMiniFactory.creaTabella(
                loadDocenti,
                SelectionMode.SINGLE,
                240,
                new LinkedHashMap<>() {{
                    put("CF",      Docente::getCf);
                    put("Nome",    Docente::getNome);
                    put("Cognome", Docente::getCognome);
                }}
        );
        if (iniziale != null && iniziale.getRidDocente() != null) {
            tabDoc.getItems().stream()
                    .filter(d -> d.getCf().equals(iniziale.getRidDocente()))
                    .findFirst().ifPresent(d -> tabDoc.getSelectionModel().select(d));
        }

        // Aggiunta campi nel giusto ordine
        dialog.aggiungiCampo(L_INSEGNAMENTO, tabIns);
        dialog.aggiungiCampo(L_DATA,         dpData);
        dialog.aggiungiCampo(L_ORA,          tfOra);
        dialog.aggiungiCampo(L_AULA,         tabAule);
        dialog.aggiungiCampo(L_DOCENTE,      tabDoc);
    }

    // ===== Lettura/validazione campi =====
    private Appello estraiAppelloDaCampi(Map<String, Control> campi, Appello target) {
        // Insegnamento
        @SuppressWarnings("unchecked")
        TableView<Insegnamento> tabIns = (TableView<Insegnamento>) campi.get(L_INSEGNAMENTO);
        Insegnamento insSel = tabIns.getSelectionModel().getSelectedItem();
        if (insSel == null) throw new IllegalArgumentException("Seleziona un insegnamento.");

        // Data
        LocalDate data = ((DatePicker) campi.get(L_DATA)).getValue();
        if (data == null) throw new IllegalArgumentException("Seleziona una data.");

        // Ora
        String oraStr = DialogsParser.validaCampo(campi, L_ORA); // controlla non vuoto
        LocalTime ora;
        try {
            ora = LocalTime.parse(oraStr);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Ora non valida. Usa il formato HH:mm (es. 09:30).");
        }

        // Aula
        @SuppressWarnings("unchecked")
        TableView<Aula> tabAule = (TableView<Aula>) campi.get(L_AULA);
        Aula aulaSel = tabAule.getSelectionModel().getSelectedItem();
        if (aulaSel == null) throw new IllegalArgumentException("Seleziona un'aula.");

        // Docente
        @SuppressWarnings("unchecked")
        TableView<Docente> tabDoc = (TableView<Docente>) campi.get(L_DOCENTE);
        Docente docSel = tabDoc.getSelectionModel().getSelectedItem();
        if (docSel == null) throw new IllegalArgumentException("Seleziona un docente.");

        // Costruzione/aggiornamento
        if (target == null) {
            // NB: per l’auto-increment il repository deve riconoscere l’ID "nuovo".
            // Se il tuo modello non consente id null, passa un placeholder e lascia
            // al repository/servizio il compito di assegnare quello definitivo.
            return new Appello(
                    0,
                    insSel.getId(),
                    data,
                    ora,
                    aulaSel.getId(),
                    docSel.getCf(),
                    null                        // ridVerbale inizialmente assente
            );
        } else {
            target.setRidInsegnamento(insSel.getId());
            target.setData(data);
            target.setOra(ora);
            target.setRidAula(aulaSel.getId());
            target.setRidDocente(docSel.getCf());
            // non tocchiamo ridVerbale qui
            return target;
        }
    }

    private void elimina(Appello a) {
        appelloService.deleteById(a.getId());
        refresh();
    }
}