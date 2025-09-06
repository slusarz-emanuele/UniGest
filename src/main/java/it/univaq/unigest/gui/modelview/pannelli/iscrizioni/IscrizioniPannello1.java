package it.univaq.unigest.gui.modelview.pannelli.iscrizioni;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.query.DomainQueryService;
import it.univaq.unigest.util.LocalDateUtil;
import it.univaq.unigest.util.loader.DomainRefresher;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class IscrizioniPannello1 implements CrudPanel {

    // Etichette
    private static final String L_ID              = "ID Iscrizione";
    private static final String L_STUDENTE        = "Studente (CF)";
    private static final String L_APPELLO         = "Appello";
    private static final String L_DATA_ISCRIZIONE = "Data iscrizione";
    private static final String L_RITIRATO        = "Ritirato";

    // Dipendenze
    private final IscrizioneService iscrizioneService;
    private final VistaConDettagliBuilder<Iscrizione> builder;
    private final DomainQueryService domainQueryService;

    // Loader esterni
    private final Supplier<List<Studente>> loadStudenti;
    private final Supplier<List<Appello>>  loadAppelli;

    public IscrizioniPannello1(IscrizioneService iscrizioneService,
                               Supplier<List<Studente>> loadStudenti,
                               Supplier<List<Appello>> loadAppelli,
                               DomainQueryService domainQueryService) {
        this.iscrizioneService = iscrizioneService;
        this.loadStudenti = loadStudenti;
        this.loadAppelli  = loadAppelli;
        this.domainQueryService = domainQueryService;
        this.builder = new VistaConDettagliBuilder<>(iscrizioneService.findAll());
    }

    // Blocchiamo il costruttore di default
    private IscrizioniPannello1() {
        this.iscrizioneService = null;
        this.loadStudenti = null;
        this.loadAppelli  = null;
        this.builder = null;
        this.domainQueryService = null;
    }

    // ===== CrudPanel API =====
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Iscrizioni",
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
        builder.refresh(iscrizioneService.findAll());
    }

    public VistaConDettagliBuilder<Iscrizione> getBuilder() { return builder; }

    // ===== Colonne / Dettagli =====
    private LinkedHashMap<String, Function<Iscrizione, String>> colonne() {
        LinkedHashMap<String, Function<Iscrizione, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Iscrizione::getId);
        map.put(L_STUDENTE, i -> studenteLabelByCf(i.getRidStudenteCf()));
        map.put(L_APPELLO,  i -> String.valueOf(i.getRidAppello()));
        map.put(L_DATA_ISCRIZIONE, i -> i.getDataIscrizione() != null ? i.getDataIscrizione().toString() : "");
        map.put(L_RITIRATO, i -> i.getRitirato() ? "Sì" : "No");
        return map;
    }

    private LinkedHashMap<String, Function<Iscrizione, String>> dettagli() {
        LinkedHashMap<String, Function<Iscrizione, String>> map = new LinkedHashMap<>(colonne());

        return map;
    }

    private String studenteLabelByCf(String cf) {
        if (cf == null || cf.isBlank()) return "";
        return loadStudenti.get().stream()
                .filter(s -> cf.equals(s.getCf()))
                .findFirst()
                .map(s -> {
                    String nome = s.getNome() != null ? s.getNome() : "";
                    String cognome = s.getCognome() != null ? s.getCognome() : "";
                    return (nome + " " + cognome + " (" + cf + ")").trim();
                })
                .orElse(cf); // fallback
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuova Iscrizione",
                "Inserisci i dati dell'iscrizione",
                null,
                iCreato -> iscrizioneService.create(iCreato),
                "Successo",
                "Iscrizione aggiunta correttamente!"
        );
    }

    private void mostraDialogModifica(Iscrizione iniziale) {
        mostraDialogoCrud(
                "Modifica Iscrizione",
                "Modifica i dati dell'iscrizione",
                iniziale,
                iAgg -> iscrizioneService.update(iAgg),
                "Successo",
                "Iscrizione modificata correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Iscrizione iniziale,
                                   Function<Iscrizione, Iscrizione> persister,
                                   String successTitle,
                                   String successMessage) {
        DialogBuilder<Iscrizione> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Iscrizione target = estraiIscrizioneDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                v -> {
                    DomainRefresher.onIscrizioneChanged();
                    refresh();
                    Dialogs.showInfo(successTitle, successMessage); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // ===== Configurazione campi (ordine: studente → appello → data → ritirato) =====
    private void configuraCampi(DialogBuilder<Iscrizione> dialog, Iscrizione iniziale) {
        // Studente (single)
        TableView<Studente> tabStudenti = TableMiniFactory.creaTabella(
                loadStudenti,
                SelectionMode.SINGLE,
                240,
                new LinkedHashMap<>() {{
                    put("CF",      Studente::getCf);
                    put("Nome",    Studente::getNome);
                    put("Cognome", Studente::getCognome);
                }}
        );
        if (iniziale != null && iniziale.getRidStudenteCf() != null) {
            tabStudenti.getItems().stream()
                    .filter(s -> iniziale.getRidStudenteCf().equals(s.getCf()))
                    .findFirst().ifPresent(s -> tabStudenti.getSelectionModel().select(s));
        }

        // Appello (single)
        TableView<Appello> tabAppelli = TableMiniFactory.creaTabella(
                loadAppelli,
                SelectionMode.SINGLE,
                220,
                new LinkedHashMap<>() {{
                    put("Data",  a -> LocalDateUtil.toString(a.getData()));
                    put("Docente", Appello::getRidDocente);
                }}
        );
        if (iniziale != null) {
            int rid = iniziale.getRidAppello();
            tabAppelli.getItems().stream()
                    .filter(a -> {
                        try { return Integer.parseInt(a.getId()) == rid; }
                        catch (Exception e) { return false; }
                    })
                    .findFirst().ifPresent(a -> tabAppelli.getSelectionModel().select(a));
        }

        // Data iscrizione
        DatePicker dpData = new DatePicker(iniziale != null ? iniziale.getDataIscrizione() : null);

        // Ritirato
        CheckBox cbRitirato = new CheckBox(L_RITIRATO);
        if (iniziale != null) cbRitirato.setSelected(iniziale.getRitirato());

        // Aggiunta secondo ordine richiesto
        dialog.aggiungiCampo(L_STUDENTE, tabStudenti);
        dialog.aggiungiCampo(L_APPELLO,  tabAppelli);
        dialog.aggiungiCampo(L_DATA_ISCRIZIONE, dpData);
        dialog.aggiungiCampo(L_RITIRATO, cbRitirato);
    }

    // ===== Lettura/validazione =====
    private Iscrizione estraiIscrizioneDaCampi(Map<String, Control> campi, Iscrizione target) {
        // Studente
        @SuppressWarnings("unchecked")
        TableView<Studente> tabStudenti = (TableView<Studente>) campi.get(L_STUDENTE);
        if (tabStudenti == null) throw new IllegalStateException("Campo 'Studente' non trovato.");
        Studente stud = tabStudenti.getSelectionModel().getSelectedItem();
        if (stud == null) throw new IllegalArgumentException("Seleziona uno studente.");
        String studCf = stud.getCf();

        // Appello
        @SuppressWarnings("unchecked")
        TableView<Appello> tabAppelli = (TableView<Appello>) campi.get(L_APPELLO);
        if (tabAppelli == null) throw new IllegalStateException("Campo 'Appello' non trovato.");
        Appello app = tabAppelli.getSelectionModel().getSelectedItem();
        if (app == null) throw new IllegalArgumentException("Seleziona un appello.");
        int appelloId;
        try { appelloId = Integer.parseInt(app.getId()); }
        catch (Exception e) { throw new IllegalArgumentException("ID appello non valido."); }

        // Data iscrizione
        LocalDate data = ((DatePicker) campi.get(L_DATA_ISCRIZIONE)).getValue();
        if (data == null) throw new IllegalArgumentException("Seleziona la data di iscrizione.");

        // Ritirato
        boolean ritirato = ((CheckBox) campi.get(L_RITIRATO)).isSelected();

        if (target == null) {
            // id null → auto-increment dal repository
            return new Iscrizione(null, studCf, appelloId, data, ritirato);
        } else {
            target.setRidStudenteCf(studCf);
            target.setRidAppello(appelloId);
            target.setDataIscrizione(data);
            target.setRitirato(ritirato);
            return target;
        }
    }

    private void elimina(Iscrizione i) {
        iscrizioneService.deleteById(i.getId());
        DomainRefresher.onIscrizioneChanged();
        refresh();
    }
}
