package it.univaq.unigest.gui.modelview.pannelli.verbali;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.manager.exceptions.VerbaleConAppelloPresente;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Verbale;
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

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaVerbaliPannelli2;

/**
 * Pannello grafico per la gestione dei verbali.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi verbali</li>
 *   <li>Modificare verbali esistenti</li>
 *   <li>Eliminare verbali selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class VerbaliPannello2 implements CrudPanel {

    /**
     * Lista dei verbali attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getVerbaleManager()}.
     */
    private static List<Verbale> verbali = Main.getVerbaleManager().getAll();

    /**
     * Builder grafico di {@link Verbale}.
     */
    private static VistaConDettagliBuilder<Verbale> builder = new VistaConDettagliBuilder<>(verbali);

    /**
     * Costruisce e restituisce la vista principale per la gestione dei verbali.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella dei verbali correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare verbali</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello verbali.
     */
    public static VBox getView() {

        LinkedHashMap<String, Function<Verbale, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.id"), v -> v.getId() != null ? v.getId().toString() : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.appello"), Verbale::getAppelloId);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.dataChiusura"), v -> v.getDataChiusura() != null ? v.getDataChiusura().toString() : "");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura"), v -> v.getChiuso() ? "Sì" : "No");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato"), v -> v.getFirmato() ? "Sì" : "No");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.note"), Verbale::getNote);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.numeroEsami"), v -> v.getEsami() != null ? String.valueOf(v.getEsami().size()) : "0");

        LinkedHashMap<String, Function<Verbale, String>> dettagli = new LinkedHashMap<>(colonne);

        dettagli.put("Visualizza Appello", s -> "Visualizza Appello");
        builder.setLinkAction("Visualizza Appello", iscrizione -> mostraAppelloDaVerbale(iscrizione));

        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.gestioneVerbali"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Verbale> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.header"),
                            campi -> {
                                try {
                                    @SuppressWarnings("unchecked")
                                    TableView<Appello> tableAppelli = (TableView<Appello>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.appello"));
                                    tableAppelli.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                                    Appello appelloSelezionato = tableAppelli.getSelectionModel().getSelectedItem();
                                    if (appelloSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.appello.error"));
                                    }
                                    String appelloSelezionatoStr = String.valueOf(appelloSelezionato.getId());

                                    LocalDate dataChiusura = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.dataChiusura"))).getValue();
                                    boolean chiuso = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura"))).isSelected();
                                    boolean firmato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato"))).isSelected();
                                    String note = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.note"))).getText();


                                    return new Verbale(
                                            Main.getVerbaleManager().assegnaIndiceCorrente(),
                                            appelloSelezionatoStr, dataChiusura, chiuso, firmato, note, null
                                    );
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I CFU devono essere un numero valido.");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            verbale -> {
                                try {
                                    Main.getVerbaleManager().aggiungi(verbale);
                                }catch (VerbaleConAppelloPresente e){
                                    Dialogs.showError(
                                            Main.getParametrizzazioneHelper().getBundle().getString("alert.header.error"),
                                            e.getMessage()
                                    );
                                    return;
                                }
                                ricaricaInterfacciaGraficaVerbaliPannelli2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.success2"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.appello"), TabelleHelper.generaTabellaFkAppelli(SelectionMode.SINGLE));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.dataChiusura"), new DatePicker());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura")));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato")));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.note"), new TextField());
                    dialogBuilder.mostra();

                },
                verbale -> mostraDialogModificaVerbale(verbale, builder),
                verbale -> {
                    Main.getVerbaleManager().rimuovi(verbale);
                    ricaricaInterfacciaGraficaVerbaliPannelli2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un verbale selezionato sfruttando la logica di creazione.
     * @param verbale Il verbale a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista dei verbali
     */
    private static void mostraDialogModificaVerbale(Verbale verbale, VistaConDettagliBuilder<Verbale> builder) {
        DialogBuilder<Verbale> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.modifica.titolo"),
                Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.modifica.header"),
                campi -> {
                    @SuppressWarnings("unchecked")
                    TableView<Appello> tableAppelli = (TableView<Appello>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.appello"));
                    Appello appelloSelezionato = tableAppelli.getSelectionModel().getSelectedItem();
                    if (appelloSelezionato == null) throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.appello.error"));

                    LocalDate dataChiusura = ((DatePicker) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.dataChiusura"))).getValue();
                    boolean chiuso = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura"))).isSelected();
                    boolean firmato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato"))).isSelected();
                    String note = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.note"))).getText();

                    verbale.setAppelloId(String.valueOf(appelloSelezionato.getId()));
                    verbale.setDataChiusura(dataChiusura);
                    verbale.setChiuso(chiuso);
                    verbale.setFirmato(firmato);
                    verbale.setNote(note);

                    Verbale nuovoVerbale = new Verbale(
                            verbale.getId(),
                            String.valueOf(appelloSelezionato.getId()),
                            dataChiusura,
                            chiuso,
                            firmato,
                            note,
                            null
                    );

                    try {
                        Main.getVerbaleManager().aggiorna(nuovoVerbale);
                    }catch (VerbaleConAppelloPresente e){
                        Dialogs.showError(
                                Main.getParametrizzazioneHelper().getBundle().getString("alert.header.error"),
                                e.getMessage()
                        );
                        return null;
                    }

                    return verbale;
                },
                v -> {
                    ricaricaInterfacciaGraficaVerbaliPannelli2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.modifica.success"));
                }
        );

        TableView<Appello> tableAppelli = TabelleHelper.generaTabellaFkAppelli(SelectionMode.SINGLE);
        tableAppelli.getItems().stream()
                .filter(a -> String.valueOf(a.getId()).equals(verbale.getAppelloId()))
                .findFirst().ifPresent(a -> tableAppelli.getSelectionModel().select(a));

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.appello"), tableAppelli);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.dataChiusura"), new DatePicker(verbale.getDataChiusura()));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.chiusura")) {{ setSelected(verbale.getChiuso()); }});
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.firmato")) {{ setSelected(verbale.getFirmato()); }});
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.verbaliPannello2.note"), new TextField(verbale.getNote() != null ? verbale.getNote() : ""));

        dialogBuilder.mostra();
    }

    public static void mostraAppelloDaVerbale(Verbale verbale) {
        // 1) Recupera l'appello collegato al verbale
        Appello appello = Main.getAppelloManager().getAppelloDaVerbale(String.valueOf(verbale.getId()));

        // 2) Prepara la lista (anche se è solo un elemento)
        List<Appello> appelli = new ArrayList<>();
        if (appello != null) {
            appelli.add(appello);
        }

        // 3) Costruisci la finestra
        Stage stage = new Stage();
        TableView<Appello> table = new TableView<>();

        TableColumn<Appello, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Appello, String> colInsegnamento = new TableColumn<>("Insegnamento");
        colInsegnamento.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRidInsegnamento()));

        TableColumn<Appello, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getData().toString()));

        TableColumn<Appello, String> colOra = new TableColumn<>("Ora");
        colOra.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOra().toString()));

        TableColumn<Appello, String> colAula = new TableColumn<>("Aula");
        colAula.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRidAula() != null ? Main.getAulaManager().getAulaNomeDaId(data.getValue().getRidAula()) : "-"
        ));

        TableColumn<Appello, String> colDocente = new TableColumn<>("Docente");
        colDocente.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getRidDocente() != null ? Main.getDocenteManager().getGeneralitaDaCf(data.getValue().getRidDocente()) : "-"
        ));

        table.getColumns().addAll(colId, colInsegnamento, colData, colOra, colAula, colDocente);
        table.setItems(FXCollections.observableArrayList(appelli));

        VBox layout = new VBox(10, table);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Appello del Verbale ID: " + verbale.getId());
        stage.show();
    }

    /**
     * Apre la finestra di aggiunta studente (usa la stessa logica di getView()).
     */
    public static void apriDialogAggiungi() {
        // Riutilizziamo direttamente la logica del builder
        if (builder != null && builder.getAggiungiAction() != null) {
            builder.getAggiungiAction().run();
        }
    }

    /**
     * Modifica lo studente selezionato nella tabella.
     */
    public static void modificaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Verbale selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaVerbale(selezionato, builder);

        //
        Main.getVerbaleManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Verbale selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getVerbaleManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaVerbaliPannelli2();
    }


    /**
     * Restituisce il builder grafico utilizzato per costruire la vista dei verbali.
     *
     * @return il {@link VistaConDettagliBuilder} associato ai verbali.
     */
    public static VistaConDettagliBuilder<Verbale> getBuilder() {
        return builder;
    }
}