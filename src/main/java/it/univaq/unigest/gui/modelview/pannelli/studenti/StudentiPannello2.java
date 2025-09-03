package it.univaq.unigest.gui.modelview.pannelli.studenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.util.LocalDateUtil;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class StudentiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_CF = "CF";
    private static final String L_NOME = "Nome";
    private static final String L_COGNOME = "Cognome";
    private static final String L_DATA_NASCITA = "Data di Nascita";
    private static final String L_EMAIL = "Email";
    private static final String L_MATRICOLA = "Matricola";
    private static final String L_CORSO_DI_LAUREA = "Corso di Laurea";
    private static final String L_CFU = "CFU";
    private static final String L_MEDIA_PONDERATA = "Media Ponderata";
    private static final String L_MEDIA_ARITMETICA = "Media Aritmetica";
    private static final String L_DATA_IMMATRICOLAZIONE = "Data Immatricolazione";

    // Dipendenze
    private final StudenteService studenteService;

    // Loader Esterni
    private final Supplier<List<CorsoDiLaurea>> loadCorsi;
    private final Function<String, String> nomeCdlById;

    private final VistaConDettagliBuilder<Studente> builder;

    public StudentiPannello2(StudenteService studenteService,
                             Supplier<List<CorsoDiLaurea>> loadCorsi,
                             Function<String, String> nomeCdlById) {
        this.studenteService = studenteService;
        this.loadCorsi = loadCorsi;
        this.nomeCdlById = nomeCdlById;
        this.builder = new VistaConDettagliBuilder<>(studenteService.findAll());
    }

    // API CrudPanel
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Studenti",
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
        if (sel == null) { Dialogs.showError("Nessuna Selezione", "Seleziona uno studente"); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna Selezione", "Seleziona uno studente"); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(studenteService.findAll());
    }

    public VistaConDettagliBuilder<Studente> getBuilder() { return builder; }

    // Colonne / Dettagli
    private LinkedHashMap<String, Function<Studente, String>> colonne() {
        LinkedHashMap<String, Function<Studente, String>> columns = new LinkedHashMap<>();
        columns.put(L_CF, Studente::getCf);
        columns.put(L_NOME, Studente::getNome);
        columns.put(L_COGNOME, Studente::getCognome);
        columns.put(L_EMAIL, Studente::getEmail);
        columns.put(L_MATRICOLA, Studente::getMatricola);
        columns.put(L_CORSO_DI_LAUREA, s -> {
            String id = s.getCorsoDiLaurea();
            return id == null ? "" : nomeCdlById.apply(id);
        });
        columns.put(L_CFU, s -> s.getCfu() != null ? String.valueOf(s.getCfu()) : "");
        // fix: non invertiamo le medie
        columns.put(L_MEDIA_ARITMETICA, s -> s.getMediaAritmetica() != null ? String.format("%.2f", s.getMediaAritmetica()) : "");
        columns.put(L_MEDIA_PONDERATA, s -> s.getMediaPonderata() != null ? String.format("%.2f", s.getMediaPonderata()) : "");
        return columns;
    }

    private LinkedHashMap<String, Function<Studente, String>> dettagli() {
        LinkedHashMap<String, Function<Studente, String>> details = new LinkedHashMap<>(colonne());
        details.put(L_DATA_IMMATRICOLAZIONE, s -> s.getDataImmatricolazione() != null ? s.getDataImmatricolazione().toString() : "");
        details.put(L_DATA_NASCITA, Studente::getDataNascita);
        return details;
    }

    // Dialoghi CRUD
    public void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Studente",
                "Inserisci i dati dello studente",
                null,
                sCreato -> studenteService.create(sCreato),
                "Successo",
                "Studente aggiunto correttamente!"
        );
    }

    public void mostraDialogModifica(Studente studente) {
        mostraDialogoCrud(
                "Modifica Studente",
                "Modifica i dati dello studente",
                studente,
                sAgg -> studenteService.update(sAgg), // fix: update, non create
                "Successo",
                "Studente modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Studente iniziale,
                                   Function<Studente, Studente> persister,
                                   String successTitle,
                                   String successMsg) {
        DialogBuilder<Studente> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Studente target = estraiStudenteDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                s -> { refresh(); Dialogs.showInfo(successTitle, successMsg); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    private void configuraCampi(DialogBuilder<Studente> dialog, Studente iniziale) {
        // TextFields
        dialog.aggiungiCampo(L_CF, new TextField(iniziale != null ? iniziale.getCf() : ""));
        dialog.aggiungiCampo(L_NOME, new TextField(iniziale != null ? iniziale.getNome() : ""));
        dialog.aggiungiCampo(L_COGNOME, new TextField(iniziale != null ? iniziale.getCognome() : ""));
        dialog.aggiungiCampo(L_MATRICOLA, new TextField(iniziale != null ? iniziale.getMatricola() : ""));

        // DatePicker (fix: immatricolazione usa il suo campo)
        dialog.aggiungiCampo(L_DATA_NASCITA, new DatePicker(
                iniziale != null && iniziale.getDataNascita() != null ? LocalDate.parse(iniziale.getDataNascita()) : null
        ));
        dialog.aggiungiCampo(L_DATA_IMMATRICOLAZIONE, new DatePicker(
                iniziale != null ? iniziale.getDataImmatricolazione() : null
        ));

        // ComboBox dei Corsi di Laurea (niente null → niente NPE)
        ComboBox<CorsoDiLaurea> comboCdl = new ComboBox<>(FXCollections.observableArrayList(loadCorsi.get()));
        comboCdl.setPromptText("Seleziona corso");
        // mostra il nome nel menu e nel "bottone"
        comboCdl.setConverter(new StringConverter<>() {
            @Override public String toString(CorsoDiLaurea c) { return c == null ? "" : c.getNome(); }
            @Override public CorsoDiLaurea fromString(String s) { return null; }
        });
        comboCdl.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(CorsoDiLaurea c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? "" : c.getNome());
            }
        });

        // preselezione in modifica
        if (iniziale != null && iniziale.getCorsoDiLaurea() != null) {
            comboCdl.getItems().stream()
                    .filter(c -> iniziale.getCorsoDiLaurea().equals(c.getId()))
                    .findFirst()
                    .ifPresent(comboCdl::setValue);
        }

        dialog.aggiungiCampo(L_CORSO_DI_LAUREA, comboCdl);
    }

    private Studente estraiStudenteDaCampi(Map<String, Control> campi, Studente target) {
        // letture/validazioni
        String cf = DialogsParser.validaCampo(campi, L_CF);
        String nome = DialogsParser.validaCampo(campi, L_NOME);
        String cognome = DialogsParser.validaCampo(campi, L_COGNOME);
        LocalDate dataNascita = ((DatePicker) campi.get(L_DATA_NASCITA)).getValue();
        String matricola = DialogsParser.validaCampo(campi, L_MATRICOLA);
        LocalDate dataImmatricolazione = ((DatePicker) campi.get(L_DATA_IMMATRICOLAZIONE)).getValue();

        @SuppressWarnings("unchecked")
        ComboBox<CorsoDiLaurea> comboCdl = (ComboBox<CorsoDiLaurea>) campi.get(L_CORSO_DI_LAUREA);
        CorsoDiLaurea cdlSelezionato = comboCdl.getValue();
        if (cdlSelezionato == null) {
            throw new IllegalArgumentException("Devi selezionare un corso di laurea!");
        }
        String cdlId = cdlSelezionato.getId();

        if (target == null) {
            // create tramite setters (evitiamo costruttori lunghi/ordinati male)
            Studente s = new Studente();
            s.setCf(cf);
            s.setNome(nome);
            s.setCognome(cognome);
            s.setDataNascita(LocalDateUtil.toString(dataNascita));
            s.setMatricola(matricola);
            s.setCorsoDiLaurea(cdlId);
            s.setDataImmatricolazione(dataImmatricolazione);
            return s;
        } else {
            // update
            target.setCf(cf);
            target.setNome(nome);
            target.setCognome(cognome);
            target.setDataNascita(LocalDateUtil.toString(dataNascita));
            target.setMatricola(matricola);
            target.setCorsoDiLaurea(cdlId);
            target.setDataImmatricolazione(dataImmatricolazione);
            return target;
        }
    }

    private void elimina(Studente s) {
        try {
            studenteService.deleteById(s.getId());
            refresh();
        } catch (Exception e) {
            Dialogs.showError("Errore", e.getMessage());
        }
    }
}
