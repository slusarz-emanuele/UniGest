package it.univaq.unigest.gui.modelview.pannelli.edifici;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.EdificioService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Pannello di gestione “Edifici”.
 * <p>
 * Responsabilità:
 * <ul>
 *   <li>Visualizzazione tabellare degli edifici (via {@link VistaConDettagliBuilder});</li>
 *   <li>Dialog di aggiunta/modifica con validazioni (nome obbligatorio, ID gestito automaticamente dal repository);</li>
 *   <li>Eliminazione semplice, senza controlli sulle relazioni;</li>
 *   <li>Dettagli dell’edificio mostrati nella parte destra del builder (stesso contenuto della tabella).</li>
 * </ul>
 *
 * <h3>Dipendenze iniettate</h3>
 * <ul>
 *   <li>{@link EdificioService}: CRUD e accesso ai dati degli edifici;</li>
 *   <li>{@link DomainQueryService}: per eventuali verifiche sulle entità esistenti (ad esempio, prevenzione duplicati);</li>
 *   <li>{@link VistaConDettagliBuilder}: gestione della tabella e dei dettagli degli edifici.</li>
 * </ul>
 *
 * <h3>Note di implementazione</h3>
 * <ul>
 *   <li>Le colonne e i dettagli della tabella sono definiti nei metodi {@code colonne()} e {@code dettagli()};</li>
 *   <li>I dialog di aggiunta/modifica sono realizzati con {@link DialogBuilder} e validano il campo nome obbligatorio;</li>
 *   <li>L’eliminazione rimuove direttamente l’edificio selezionato e aggiorna la tabella;</li>
 *   <li>Il campo ID non è modificabile; viene gestito internamente dal repository tramite auto-incremento;</li>
 *   <li>Il refresh della tabella avviene automaticamente dopo ogni operazione CRUD.</li>
 * </ul>
 */

public class EdificiPannello1 implements CrudPanel {

    // Etichette
    private static final String L_ID   = "ID";
    private static final String L_NOME = "Nome";

    // Dipendenze
    private final VistaConDettagliBuilder<Edificio> builder;
    private final EdificioService edificioService;
    private final DomainQueryService domainQueryService;

    public EdificiPannello1(EdificioService edificioService,
                            DomainQueryService domainQueryService) {
        this.edificioService = edificioService;
        this.domainQueryService = domainQueryService;
        this.builder = new VistaConDettagliBuilder<>(edificioService.findAll());
    }

    // Blocchiamo il costruttore di default
    private EdificiPannello1() {
        this.edificioService = null;
        this.builder = null;
        this.domainQueryService = null;
    }

    // ===== CrudPanel API =====
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Edifici",
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un edificio."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un edificio."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(edificioService.findAll());
    }

    public VistaConDettagliBuilder<Edificio> getBuilder() { return builder; }

    // ===== Colonne / Dettagli =====
    private LinkedHashMap<String, Function<Edificio, String>> colonne() {
        LinkedHashMap<String, Function<Edificio, String>> map = new LinkedHashMap<>();
        map.put(L_ID,   Edificio::getId);
        map.put(L_NOME, Edificio::getNome);
        return map;
    }

    private LinkedHashMap<String, Function<Edificio, String>> dettagli() {
        return new LinkedHashMap<>(colonne());
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Edificio",
                "Inserisci i dati dell'edificio",
                null,
                eCreato -> {
                    if(domainQueryService.existsEdificioByName(eCreato.getNome())){
                        Dialogs.showError("Errore di creazione",
                                "Esiste già un edificio chiamato \"" + eCreato.getNome() + "\".");
                        return null;
                    }
                    return edificioService.create(eCreato);
                },   // id null -> auto-increment nel repo
                "Successo",
                "Edificio aggiunto correttamente!"
        );
    }

    private void mostraDialogModifica(Edificio edificio) {
        mostraDialogoCrud(
                "Modifica Edificio",
                "Modifica i dati dell'edificio",
                edificio,
                eAgg -> {
                    if (domainQueryService.existsEdificioByNameExceptId(eAgg.getNome(), edificio.getId())) {
                        Dialogs.showError("Errore di modifica",
                                "Esiste già un edificio chiamato \"" + eAgg.getNome() + "\".");
                        return null;
                    }
                    return edificioService.update(eAgg);
                },
                "Successo",
                "Edificio modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Edificio iniziale,
                                   Function<Edificio, Edificio> persister,
                                   String successTitle,
                                   String successMessage) {
        DialogBuilder<Edificio> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Edificio target = estraiEdificioDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                e -> { refresh(); Dialogs.showInfo(successTitle, successMessage); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    private void configuraCampi(DialogBuilder<Edificio> dialog, Edificio iniziale) {
        // Solo il nome Ã¨ editabile; l'ID viene gestito dal repository
        dialog.aggiungiCampo(L_NOME, new TextField(iniziale != null ? iniziale.getNome() : ""));
    }

    private Edificio estraiEdificioDaCampi(Map<String, Control> campi, Edificio target) {
        String nome = DialogsParser.validaCampo(campi, L_NOME);

        if (target == null) {
            // create: id null per auto-increment
            return new Edificio(null, nome);
        } else {
            target.setNome(nome);
            return target;
        }
    }

    private void elimina(Edificio e) {
        edificioService.deleteById(e.getId());
        refresh();
    }

}