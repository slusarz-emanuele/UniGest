package it.univaq.unigest.gui.modelview.pannelli.iscrizioni;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.*;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.util.LocalDateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class IscrizioniPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID               = "ID Iscrizione";
    private static final String L_DATA_ISCRIZIONE  = "Data iscrizione";
    private static final String L_RITIRATO         = "Ritirato";
    private static final String L_STUDENTE         = "Studente (CF)";
    private static final String L_APPELLO          = "Appello";

    // Dipendenze
    private final IscrizioneService iscrizioneService;
    private final VistaConDettagliBuilder<Iscrizione> builder;

    // Loader esterni
    private final Supplier<List<Studente>> loadStudenti;
    private final Supplier<List<Appello>> loadAppelli;

    // Costruttore
    public IscrizioniPannello2(IscrizioneService iscrizioneService,
                               Supplier<List<Studente>> loadStudenti,
                               Supplier<List<Appello>> loadAppelli) {
        this.iscrizioneService = iscrizioneService;
        this.loadStudenti = loadStudenti;
        this.loadAppelli = loadAppelli;
        this.builder = new VistaConDettagliBuilder<>(iscrizioneService.findAll());
    }

    // Blocchiamo il costruttore di default
    public IscrizioniPannello2() {
        this.iscrizioneService = null;
        this.builder = null;
        this.loadAppelli = null;
        loadStudenti = null;
    }

    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Iscrizioni",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaIscrizione,
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'iscrizione."); return; }
        mostraDialogModificaIscrizione(sel);
    }

    // Dialog di eliminazione
    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un'iscrizione."); return; }
        elimina(sel);
    }

    // Funzione di aggiornamento grafica tabella dopo aver apportato una modifica
    @Override
    public void refresh() {
        builder.refresh(iscrizioneService.findAll());
    }

    public VistaConDettagliBuilder<Iscrizione> getBuilder() { return builder; }

    // Colonne
    private LinkedHashMap<String, Function<Iscrizione, String>> colonne() {
        LinkedHashMap<String, Function<Iscrizione, String>> columns = new LinkedHashMap<>();
        columns.put(L_ID, i -> i.getId() != null ? i.getId().toString() : "");
        columns.put(L_DATA_ISCRIZIONE, i -> i.getDataIscrizione() != null ? i.getDataIscrizione().toString() : "");
        columns.put(L_RITIRATO, i -> Boolean.TRUE.equals(i.getRitirato()) ? "Sì" : "No");
        columns.put(L_STUDENTE, Iscrizione::getRidStudenteCf);
        columns.put(L_APPELLO, i -> i.getRidAppello() != 0 ? String.valueOf(i.getRidAppello()) : "");
        return columns;
    }

    // Dettagli
    private LinkedHashMap<String, Function<Iscrizione, String>> dettagli() {
        LinkedHashMap<String, Function<Iscrizione, String>> details = new LinkedHashMap<>(colonne());
        return details;
    }

    // Dialoghi CRUD
    public void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuova iscrizione",
                "Inserisci i dati dell'iscrizione",
                null,
                iCreato -> iscrizioneService.create(iCreato),
                "Successo",
                "Iscrizione aggiunto correttamente!"
        );
    }


    private void mostraDialogModificaIscrizione(Iscrizione iscrizione) {
        mostraDialogoCrud(
                "Modifica Iscrizione",
                "Modifica i dati dell'iscrizione",
                iscrizione,
                iAgg -> iscrizioneService.create(iAgg),
                "Successo",
                "Iscrizione modificata correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Iscrizione iniziale,
                                   Function<Iscrizione, Iscrizione> persister,
                                   String successTitle,
                                   String successMessage){
        DialogBuilder<Iscrizione> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Iscrizione target = estraiIscrizioneDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                i -> {refresh(); Dialogs.showInfo(successTitle, successMessage);}
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // Configurazione dei campi
    private void configuraCampi(DialogBuilder<Iscrizione> dialog,
                                Iscrizione iniziale) {

        TableView<Studente> tabStudenti = TableMiniFactory.creaTabella(
                loadStudenti,
                SelectionMode.SINGLE,
                0,
                new LinkedHashMap<>() {{
                    put("Data", s -> LocalDateUtil.toString(s.getData()));
                    put("Docente", Appello::getRidDocente);
                }}
        );

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
        if (iniziale != null && iniziale.getRidStudenteCf() != null) {
            tabStudenti.getItems().stream()
                .filter(s -> String.valueOf(s.getId()).equals(iniziale.getRidStudenteCf()))
                .findFirst()
                .ifPresent(s -> tabStudenti.getSelectionModel().select(s));
    }
        if (iniziale != null && iniziale.getRidAppello() != 0) {
            tabAppelli.getItems().stream()
                    .filter(a -> String.valueOf(a.getId()).equals(iniziale.getRidAppello()))
                    .findFirst()
                    .ifPresent(a -> tabAppelli.getSelectionModel().select(a));
        }

        dialog.aggiungiCampo(L_DATA_ISCRIZIONE, new DatePicker());
        dialog.aggiungiCampo(L_RITIRATO, new CheckBox(L_RITIRATO));
        dialog.aggiungiCampo(L_STUDENTE, tabStudenti);
        dialog.aggiungiCampo(L_APPELLO, tabAppelli);
}

        private void elimina (Iscrizione i){
            iscrizioneService.deleteById(i.getId());
            refresh();
        }
    }
