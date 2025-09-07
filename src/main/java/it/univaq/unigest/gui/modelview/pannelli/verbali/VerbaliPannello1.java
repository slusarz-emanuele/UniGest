package it.univaq.unigest.gui.modelview.pannelli.verbali;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.service.VerbaleService;
import it.univaq.unigest.service.query.DomainQueryService;
import it.univaq.unigest.util.LocalDateUtil;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Pannello di gestione dei {@link Verbale}.
 * <p>
 * Responsabilità principali:
 * <ul>
 *   <li>Visualizzare i verbali in una tabella con {@link VistaConDettagliBuilder};</li>
 *   <li>Gestire dialog di aggiunta e modifica dei verbali con {@link DialogBuilder};</li>
 *   <li>Consentire la selezione degli {@link Appello} tramite {@link TableView} nei dialog;</li>
 *   <li>Garantire l'unicità del verbale per ciascun appello;</li>
 *   <li>Gestire la cancellazione dei verbali;</li>
 *   <li>Mostrare informazioni aggiuntive come numero di esami, note, stato di chiusura e firma.</li>
 * </ul>
 *
 * <h3>Dipendenze iniettate</h3>
 * <ul>
 *   <li>{@link VerbaleService}: CRUD sui verbali;</li>
 *   <li>{@link DomainQueryService}: interrogazioni per verificare eventuali verbali già esistenti per un appello;</li>
 *   <li>{@link Supplier}&lt;List&lt;Appello&gt;&gt;: carica gli appelli disponibili per la selezione;</li>
 *   <li>{@link VistaConDettagliBuilder}: gestione della tabella e dei dettagli dei verbali.</li>
 * </ul>
 *
 * <h3>Note di implementazione</h3>
 * <ul>
 *   <li>I dialog di aggiunta/modifica utilizzano {@link TableView} per la selezione dell'appello, {@link DatePicker} per le date, {@link CheckBox} per gli stati booleani e {@link TextField} per note;</li>
 *   <li>Viene garantita l'unicità dei verbali per ciascun appello tramite il metodo {@link #validateAppelloUnico(String, String)};</li>
 *   <li>Le colonne della tabella mostrano ID, appello, data chiusura, chiuso, firmato, note e numero di esami associati;</li>
 *   <li>Il refresh della tabella viene effettuato dopo ogni operazione CRUD per aggiornare la vista;</li>
 *   <li>L'eliminazione del verbale non richiede ulteriori verifiche di relazioni collegate (ma può essere estesa se necessario).</li>
 * </ul>
 */

public class VerbaliPannello1 implements CrudPanel {

    // Etichette
    private static final String L_ID            = "ID Verbale";
    private static final String L_APPELLO       = "Appello";
    private static final String L_DATA_CHIUSURA = "Data chiusura";
    private static final String L_CHIUSO        = "Chiuso";
    private static final String L_FIRMATO       = "Firmato";
    private static final String L_NOTE          = "Note";
    private static final String L_NUM_ESAMI     = "Numero esami";

    // Dipendenze
    private final VerbaleService verbaleService;
    private final VistaConDettagliBuilder<Verbale> builder;
    private final DomainQueryService domainQueryService;

    // Loader esterni
    private final Supplier<List<Appello>> loadAppelli;

    // Costruttore
    public VerbaliPannello1(VerbaleService verbaleService,
                            Supplier<List<Appello>> loadAppelli,
                            DomainQueryService domainQueryService) {
        this.verbaleService = verbaleService;
        this.loadAppelli = loadAppelli;
        this.domainQueryService = domainQueryService;
        this.builder = new VistaConDettagliBuilder<>(verbaleService.findAll());
    }

    // Blocchiamo il costruttore di default
    private VerbaliPannello1(){
        this.verbaleService = null;
        this.builder = null;
        this.loadAppelli = null;
        this.domainQueryService = null;
    }

