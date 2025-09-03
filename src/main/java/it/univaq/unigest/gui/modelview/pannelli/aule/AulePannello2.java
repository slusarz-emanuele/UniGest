package it.univaq.unigest.gui.modelview.pannelli.aule;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.service.AulaService;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.util.PdfHelper;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
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

    public AulePannello2(AulaService aulaService,
                         Supplier<List<Edificio>> loadEdifici) {
        this.aulaService = aulaService;
        this.loadEdifici = loadEdifici;
        this.builder = new VistaConDettagliBuilder<>(aulaService.findAll());
    }

    @Override
    public VBox getView() {
        LinkedHashMap<String, Function<Aula, String>> colonne = colonne();

        LinkedHashMap<String, Function<Aula, String>> dettagli = new LinkedHashMap<>(colonne);
        dettagli.put("Esporta in PDF", a -> "Esporta in PDF");
        builder.setLinkAction("Esporta in PDF", a ->
                PdfHelper.esportaEntita(a, "Aula " + a.getId(), "Aula_" + a.getId())
        );

        return builder.build(
                "Gestione Aule",
                colonne,
                dettagli,
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
        builder.refresh(Main.getAulaManager().getAll());
    }

    public VistaConDettagliBuilder<Aula> getBuilder() { return builder; }

    // ===== Colonne =====
    private LinkedHashMap<String, Function<Aula, String>> colonne() {
        LinkedHashMap<String, Function<Aula, String>> map = new LinkedHashMap<>();
        map.put(L_ID, Aula::getId);
        map.put(L_CAPIENZA, a -> Integer.toString(a.getCapienza()));
        map.put(L_EDIFICIO, a -> Main.getEdificioManager().getNomeEdificioDaId(a.getEdificio()));
        return map;
    }

    // ===== Dialoghi CRUD =====
    private void apriDialogAggiungi() {
        DialogBuilder<Aula> dialog = new DialogBuilder<>(
                "Nuova Aula",
                "Inserisci i dati dell'aula",
                campi -> {
                    // capienza
                    String capienzaText = DialogsParser.validaCampo(campi, L_CAPIENZA);
                    int capienza = Integer.parseInt(capienzaText);

                    // edificio (tabella single-selection)
                    @SuppressWarnings("unchecked")
                    TableView<Edificio> tableEdifici = (TableView<Edificio>) campi.get(L_EDIFICIO);
                    tableEdifici.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                    Edificio sel = tableEdifici.getSelectionModel().getSelectedItem();
                    if (sel == null) throw new IllegalArgumentException("Seleziona un edificio.");

                    Aula nuova = new Aula(
                            String.valueOf(Main.getAulaManager().assegnaIndiceCorrente()),
                            capienza,
                            sel.getId()
                    );
                    Main.getAulaManager().aggiungi(nuova);
                    return nuova;
                },
                a -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Aula aggiunta con successo!");
                }
        );

        dialog.aggiungiCampo(L_CAPIENZA, new TextField());
        dialog.aggiungiCampo(L_EDIFICIO, TabelleHelper.generaTabellaFkEdifici(SelectionMode.SINGLE));
        dialog.mostra();
    }

    private void mostraDialogModificaAula(Aula aula) {
        DialogBuilder<Aula> dialog = new DialogBuilder<>(
                "Modifica Aula",
                "Aggiorna i dati dell'aula",
                campi -> {
                    int capienza = Integer.parseInt(((TextField) campi.get(L_CAPIENZA)).getText());

                    @SuppressWarnings("unchecked")
                    TableView<Edificio> tableEdifici = (TableView<Edificio>) campi.get(L_EDIFICIO);
                    Edificio sel = tableEdifici.getSelectionModel().getSelectedItem();
                    if (sel == null) throw new IllegalArgumentException("Seleziona un edificio.");

                    aula.setCapienza(capienza);
                    aula.setEdificio(sel.getId());
                    Main.getAulaManager().aggiorna(aula);
                    return aula;
                },
                a -> {
                    refresh();
                    Dialogs.showInfo("Successo", "Aula modificata con successo!");
                }
        );

        // Pre-compilazione
        TableView<Edificio> tableEdifici = TabelleHelper.generaTabellaFkEdifici(SelectionMode.SINGLE);
        tableEdifici.getItems().stream()
                .filter(e -> e.getId().equals(aula.getEdificio()))
                .findFirst()
                .ifPresent(e -> tableEdifici.getSelectionModel().select(e));

        dialog.aggiungiCampo(L_ID, new TextField(aula.getId()) {{ setEditable(false); }});
        dialog.aggiungiCampo(L_CAPIENZA, new TextField(Integer.toString(aula.getCapienza())));
        dialog.aggiungiCampo(L_EDIFICIO, tableEdifici);

        dialog.mostra();
    }

    private void elimina(Aula a) {
        Main.getAulaManager().rimuovi(a);
        refresh();
    }
}
