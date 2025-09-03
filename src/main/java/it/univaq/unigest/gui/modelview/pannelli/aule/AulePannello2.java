package it.univaq.unigest.gui.modelview.pannelli.aule;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.AulaService;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class AulePannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID       = "ID";
    private static final String L_CAPIENZA = "Capienza";
    private static final String L_EDIFICIO = "Edificio";

    // Dipendenze
    private final VistaConDettagliBuilder<Aula> builder;
    private final AulaService aulaService;

    // Loader Esterni
    private final Supplier<List<Edificio>> loadEdifici;

    // Costruttore
    public AulePannello2(AulaService aulaService,
                         Supplier<List<Edificio>> loadEdifici) {
        this.aulaService = aulaService;
        this.loadEdifici = loadEdifici;
        this.builder = new VistaConDettagliBuilder<>(aulaService.findAll());
    }

    // Blocchiamo il costruttore di default
    private AulePannello2() {
        this.aulaService = null;
        this.loadEdifici = null;
        this.builder = null;
    }

    // API CrudPanel
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Aule",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaAula,
                this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'aula."); return; }
        mostraDialogModificaAula(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'aula."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(aulaService.findAll());
    }

    public VistaConDettagliBuilder<Aula> getBuilder() { return builder; }

    // Colonne
    private LinkedHashMap<String, Function<Aula, String>> colonne() {
        LinkedHashMap<String, Function<Aula, String>> columns = new LinkedHashMap<>();
        columns.put(L_ID, Aula::getId);
        columns.put(L_CAPIENZA, a -> Integer.toString(a.getCapienza()));
        columns.put(L_EDIFICIO, a -> nomeEdificioById(a.getEdificio()));
        return columns;
    }

    // Dettagli
    private LinkedHashMap<String, Function<Aula, String>> dettagli() {
        return new LinkedHashMap<>(colonne());
    }

    // Dialoghi CRUD
    public void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuova Aula",
                "Inserisci i dati dell'aula",
                null,
                aCreato -> aulaService.create(aCreato),
                "Successo",
                "Aula aggiunta correttamente!"
        );
    }

    public void mostraDialogModificaAula(Aula aula) {
        mostraDialogoCrud(
                "Modifica Aula",
                "Modifica i dati dell'aula",
                aula,
                aAgg -> aulaService.update(aAgg),
                "Successo",
                "Aula modificata correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Aula iniziale,
                                   Function<Aula, Aula> persister,
                                   String successTitle,
                                   String successMessage) {
        DialogBuilder<Aula> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Aula target = estraiAulaDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                a -> { refresh(); Dialogs.showInfo(successTitle, successMessage); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // Configurazione dei campi (ordine: Capienza -> Edificio)
    private void configuraCampi(DialogBuilder<Aula> dialog, Aula iniziale) {
        // Capienza
        TextField tfCapienza = new TextField(iniziale != null ? String.valueOf(iniziale.getCapienza()) : "");
        dialog.aggiungiCampo(L_CAPIENZA, tfCapienza);

        // Tabellina Edifici
        TableView<Edificio> tabEdifici = TableMiniFactory.creaTabella(
                loadEdifici,
                SelectionMode.SINGLE,
                0,
                new LinkedHashMap<>() {{
                    put("ID", Edificio::getId);
                    put("Nome", e -> {
                        try { return (String) Edificio.class.getMethod("getNome").invoke(e); }
                        catch (Exception ex) { return e.getId(); }
                    });
                }}
        );

        // Preselezione in modifica
        if (iniziale != null && iniziale.getEdificio() != null) {
            tabEdifici.getItems().stream()
                    .filter(ed -> ed.getId().equals(iniziale.getEdificio()))
                    .findFirst()
                    .ifPresent(ed -> tabEdifici.getSelectionModel().select(ed));
        }

        dialog.aggiungiCampo(L_EDIFICIO, tabEdifici);
    }

    private Aula estraiAulaDaCampi(Map<String, Control> campi, Aula target) {
        // Capienza
        String capStr = ((TextField) campi.get(L_CAPIENZA)).getText();
        int capienza;
        try {
            capienza = Integer.parseInt(capStr.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La capienza deve essere un numero intero valido.");
        }

        // Edificio selezionato
        @SuppressWarnings("unchecked")
        TableView<Edificio> tabEdifici = (TableView<Edificio>) campi.get(L_EDIFICIO);
        if (tabEdifici == null) throw new IllegalStateException("Campo 'Edificio' non trovato nel dialog.");
        Edificio sel = tabEdifici.getSelectionModel().getSelectedItem();
        if (sel == null) throw new IllegalArgumentException("Seleziona un edificio.");

        if (target == null) {
            // id null -> repository assegna auto-increment
            return new Aula(null, capienza, sel.getId());
        } else {
            target.setCapienza(capienza);
            target.setEdificio(sel.getId());
            return target;
        }
    }

    private void elimina(Aula a) {
        aulaService.deleteById(a.getId());
        refresh();
    }

    // Helper
    private String nomeEdificioById(String id) {
        if (id == null) return "";
        return loadEdifici.get().stream()
                .filter(e -> id.equals(e.getId()))
                .map(e -> {
                    try { return (String) Edificio.class.getMethod("getNome").invoke(e); }
                    catch (Exception ex) { return e.getId(); }
                })
                .findFirst()
                .orElse("");
    }
}