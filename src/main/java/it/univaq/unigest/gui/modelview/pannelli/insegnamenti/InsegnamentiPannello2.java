package it.univaq.unigest.gui.modelview.pannelli.insegnamenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TableMiniFactory;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.InsegnamentoService;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class InsegnamentiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID       = "ID";
    private static final String L_NOME     = "Nome";
    private static final String L_CFU      = "CFU";
    private static final String L_CDL      = "Corso di Laurea";
    private static final String L_ANNO     = "Anno";
    private static final String L_SEMESTRE = "Semestre";
    private static final String L_DOCENTI  = "Docenti";

    // Dipendenze
    private final InsegnamentoService insegnamentoService;

    // Loader esterni
    private final Supplier<List<CorsoDiLaurea>> loadCorsi;
    private final Supplier<List<Docente>>       loadDocenti;

    private final VistaConDettagliBuilder<Insegnamento> builder;

    public InsegnamentiPannello2(InsegnamentoService insegnamentoService,
                                 Supplier<List<CorsoDiLaurea>> loadCorsi,
                                 Supplier<List<Docente>> loadDocenti) {
        this.insegnamentoService = insegnamentoService;
        this.loadCorsi   = loadCorsi;
        this.loadDocenti = loadDocenti;
        this.builder = new VistaConDettagliBuilder<>(insegnamentoService.findAll());
    }

    // Blocchiamo il costruttore di default
    private InsegnamentiPannello2() {
        this.insegnamentoService = null;
        this.loadCorsi = null;
        this.loadDocenti = null;
        this.builder = null;
    }

    // ===== CrudPanel API =====
    @Override
    public VBox getView() {
        return builder.build(
                "Gestione Insegnamenti",
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un insegnamento."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un insegnamento."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(insegnamentoService.findAll());
    }

    public VistaConDettagliBuilder<Insegnamento> getBuilder() { return builder; }

    // ===== Colonne / Dettagli =====
    private LinkedHashMap<String, Function<Insegnamento, String>> colonne() {
        LinkedHashMap<String, Function<Insegnamento, String>> map = new LinkedHashMap<>();
        map.put(L_ID,    Insegnamento::getId);
        map.put(L_NOME,  Insegnamento::getNome);
        map.put(L_CFU,   i -> i.getCfu() != null ? String.valueOf(i.getCfu()) : "");
        map.put(L_CDL,   i -> nomeCorsoById(i.getCorsoDiLaureaId()));
        map.put(L_ANNO,  i -> i.getAnno() != null ? String.valueOf(i.getAnno()) : "");
        map.put(L_SEMESTRE, i -> i.getSemestre() != null ? String.valueOf(i.getSemestre()) : "");
        map.put(L_DOCENTI,  i -> i.getDocenti() != null ? String.join(", ", i.getDocenti()) : "");
        return map;
    }

    private LinkedHashMap<String, Function<Insegnamento, String>> dettagli() {
        return new LinkedHashMap<>(colonne());
    }

    private String nomeCorsoById(String id) {
        if (id == null) return "";
        return loadCorsi.get().stream()
                .filter(c -> id.equals(c.getId()))
                .map(CorsoDiLaurea::getNome)
                .findFirst()
                .orElse(id); // fallback: mostra l'id se non trovato
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        mostraDialogoCrud(
                "Nuovo Insegnamento",
                "Inserisci i dati del nuovo insegnamento",
                null,
                iCreato -> insegnamentoService.create(iCreato),
                "Successo",
                "Insegnamento aggiunto correttamente!"
        );
    }

    private void mostraDialogModifica(Insegnamento ins) {
        mostraDialogoCrud(
                "Modifica Insegnamento",
                "Modifica i dati dell'insegnamento",
                ins,
                iAgg -> insegnamentoService.update(iAgg),
                "Successo",
                "Insegnamento modificato correttamente!"
        );
    }

    private void mostraDialogoCrud(String titolo,
                                   String header,
                                   Insegnamento iniziale,
                                   Function<Insegnamento, Insegnamento> persister,
                                   String successTitle,
                                   String successMessage) {
        DialogBuilder<Insegnamento> dialog = new DialogBuilder<>(
                titolo,
                header,
                campi -> {
                    Insegnamento target = estraiInsegnamentoDaCampi(campi, iniziale);
                    return persister.apply(target);
                },
                i -> { refresh(); Dialogs.showInfo(successTitle, successMessage); }
        );

        configuraCampi(dialog, iniziale);
        dialog.mostra();
    }

    // ===== Configurazione campi (ordine: nome → cfu → corso → anno → semestre → docenti) =====
    private void configuraCampi(DialogBuilder<Insegnamento> dialog, Insegnamento iniziale) {
        // Nome
        TextField tfNome = new TextField(iniziale != null ? coalesce(iniziale.getNome()) : "");

        // CFU
        TextField tfCfu = new TextField(iniziale != null && iniziale.getCfu() != null ? String.valueOf(iniziale.getCfu()) : "");

        // Corso di Laurea (tabellina single-select)
        TableView<CorsoDiLaurea> tabCdl = TableMiniFactory.creaTabella(
                loadCorsi,
                SelectionMode.SINGLE,
                220,
                new LinkedHashMap<>() {{
                    put("ID",   CorsoDiLaurea::getId);
                    put("Nome", CorsoDiLaurea::getNome);
                }}
        );
        if (iniziale != null && iniziale.getCorsoDiLaureaId() != null) {
            tabCdl.getItems().stream()
                    .filter(c -> c.getId().equals(iniziale.getCorsoDiLaureaId()))
                    .findFirst().ifPresent(c -> tabCdl.getSelectionModel().select(c));
        }

        // Anno
        TextField tfAnno = new TextField(iniziale != null && iniziale.getAnno() != null ? String.valueOf(iniziale.getAnno()) : "");

        // Semestre (1 o 2)
        ComboBox<Integer> cbSemestre = new ComboBox<>(FXCollections.observableArrayList(1, 2));
        if (iniziale != null && iniziale.getSemestre() != null) cbSemestre.setValue(iniziale.getSemestre());

        // Docenti (multi-select)
        TableView<Docente> tabDoc = TableMiniFactory.creaTabella(
                loadDocenti,
                SelectionMode.MULTIPLE,
                260,
                new LinkedHashMap<>() {{
                    put("CF",      Docente::getCf);
                    put("Nome",    Docente::getNome);
                    put("Cognome", Docente::getCognome);
                }}
        );
        // Preselezione docenti in modifica
        if (iniziale != null && iniziale.getDocenti() != null) {
            var setDoc = new HashSet<>(iniziale.getDocenti());
            tabDoc.getItems().forEach(d -> {
                // selezioniamo se la stringa salvata contiene il CF oppure Nome Cognome
                boolean match = setDoc.stream().anyMatch(s ->
                        (d.getCf() != null && s.contains(d.getCf())) ||
                                (d.getNome() != null && d.getCognome() != null && s.contains(d.getNome()) && s.contains(d.getCognome()))
                );
                if (match) tabDoc.getSelectionModel().select(d);
            });
        }

        // Aggiunta secondo l'ordine richiesto
        dialog.aggiungiCampo(L_NOME, tfNome);
        dialog.aggiungiCampo(L_CFU, tfCfu);
        dialog.aggiungiCampo(L_CDL, tabCdl);
        dialog.aggiungiCampo(L_ANNO, tfAnno);
        dialog.aggiungiCampo(L_SEMESTRE, cbSemestre);
        dialog.aggiungiCampo(L_DOCENTI, tabDoc);
    }

    // ===== Lettura/validazione =====
    private Insegnamento estraiInsegnamentoDaCampi(Map<String, Control> campi, Insegnamento target) {
        String nome = DialogsParser.validaCampo(campi, L_NOME);

        Integer cfu;
        try {
            cfu = Integer.parseInt(DialogsParser.validaCampo(campi, L_CFU));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("CFU non valido.");
        }

        @SuppressWarnings("unchecked")
        TableView<CorsoDiLaurea> tabCdl = (TableView<CorsoDiLaurea>) campi.get(L_CDL);
        CorsoDiLaurea cdl = tabCdl.getSelectionModel().getSelectedItem();
        if (cdl == null) throw new IllegalArgumentException("Seleziona un corso di laurea.");

        Integer anno;
        try {
            anno = Integer.parseInt(DialogsParser.validaCampo(campi, L_ANNO));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Anno non valido.");
        }

        @SuppressWarnings("unchecked")
        ComboBox<Integer> cbSem = (ComboBox<Integer>) campi.get(L_SEMESTRE);
        Integer semestre = cbSem.getValue();
        if (semestre == null) throw new IllegalArgumentException("Seleziona un semestre.");

        @SuppressWarnings("unchecked")
        TableView<Docente> tabDoc = (TableView<Docente>) campi.get(L_DOCENTI);
        List<Docente> selDoc = new ArrayList<>(tabDoc.getSelectionModel().getSelectedItems());
        if (selDoc.isEmpty()) throw new IllegalArgumentException("Seleziona almeno un docente.");

        // Salviamo come "Nome Cognome (CF)"
        List<String> docenti = selDoc.stream()
                .map(d -> (coalesce(d.getNome()) + " " + coalesce(d.getCognome()) + " (" + coalesce(d.getCf()) + ")").trim())
                .collect(Collectors.toList());

        if (target == null) {
            return new Insegnamento(
                    null,         // id null → verrà assegnato dal repository
                    nome,
                    cfu,
                    cdl.getId(),
                    docenti,
                    anno,
                    semestre
            );
        } else {
            target.setNome(nome);
            target.setCfu(cfu);
            target.setCorsoDiLaureaId(cdl.getId());
            target.setAnno(anno);
            target.setSemestre(semestre);
            target.setDocenti(docenti);
            return target;
        }
    }

    private void elimina(Insegnamento i) {
        insegnamentoService.deleteById(i.getId());
        refresh();
    }

    private static String coalesce(String s) { return s == null ? "" : s; }
}