    // API CrudPanel
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Verbali",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaVerbale,
                this::elimina
        );
    }

    // Dialog di aggiunta
    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    // Dialog di modifica
    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un verbale."); return; }
        mostraDialogModificaVerbale(sel);
    }

    // Dialog di eliminazione
    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un verbale."); return; }
        elimina(sel);
    }

    // Funzione di aggiornamento grafica tabella dopo aver apportato una modifica
    @Override
    public void refresh() {
        builder.refresh(verbaleService.findAll());
    }

    public VistaConDettagliBuilder<Verbale> getBuilder() { return builder; }

    // Colonne
    private LinkedHashMap<String, Function<Verbale, String>> colonne() {
        LinkedHashMap<String, Function<Verbale, String>> columns = new LinkedHashMap<>();
        columns.put(L_ID, v -> v.getId() != null ? v.getId().toString() : "");
        columns.put(L_APPELLO, Verbale::getAppelloId);
        columns.put(L_DATA_CHIUSURA, v -> v.getDataChiusura() != null ? v.getDataChiusura().toString() : "");
        columns.put(L_CHIUSO, v -> Boolean.TRUE.equals(v.getChiuso()) ? "Sì" : "No");
        columns.put(L_FIRMATO, v -> Boolean.TRUE.equals(v.getFirmato()) ? "Sì" : "No");
        columns.put(L_NOTE, Verbale::getNote);
        columns.put(L_NUM_ESAMI, v -> v.getEsami() != null ? String.valueOf(v.getEsami().size()) : "0");
        return columns;
    }

    // Dettagli, TODO: Aggiungere i dettagli o verificare che siano solo questi
    private LinkedHashMap<String, Function<Verbale, String>> dettagli() {
        LinkedHashMap<String, Function<Verbale, String>> details = new LinkedHashMap<>(colonne());
        return details;
    }

    // Dialoghi CRUD
    public void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Verbale",
                "Inserisci i dati del verbale",
                null,
                vCreato -> {
                    if (!validateAppelloUnico(vCreato.getAppelloId(), null)) return null;
                    return verbaleService.create(vCreato);
                },
                "Successo",
                "Verbale aggiunto correttamente!"
        );
    }

    public void mostraDialogModificaVerbale(Verbale verbale) {
        mostraDialogoCrud(
                "Modifica Verbale",
                "Modifica i dati del verbale",
                verbale,
                vAgg -> {
                    if (!validateAppelloUnico(vAgg.getAppelloId(), vAgg.getId())) return null;
                    return verbaleService.update(vAgg);
                },
                "Successo",
                "Verbale modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Verbale iniziale,
                                   Function<Verbale, Verbale> persister,
                                   String successTitle,
                                   String successMessage){
        DialogBuilder<Verbale> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Verbale target = estraiVerbaleDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                v -> {refresh(); Dialogs.showInfo(successTitle, successMessage);}
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // Configurazione dei campi
    private void configuraCampi(DialogBuilder<Verbale> dialog, Verbale iniziale){
        Supplier<List<Appello>> filteredAppelli = () -> {
            List<Appello> all = loadAppelli.get();
            if (iniziale == null) {
                return all.stream()
                        .filter(a -> domainQueryService
                                .verbaleByAppello(String.valueOf(a.getId()))
                                .isEmpty())
                        .toList();
            } else {
                String current = iniziale.getAppelloId();
                return all.stream()
                        .filter(a -> {
                            String id = String.valueOf(a.getId());
                            return id.equals(current) ||
                                    domainQueryService.verbaleByAppello(id).isEmpty();
                        })
                        .toList();
            }
        };

        TableView<Appello> tabAppelli = TableMiniFactory.creaTabella(
                filteredAppelli,
                SelectionMode.SINGLE,
                0,
                new LinkedHashMap<>() {{
                    put("Data", a -> LocalDateUtil.toString(a.getData()));
                    put("Docente", Appello::getRidDocente);
                }}
        );


        if (iniziale != null && iniziale.getAppelloId() != null) {
            tabAppelli.getItems().stream()
                    .filter(a -> String.valueOf(a.getId()).equals(iniziale.getAppelloId()))
                    .findFirst()
                    .ifPresent(a -> tabAppelli.getSelectionModel().select(a));
        }

        DatePicker dpChiusura = new DatePicker(iniziale != null ? iniziale.getDataChiusura() : null);
        CheckBox cbChiuso = new CheckBox(L_CHIUSO);
        CheckBox cbFirmato = new CheckBox(L_FIRMATO);
        TextField tfNote = new TextField(iniziale != null ? iniziale.getNote() : "");

        if (iniziale != null) {
            cbChiuso.setSelected(Boolean.TRUE.equals(iniziale.getChiuso()));
            cbFirmato.setSelected(Boolean.TRUE.equals(iniziale.getFirmato()));
        }

        dialog.aggiungiCampo(L_APPELLO, tabAppelli);
        dialog.aggiungiCampo(L_DATA_CHIUSURA, dpChiusura);
        dialog.aggiungiCampo(L_CHIUSO, cbChiuso);
        dialog.aggiungiCampo(L_FIRMATO, cbFirmato);
        dialog.aggiungiCampo(L_NOTE, tfNote);
    }


    private Verbale estraiVerbaleDaCampi(Map<String, Control> campi, Verbale target) {
        // Appello id
        @SuppressWarnings("unchecked")
        TableView<Appello> tabAppelli = (TableView<Appello>) campi.get(L_APPELLO);
        if (tabAppelli == null) {
            throw new IllegalStateException("Campo 'Appello' non trovato nel dialog.");
        }
        Appello sel = tabAppelli.getSelectionModel().getSelectedItem();
        if (sel == null) {
            throw new IllegalArgumentException("Seleziona un appello.");
        }
        String appelloId = String.valueOf(sel.getId());

        // Altri Campi
        LocalDate dataChiusura = ((DatePicker) campi.get(L_DATA_CHIUSURA)).getValue();
        boolean chiuso = ((CheckBox) campi.get(L_CHIUSO)).isSelected();
        boolean firmato = ((CheckBox) campi.get(L_FIRMATO)).isSelected();
        String note = ((TextField) campi.get(L_NOTE)).getText();

        if (target == null) {
            // id null -> il repository assegnerà l'auto-increment
            return new Verbale(null, appelloId, dataChiusura, chiuso, firmato, note, null);
        } else {
            target.setAppelloId(appelloId);
            target.setDataChiusura(dataChiusura);
            target.setChiuso(chiuso);
            target.setFirmato(firmato);
            target.setNote(note);
            return target;
        }
    }

    private boolean validateAppelloUnico(String appelloId, String currentVerbaleId) {
        var existing = domainQueryService.verbaleByAppello(appelloId);
        if (existing.isPresent() && !java.util.Objects.equals(existing.get().getId(), currentVerbaleId)) {
            Dialogs.showError(
                    "Associazione non valida",
                    "Per l'appello selezionato esiste già un verbale."
            );
            return false;
        }
        return true;
    }

    private void elimina(Verbale v) {
        verbaleService.deleteById(v.getId());
        refresh();
    }
}