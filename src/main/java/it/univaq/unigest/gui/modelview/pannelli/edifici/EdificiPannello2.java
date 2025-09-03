package it.univaq.unigest.gui.modelview.pannelli.edifici;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.EdificioService;
import it.univaq.unigest.util.PdfHelper;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class EdificiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_ID   = "ID";
    private static final String L_NOME = "Nome";

    // Dipendenze
    private final VistaConDettagliBuilder<Edificio> builder;
    private final EdificioService edificioService;

    public EdificiPannello2(EdificioService edificioService) {
        this.edificioService = edificioService;
        this.builder = new VistaConDettagliBuilder<>(edificioService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Edificio, String>> colonne = colonne();

        LinkedHashMap<String, Function<Edificio, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Esporta in PDF", e -> "Esporta in PDF");
        builder.setLinkAction("Esporta in PDF", e ->
                PdfHelper.esportaEntita(e, "Edificio " + e.getId(), "Edificio_" + e.getId())
        );

        return builder.build(
                "Gestione Edifici",
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
        builder.refresh(Main.getEdificioManager().getAll());
    }

    public VistaConDettagliBuilder<Edificio> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Edificio, String>> colonne() {
        LinkedHashMap<String, Function<Edificio, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Edificio::getId);
        map.put(L_NOME, Edificio::getNome);
        return map;
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        DialogBuilder<Edificio> dialog = new DialogBuilder<>(
                "Nuovo Edificio",
                "Inserisci i dati dell'edificio",
                campi -> {
                    String nome = DialogsParser.validaCampo(campi, L_NOME);
                    Edificio nuovo = new Edificio(
                            String.valueOf(Main.getEdificioManager().assegnaIndiceCorrente()),
                            nome
                    );
                    Main.getEdificioManager().aggiungi(nuovo);
                    return nuovo;
                },
                e -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Edificio aggiunto con successo!");
                }
        );

        dialog.aggiungiCampo(L_NOME, new TextField());
        dialog.mostra();
    }

    private void mostraDialogModifica(Edificio edificio) {
        DialogBuilder<Edificio> dialog = new DialogBuilder<>(
                "Modifica Edificio",
                "Aggiorna i dati dell'edificio",
                campi -> {
                    String nome = ((TextField) campi.get(L_NOME)).getText();
                    edificio.setNome(nome);
                    Main.getEdificioManager().aggiorna(edificio);
                    return edificio;
                },
                e -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Edificio modificato con successo!");
                }
        );

        dialog.aggiungiCampo(L_ID, new TextField(edificio.getId()) {{ setEditable(false); }});
        dialog.aggiungiCampo(L_NOME, new TextField(edificio.getNome()));
        dialog.mostra();
    }

    private void elimina(Edificio e) {
        Main.getEdificioManager().rimuovi(e);
        refresh();
    }
}
