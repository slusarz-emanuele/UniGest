package it.univaq.unigest.gui.modelview.pannelli.cdl;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.actions.QueryActions;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class CorsiDiLaureaPannello1 implements CrudPanel {

    // Etichette
    private static final String L_ID              = "ID";
    private static final String L_NOME            = "Nome";
    private static final String L_CFU_TOTALI      = "CFU totali";
    private static final String L_DIPARTIMENTO    = "Dipartimento";
    private static final String L_COORDINATORE_ID = "Coordinatore ID";

    // Dipendenze
    private final CorsoDiLaureaService corsoDiLaureaService;
    private final VistaConDettagliBuilder<CorsoDiLaurea> builder;
    private final DomainQueryService domainQueryService;

    public CorsiDiLaureaPannello1(CorsoDiLaureaService corsoDiLaureaService,
                                  DomainQueryService domainQueryService){
        this.corsoDiLaureaService = corsoDiLaureaService;
        this.domainQueryService = domainQueryService;
        this.builder = new VistaConDettagliBuilder<>(corsoDiLaureaService.findAll());
    }

    // ===== API CrudPanel =====

    @Override
    public VBox getView (){
        return builder.build(
                "Gestione Corsi di laurea",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaCorsoDiLaurea,
                this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) {
            Dialogs.showError("Nessuna selezione","Seleziona un corso di laurea");
            return;
        }
        mostraDialogModificaCorsoDiLaurea(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) {
            Dialogs.showError("Nessuna selezione","Seleziona un corso di laurea");
            return;
        }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(corsoDiLaureaService.findAll());
    }

    public VistaConDettagliBuilder<CorsoDiLaurea> getBuilder() { return builder; }

    // ===== Colonne / Dettagli =====

    private LinkedHashMap<String, Function<CorsoDiLaurea, String>> colonne() {
        LinkedHashMap<String, Function<CorsoDiLaurea, String>> columns = new LinkedHashMap<>();
        columns.put(L_ID, CorsoDiLaurea::getId);
        columns.put(L_NOME, CorsoDiLaurea::getNome);
        columns.put(L_CFU_TOTALI, c -> Integer.toString(c.getCfuTotali()));
        columns.put(L_DIPARTIMENTO, CorsoDiLaurea::getDipartimento);
        columns.put(L_COORDINATORE_ID, CorsoDiLaurea::getCoordinatoreId);
        return columns;
    }

    private LinkedHashMap<String, Function<CorsoDiLaurea, String>> dettagli() {
        LinkedHashMap<String, Function<CorsoDiLaurea, String>> details = new LinkedHashMap<>(colonne());
        var actions = new QueryActions(domainQueryService);

        details.put("Insegnamenti", a-> "Visualizza Insegnamenti");
        builder.setLinkAction("Insegnamenti", corso -> actions.openInsegnamentiPerCorso(corso));

        details.put("Studenti", a-> "Visualizza Studenti");
        builder.setLinkAction("Studenti", corso -> actions.openStudentiPerCorso(corso));
        return details;
    }

    // ===== Dialoghi CRUD =====

    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo corso di laurea",
                "Inserisci i dati del corso di laurea",
                null, // create
                cdlCreato -> corsoDiLaureaService.create(cdlCreato),
                "Successo",
                "Corso di laurea aggiunto correttamente!"
        );
    }

    private void mostraDialogModificaCorsoDiLaurea(CorsoDiLaurea corsoDiLaurea) {
        mostraDialogoCrud(
                "Modifica corso di laurea",
                "Modifica i dati del corso di laurea",
                corsoDiLaurea, // edit
                cdlAgg -> corsoDiLaureaService.update(cdlAgg),
                "Successo",
                "Corso di laurea modificato con successo!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   CorsoDiLaurea iniziale, // null = create, non null = edit
                                   Function<CorsoDiLaurea, CorsoDiLaurea> persister,
                                   String successTitle,
                                   String successMsg) {

        DialogBuilder<CorsoDiLaurea> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    CorsoDiLaurea target = estraiCorsoDiLaureaDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                cdl -> { refresh(); Dialogs.showInfo(successTitle, successMsg); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    /**
     * Aggiunge i campi al DialogBuilder. Se 'iniziale' è non nullo, pre-popola i controlli.
     */
    private void configuraCampi(DialogBuilder<CorsoDiLaurea> dialog, CorsoDiLaurea iniziale) {
        dialog.aggiungiCampo(L_ID,           new TextField(iniziale != null ? iniziale.getId()           : ""));
        dialog.aggiungiCampo(L_NOME,         new TextField(iniziale != null ? iniziale.getNome()         : ""));
        dialog.aggiungiCampo(L_CFU_TOTALI,   new TextField(iniziale != null ? String.valueOf(iniziale.getCfuTotali()) : ""));
        dialog.aggiungiCampo(L_DIPARTIMENTO, new TextField(iniziale != null ? iniziale.getDipartimento() : ""));
        dialog.aggiungiCampo(L_COORDINATORE_ID, new TextField(iniziale != null ? iniziale.getCoordinatoreId() : ""));
    }

    /**
     * Legge i controlli dal dialogo e costruisce/aggiorna il Corso di laurea.
     * Se 'target' è null → create, altrimenti update sullo stesso oggetto.
     */
    private CorsoDiLaurea estraiCorsoDiLaureaDaCampi(Map<String, Control> campi, CorsoDiLaurea target) {
        String id            = DialogsParser.validaCampo(campi, L_ID);
        String nome          = DialogsParser.validaCampo(campi, L_NOME);
        String cfuText       = DialogsParser.validaCampo(campi, L_CFU_TOTALI);
        int cfuTotali        = Integer.parseInt(cfuText);
        String dipartimento  = DialogsParser.validaCampo(campi, L_DIPARTIMENTO);
        String coordinatoreId= DialogsParser.validaCampo(campi, L_COORDINATORE_ID);

        if (target == null) {
            return new CorsoDiLaurea(id, nome, cfuTotali, dipartimento, coordinatoreId);
        } else {
            target.setId(id);
            target.setNome(nome);
            target.setCfuTotali(cfuTotali);
            target.setDipartimento(dipartimento);
            target.setCoordinatoreId(coordinatoreId);
            return target;
        }
    }

    private void elimina(CorsoDiLaurea cdl) {
        try {
            corsoDiLaureaService.deleteById(cdl.getId());
            refresh();
        } catch (Exception e) {
            Dialogs.showError("Errore", e.getMessage());
        }
    }
}
