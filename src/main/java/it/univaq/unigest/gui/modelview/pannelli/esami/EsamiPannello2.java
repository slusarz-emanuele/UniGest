package it.univaq.unigest.gui.modelview.pannelli.esami;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.manager.exceptions.EsameConIdPresente;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.util.PdfHelper;
import it.univaq.unigest.util.loader.StudenteLoader;
import it.univaq.unigest.util.loader.VerbaleLoader;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    private final VistaConDettagliBuilder<Esame> builder;

    private final EsameService esameService;

    private final Supplier<List<Iscrizione>> loadIscrizioni;

    public EsamiPannello2(EsameService esameService,
                          Supplier<List<Iscrizione>> loadIscrizioni) {
        this.esameService = esameService;
        this.loadIscrizioni = loadIscrizioni;
        this.builder = new VistaConDettagliBuilder<>(esameService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Esame, String>> colonne = colonne();
        LinkedHashMap<String, Function<Esame, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Esporta in PDF", e -> "Esporta in PDF");
        builder.setLinkAction("Esporta in PDF", e ->
                PdfHelper.esportaEntita(e, "Esame " + e.getId(), "Esame_" + e.getId())
        );

        return builder.build(
                "Gestione Esami",
                colonne,
                dettagli,
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
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un esame."); return; }
        mostraDialogModifica(sel);
    }

    @Override
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione", "Seleziona un esame."); return; }
        elimina(sel);
    }

    @Override
    public void refresh() {
        builder.refresh(Main.getEsameManager().getAll());
    }

    public VistaConDettagliBuilder<Esame> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Esame, String>> colonne() {
        LinkedHashMap<String, Function<Esame, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Esame::getId);
        map.put(L_ISCRIZIONE, Esame::getIscrizioneId);
        map.put(L_VOTO, e -> e.getVoto() == null ? "" : e.getVoto().toString());
        map.put(L_LODE, e -> e.isLode() ? "Sì" : "No");
        map.put(L_RIFIUTATO, e -> e.isRifiutato() ? "Sì" : "No");
        map.put(L_VERBALIZZATO, e -> e.isVerbalizzato() ? "Sì" : "No");
        return map;
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        DialogBuilder<Esame> dialog = new DialogBuilder<>(
                "Nuovo Esame",
                "Inserisci i dati dell'esame",
                campi -> {
                    // Iscrizione
                    @SuppressWarnings("unchecked")
                    TableView<Iscrizione> tabIscr = (TableView<Iscrizione>) campi.get(L_ISCRIZIONE);
                    tabIscr.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Iscrizione iscr = tabIscr.getSelectionModel().getSelectedItem();
                    if (iscr == null) throw new IllegalArgumentException("Seleziona un'iscrizione.");

                    // Voto
                    @SuppressWarnings("unchecked")
                    ComboBox<Double> cbVoto = (ComboBox<Double>) campi.get(L_VOTO);
                    Double votoSel = cbVoto.getValue();
                    double voto = votoSel != null ? votoSel : 0.0;

                    boolean lode = ((CheckBox) campi.get(L_LODE)).isSelected();
                    boolean rifiutato = ((CheckBox) campi.get(L_RIFIUTATO)).isSelected();
                    boolean verbalizzato = ((CheckBox) campi.get(L_VERBALIZZATO)).isSelected();

                    Esame nuovo = new Esame(
                            String.valueOf(Main.getEsameManager().assegnaIndiceCorrente()),
                            String.valueOf(iscr.getId()),
                            voto,
                            lode,
                            rifiutato,
                            verbalizzato
                    );
                    Main.getEsameManager().aggiungi(nuovo);

                    // aggiorna relazioni
                    StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
                    VerbaleLoader.caricaEsamiPerOgniVerbale(Main.getVerbaleManager(), Main.getIscrizioneManager(), Main.getEsameManager());

                    return nuovo;
                },
                e -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Esame aggiunto con successo!");
                }
        );

        dialog.aggiungiCampo(L_ISCRIZIONE, generaTabellaIscrizioni());
        dialog.aggiungiCampo(L_VOTO, generaComboVoti(null));
        dialog.aggiungiCampo(L_LODE, new CheckBox(L_LODE));
        dialog.aggiungiCampo(L_RIFIUTATO, new CheckBox(L_RIFIUTATO));
        dialog.aggiungiCampo(L_VERBALIZZATO, new CheckBox(L_VERBALIZZATO));
        dialog.mostra();
    }

    private void mostraDialogModifica(Esame esame) {
        DialogBuilder<Esame> dialog = new DialogBuilder<>(
                "Modifica Esame",
                "Aggiorna i dati dell'esame",
                campi -> {
                    @SuppressWarnings("unchecked")
                    TableView<Iscrizione> tabIscr = (TableView<Iscrizione>) campi.get(L_ISCRIZIONE);
                    Iscrizione iscr = tabIscr.getSelectionModel().getSelectedItem();
                    if (iscr == null) throw new IllegalArgumentException("Seleziona un'iscrizione.");

                    @SuppressWarnings("unchecked")
                    ComboBox<Double> cbVoto = (ComboBox<Double>) campi.get(L_VOTO);
                    Double votoSel = cbVoto.getValue();
                    double voto = votoSel != null ? votoSel : 0.0;

                    boolean lode = ((CheckBox) campi.get(L_LODE)).isSelected();
                    boolean rifiutato = ((CheckBox) campi.get(L_RIFIUTATO)).isSelected();
                    boolean verbalizzato = ((CheckBox) campi.get(L_VERBALIZZATO)).isSelected();

                    Esame aggiornato = new Esame(
                            esame.getId(),
                            String.valueOf(iscr.getId()),
                            voto,
                            lode,
                            rifiutato,
                            verbalizzato
                    );

                    try {
                        Main.getEsameManager().aggiorna(aggiornato);
                    } catch (EsameConIdPresente ex) {
                        Dialogs.showError("Errore", ex.getMessage());
                        return null;
                    }

                    // aggiorna relazioni
                    StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
                    VerbaleLoader.caricaEsamiPerOgniVerbale(Main.getVerbaleManager(), Main.getIscrizioneManager(), Main.getEsameManager());

                    return aggiornato;
                },
                e -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Esame modificato con successo!");
                }
        );

        // Pre-popola controlli
        TableView<Iscrizione> tabIscr = generaTabellaIscrizioni();
        tabIscr.getItems().stream()
                .filter(i -> String.valueOf(i.getId()).equals(esame.getIscrizioneId()))
                .findFirst().ifPresent(i -> tabIscr.getSelectionModel().select(i));

        ComboBox<Double> cbVoto = generaComboVoti(esame.getVoto());

        CheckBox cbLode = new CheckBox(L_LODE); cbLode.setSelected(esame.isLode());
        CheckBox cbRif  = new CheckBox(L_RIFIUTATO); cbRif.setSelected(esame.isRifiutato());
        CheckBox cbVerb = new CheckBox(L_VERBALIZZATO); cbVerb.setSelected(esame.isVerbalizzato());

        dialog.aggiungiCampo(L_ID, new TextField(esame.getId()) {{ setEditable(false); }});
        dialog.aggiungiCampo(L_ISCRIZIONE, tabIscr);
        dialog.aggiungiCampo(L_VOTO, cbVoto);
        dialog.aggiungiCampo(L_LODE, cbLode);
        dialog.aggiungiCampo(L_RIFIUTATO, cbRif);
        dialog.aggiungiCampo(L_VERBALIZZATO, cbVerb);
        dialog.mostra();
    }

    private void elimina(Esame e) {
        Main.getEsameManager().rimuovi(e);
        refresh();
        // aggiorna relazioni dopo delete
        StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
        VerbaleLoader.caricaEsamiPerOgniVerbale(Main.getVerbaleManager(), Main.getIscrizioneManager(), Main.getEsameManager());
    }

    // ===== Supporto UI =====
    private TableView<Iscrizione> generaTabellaIscrizioni() {
        TableView<Iscrizione> table = new TableView<>();
        table.setPrefHeight(220);

        TableColumn<Iscrizione, String> c1 = new TableColumn<>("ID");
        c1.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getId())));

        TableColumn<Iscrizione, String> c2 = new TableColumn<>("Studente CF");
        c2.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getRidStudenteCf()));

        TableColumn<Iscrizione, String> c3 = new TableColumn<>("Appello ID");
        c3.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(String.valueOf(d.getValue().getRidAppello())));

        table.getColumns().addAll(c1, c2, c3);
        table.setItems(FXCollections.observableArrayList(Main.getIscrizioneManager().getAll()));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        return table;
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
