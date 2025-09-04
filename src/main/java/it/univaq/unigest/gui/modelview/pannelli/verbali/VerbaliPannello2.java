package it.univaq.unigest.gui.modelview.pannelli.verbali;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.actions.QueryActions;
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

public class VerbaliPannello2 implements CrudPanel {

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
    public VerbaliPannello2(VerbaleService verbaleService,
                            Supplier<List<Appello>> loadAppelli,
                            DomainQueryService domainQueryService) {
        this.verbaleService = verbaleService;
        this.loadAppelli = loadAppelli;
        this.domainQueryService = domainQueryService;
        this.builder = new VistaConDettagliBuilder<>(verbaleService.findAll());
    }

    // Blocchiamo il costruttore di default
    private VerbaliPannello2(){
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
                vCreato -> verbaleService.create(vCreato),
                "Successo",
                "Verbale aggiunto correttamente!"
        );
    }

    public void mostraDialogModificaVerbale(Verbale verbale) {
        mostraDialogoCrud(
                "Modifica Verbale",
                "Modifica i dati del verbale",
                verbale,
                vAgg -> verbaleService.create(vAgg),
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
    private void configuraCampi(DialogBuilder<Verbale> dialog,
                                Verbale iniziale){

        TableView<Appello> tabAppelli = TableMiniFactory.creaTabella(
                loadAppelli,
                SelectionMode.SINGLE,
                0,
                new LinkedHashMap<>() {{
                    put("Data", a -> LocalDateUtil.toString(a.getData()));
                    put("Docente", Appello::getRidDocente);
                }}
        );

        // Preselezione in modifica
        if (iniziale != null && iniziale.getAppelloId() != null) {
            tabAppelli.getItems().stream()
                    .filter(a -> String.valueOf(a.getId()).equals(iniziale.getAppelloId()))
                    .findFirst()
                    .ifPresent(a -> tabAppelli.getSelectionModel().select(a));
        }

        dialog.aggiungiCampo(L_APPELLO, tabAppelli);
        dialog.aggiungiCampo(L_DATA_CHIUSURA, new DatePicker());
        dialog.aggiungiCampo(L_CHIUSO, new CheckBox(L_CHIUSO));
        dialog.aggiungiCampo(L_FIRMATO, new CheckBox(L_FIRMATO));
        dialog.aggiungiCampo(L_NOTE, new TextField());
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

    private void elimina(Verbale v) {
        verbaleService.deleteById(v.getId());
        refresh();
    }
}