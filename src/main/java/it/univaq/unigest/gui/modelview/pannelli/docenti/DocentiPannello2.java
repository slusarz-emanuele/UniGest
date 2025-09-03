package it.univaq.unigest.gui.modelview.pannelli.docenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.service.DocenteService;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Control;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class DocentiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_CF = "CF";
    private static final String L_NOME = "Nome";
    private static final String L_COGNOME = "Cognome";
    private static final String L_DATA_NASCITA = "Data di Nascita";
    private static final String L_CODICE_DOCENTE = "Codice Docente";
    private static final String L_RUOLO = "Ruolo";
    private static final String L_INGRESSO_DOCENTE = "Ingresso Univ. Docente";
    private static final String L_DIPARTIMENTO = "Dipartimento";
    private static final String L_QUALIFICA = "Qualifica";
    private static final String L_EMAIL = "Email";

    // Dipendenze
    private final DocenteService docenteService;
    private final VistaConDettagliBuilder<Docente> builder;

    public DocentiPannello2(DocenteService docenteService){
        this.docenteService = docenteService;
        this.builder = new VistaConDettagliBuilder<>(docenteService.findAll());
    }

    // ===== API CrudPanel =====

    @Override
    public VBox getView (){
        return builder.build(
                "Gestione Docenti",
                colonne(),
                dettagli(),
                this::apriDialogAggiungi,
                this::mostraDialogModificaDocente,
                this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }

    @Override
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione","Seleziona un docente"); return; }
        mostraDialogModificaDocente(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione","Seleziona un docente"); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(docenteService.findAll());
    }

    public VistaConDettagliBuilder<Docente> getBuilder() { return builder; }

    // ===== Colonne / Dettagli (no duplicazioni) =====

    private LinkedHashMap<String, Function<Docente, String>> colonne() {
        LinkedHashMap<String, Function<Docente, String>> columns = new LinkedHashMap<>();
        columns.put(L_CF, Docente::getCf);
        columns.put(L_NOME, Docente::getNome);
        columns.put(L_COGNOME, Docente::getCognome);
        columns.put(L_EMAIL, Docente::getEmail);
        columns.put(L_RUOLO, d -> d.isRuolo() ? "Di ruolo" : "Esterno");
        columns.put(L_DIPARTIMENTO, Docente::getDipartimento);
        columns.put(L_QUALIFICA, Docente::getQualifica);
        return columns;
    }

    private LinkedHashMap<String, Function<Docente, String>> dettagli() {
        LinkedHashMap<String, Function<Docente, String>> details = new LinkedHashMap<>(colonne());
        details.put(L_DATA_NASCITA, d -> String.valueOf(d.getDataNascita()));
        details.put(L_INGRESSO_DOCENTE, d -> String.valueOf(d.getDataIngressoUniversitaDocente()));
        details.put(L_CODICE_DOCENTE, Docente::getCodiceDocente);
        return details;
    }

    // ===== Dialoghi CRUD compattati =====

    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Docente",
                "Inserisci i dati del docente",
                null,                              // create
                dCreato -> docenteService.create(dCreato),
                "Successo",
                "Docente aggiunto correttamente!"
        );
    }

    private void mostraDialogModificaDocente(Docente docente) {
        mostraDialogoCrud(
                "Modifica Docente",
                "Modifica i dati del docente",
                docente,                           // edit
                dAgg -> docenteService.update(dAgg),
                "Successo",
                "Docente modificato con successo!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Docente iniziale, // null = create, non null = edit
                                   Function<Docente, Docente> persister,
                                   String successTitle,
                                   String successMsg) {

        DialogBuilder<Docente> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Docente target = estraiDocenteDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                d -> { refresh(); Dialogs.showInfo(successTitle, successMsg); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    /**
     * Aggiunge i campi al DialogBuilder. Se 'iniziale' è non nullo, pre-popola i controlli.
     */
    private void configuraCampi(DialogBuilder<Docente> dialog, Docente iniziale) {
        // TextFields
        dialog.aggiungiCampo(L_CF, new TextField(iniziale != null ? iniziale.getCf() : ""));
        dialog.aggiungiCampo(L_NOME, new TextField(iniziale != null ? iniziale.getNome() : ""));
        dialog.aggiungiCampo(L_COGNOME, new TextField(iniziale != null ? iniziale.getCognome() : ""));
        dialog.aggiungiCampo(L_CODICE_DOCENTE, new TextField(iniziale != null ? iniziale.getCodiceDocente() : ""));
        dialog.aggiungiCampo(L_DIPARTIMENTO, new TextField(iniziale != null ? iniziale.getDipartimento() : ""));

        // DatePicker
        dialog.aggiungiCampo(L_DATA_NASCITA, new DatePicker(
                iniziale != null && iniziale.getDataNascita()!=null
                        ? LocalDate.parse(iniziale.getDataNascita())
                        : null
        ));
        dialog.aggiungiCampo(L_INGRESSO_DOCENTE, new DatePicker(
                iniziale != null ? iniziale.getDataIngressoUniversitaDocente() : null
        ));

        // CheckBox
        CheckBox ruolo = new CheckBox("Docente di ruolo");
        ruolo.setSelected(iniziale != null && iniziale.isRuolo());
        dialog.aggiungiCampo(L_RUOLO, ruolo);

        // ComboBox Qualifica
        ComboBox<String> qualifica = new ComboBox<>();
        qualifica.setItems(FXCollections.observableArrayList("Ordinario","Associato","Ricercatore","Contratto"));
        if (iniziale != null) qualifica.setValue(iniziale.getQualifica());
        dialog.aggiungiCampo(L_QUALIFICA, qualifica);
    }

    /**
     * Legge i controlli dal dialogo e costruisce/aggiorna il Docente.
     * Se 'target' è null → create, altrimenti update sullo stesso oggetto.
     */
    private Docente estraiDocenteDaCampi(Map<String, Control> campi, Docente target) {
        // Letture/validazioni centralizzate
        String cf = DialogsParser.validaCampo(campi, L_CF);
        String nome = DialogsParser.validaCampo(campi, L_NOME);
        String cognome = DialogsParser.validaCampo(campi, L_COGNOME);
        LocalDate dataNascita = ((DatePicker) campi.get(L_DATA_NASCITA)).getValue();
        String codiceDocente = DialogsParser.validaCampo(campi, L_CODICE_DOCENTE);
        boolean isRuolo = ((CheckBox) campi.get(L_RUOLO)).isSelected();
        LocalDate ingressoDocente = ((DatePicker) campi.get(L_INGRESSO_DOCENTE)).getValue();
        String dipartimento = DialogsParser.validaCampo(campi, L_DIPARTIMENTO);
        @SuppressWarnings("unchecked")
        ComboBox<String> comboQualifica = (ComboBox<String>) campi.get(L_QUALIFICA);
        String qualifica = comboQualifica.getValue();

        if (target == null) {
            // CREATE
            return new Docente(cf, nome, cognome, dataNascita, null,
                    codiceDocente, isRuolo, ingressoDocente, dipartimento, qualifica);
        } else {
            // UPDATE (rispetta i tuoi setter già esistenti)
            target.setCf(cf);
            target.setNome(nome);
            target.setCognome(cognome);
            target.setDataNascita(dataNascita != null ? dataNascita.toString() : null);
            target.setCodiceDocente(codiceDocente);
            target.setRuolo(isRuolo);
            target.setDataIngressoUniversitaDocente(ingressoDocente);
            target.setDipartimento(dipartimento);
            target.setQualifica(qualifica);
            return target;
        }
    }

    private void elimina(Docente d) {
        try {
            docenteService.deleteById(d.getId());
            refresh();
        } catch (Exception e) {
            Dialogs.showError("Errore", e.getMessage());
        }
    }
}
