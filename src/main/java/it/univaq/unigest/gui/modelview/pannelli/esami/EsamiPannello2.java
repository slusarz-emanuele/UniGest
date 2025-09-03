package it.univaq.unigest.gui.modelview.pannelli.esami;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.*;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.util.LocalDateUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class EsamiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID          = "ID";
    private static final String L_ISCRIZIONE  = "Iscrizione ID";
    private static final String L_VOTO        = "Voto";
    private static final String L_LODE        = "Lode";
    private static final String L_RIFIUTATO   = "Rifiutato";
    private static final String L_VERBALIZZATO = "Verbalizzato";

    // Dipendenze
    private final EsameService esameService;
    private final VistaConDettagliBuilder<Esame> builder;

    // Loader esterni
    private final Supplier<List<Iscrizione>> loadIscrizioni;

    // Costruttore
    public EsamiPannello2(EsameService esameService,
                          Supplier<List<Iscrizione>> loadIscrizioni) {
        this.esameService = esameService;
        this.loadIscrizioni = loadIscrizioni;
        this.builder = new VistaConDettagliBuilder<>(esameService.findAll());
    }

    // Blocchiamo il costruttore di default
    private EsamiPannello2(){
        this.esameService = null;
        this.builder = null;
        this.loadIscrizioni = null;
    }

    // API CrudPanel
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Esami",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaEsame,
                this::elimina
        );
    }

    // Dialog di aggiunta
    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    // Dialog di eliminazione
    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un esame."); return; }
        mostraDialogModificaEsame(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un esame."); return; }
        elimina(sel);
    }

    // Funzione di aggiornamento grafica tabella dopo aver apportato una modifica
    @Override
    public void refresh() {
        builder.refresh(esameService.findAll());
    }

    public VistaConDettagliBuilder<Esame> getBuilder() { return builder; }

    // Colonne
    private LinkedHashMap<String, Function<Esame, String>> colonne() {
        LinkedHashMap<String, Function<Esame, String>> columns = new LinkedHashMap<>();
        columns.put(L_ID, Esame::getId);
        columns.put(L_ISCRIZIONE, Esame::getIscrizioneId);
        columns.put(L_VOTO, e -> e.getVoto() != null ? String.valueOf(e.getVoto()) : "0");
        columns.put(L_LODE, e -> Boolean.TRUE.equals(e.isLode()) ? "Sì" : "No");
        columns.put(L_RIFIUTATO, e -> Boolean.TRUE.equals(e.isRifiutato()) ? "Sì" : "No");
        columns.put(L_VERBALIZZATO, e -> Boolean.TRUE.equals(e.isVerbalizzato())? "Sì" : "No");
        return columns;
    }

   // Dettagli
   private LinkedHashMap<String, Function<Esame, String>> dettagli() {
       LinkedHashMap<String, Function<Esame, String>> details = new LinkedHashMap<>(colonne());
       return details;
   }

    // Dialoghi CRUD
    public void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Esame",
                "Inserisci i dati dell'esame",
                null,
                eCreato -> esameService.create(eCreato),
                "Successo",
                "Esame aggiunto correttamente!"
        );
    }

    public void mostraDialogModificaEsame(Esame esame) {
        mostraDialogoCrud(
                "Modifica Esame",
                "Modifica i dati dell'esame",
                esame,
                eAgg -> esameService.create(eAgg),
                "Successo",
                "Esame modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Esame iniziale,
                                   Function<Esame, Esame> persister,
                                   String successTitle,
                                   String successMessage){
        DialogBuilder<Esame> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Esame target = estraiEsameDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                v -> {refresh(); Dialogs.showInfo(successTitle, successMessage);}
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // Configurazione dei campi
    private void configuraCampi(DialogBuilder<Esame> dialog,
                                Esame iniziale){

        TableView<Iscrizione> tabIscrizione = TableMiniFactory.creaTabella(
                loadIscrizioni,
                SelectionMode.SINGLE,
                0,
                // TODO: da modificare per iscrizioni
                new LinkedHashMap<>() {{
                    put("Data", a -> LocalDateUtil.toString(a.getDataIscrizione()));
                    put("Studente", Iscrizione::getRidStudenteCf);
                    put("Appello", a -> String.valueOf(a.getRidAppello()));
                }}
        );

        // TODO: Preselezione in modifica per iscrizioni
        if (iniziale != null && iniziale.getIscrizioneId() != null) {
            tabIscrizione.getItems().stream()
                    .filter(a -> String.valueOf(a.getId()).equals(iniziale.getIscrizioneId()))
                    .findFirst()
                    .ifPresent(a -> tabIscrizione.getSelectionModel().select(a));
        }


        dialog.aggiungiCampo(L_ISCRIZIONE, tabIscrizione);
        dialog.aggiungiCampo(L_VOTO, generaComboVoti(iniziale != null ? iniziale.getVoto() : null));
        dialog.aggiungiCampo(L_LODE, new CheckBox(L_LODE));
        dialog.aggiungiCampo(L_RIFIUTATO, new CheckBox(L_RIFIUTATO));
        dialog.aggiungiCampo(L_VERBALIZZATO, new CheckBox(L_VERBALIZZATO));
    }

    private Esame estraiEsameDaCampi(Map<String, Control> campi, Esame target) {
        // Esame id -> selezione Iscrizione
        @SuppressWarnings("unchecked")
        TableView<Iscrizione> tabIscrizioni = (TableView<Iscrizione>) campi.get(L_ISCRIZIONE);
        if (tabIscrizioni == null) {
            throw new IllegalStateException("Campo 'Iscrizione' non trovato nel dialog.");
        }
        Iscrizione sel = tabIscrizioni.getSelectionModel().getSelectedItem();
        if (sel == null) {
            throw new IllegalArgumentException("Seleziona un'iscrizione.");
        }
        String iscrizioneId = String.valueOf(sel.getId());

        // Altri Campi
        @SuppressWarnings("unchecked")
        ComboBox<Double> cbVoto = (ComboBox<Double>) campi.get(L_VOTO);
        Double voto = cbVoto != null ? cbVoto.getValue() : null;
        double votoVal = (voto != null) ? voto : 0.0;

        boolean lode         = ((CheckBox) campi.get(L_LODE)).isSelected();
        boolean rifiutato    = ((CheckBox) campi.get(L_RIFIUTATO)).isSelected();
        boolean verbalizzato = ((CheckBox) campi.get(L_VERBALIZZATO)).isSelected();

        if (target == null) {
            // id null -> il repository assegnerà l'auto-increment
            return new Esame(
                    null,
                    iscrizioneId,
                    votoVal,
                    lode,
                    rifiutato,
                    verbalizzato
            );
        } else {
            target.setIscrizioneId(iscrizioneId);
            target.setVoto(votoVal);
            target.setLode(lode);
            target.setRifiutato(rifiutato);
            target.setVerbalizzato(verbalizzato);
            return target;
        }
    }

    private void elimina(Esame v) {
        esameService.deleteById(v.getId());
        refresh();
    }

    private ComboBox<Double> generaComboVoti(Double preselezione) {
        List<Double> voti = new ArrayList<>();
        for (double v = 0.0; v <= 30.0 + 1e-9; v += 0.25) voti.add(Math.round(v * 100.0) / 100.0);
        ComboBox<Double> combo = new ComboBox<>(FXCollections.observableArrayList(voti));
        combo.setPrefWidth(150);
        if (preselezione != null) combo.setValue(preselezione);
        return combo;
    }
}
