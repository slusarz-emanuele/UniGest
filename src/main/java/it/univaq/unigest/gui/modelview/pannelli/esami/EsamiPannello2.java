package it.univaq.unigest.gui.modelview.pannelli.esami;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.manager.exceptions.EsameConIdPresente;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
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

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaEsamiPannello2;

/**
 * Pannello grafico per la gestione degli esami.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi esami</li>
 *   <li>Modificare esami esistenti</li>
 *   <li>Eliminare esami selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class EsamiPannello2 {

    /**
     * Lista degli esami attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getEsameManager()}.
     */
    private static List<Esame> esami = Main.getEsameManager().getAll();

    /**
     * Builder grafico di {@link Esame}.
     */
    private static VistaConDettagliBuilder<Esame> builder = new VistaConDettagliBuilder<>(esami);

    /**
     * Costruisce e restituisce la vista principale per la gestione degli esami.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella degli esami correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare esami</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello esami.
     */
    public static VBox getView() {

        // Colonne principali per tabella
        LinkedHashMap<String, Function<Esame, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.id"), Esame::getId);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.iscrizioneId"), Esame::getIscrizioneId);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.voto"), e -> e.getVoto() == null ? "" : e.getVoto().toString());
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"), e -> e.isLode() ? "Si" : "No");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"), e -> e.isRifiutato() ? "Si" : "No");
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"), e -> e.isVerbalizzato() ? "Si" : "No");

        // Dettagli (aggiungo eventuali altre info)
        LinkedHashMap<String, Function<Esame, String>> dettagli = new LinkedHashMap<>(colonne);

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.esame.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.esame.descrizione") + " " + iscrizione.getId()));

        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.titolo"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Esame> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.header"),
                            campi -> {
                                try {

                                    // Iscrizioni
                                    @SuppressWarnings("unchecked")
                                    TableView<Iscrizione> tableIscizioni = (TableView<Iscrizione>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.iscrizioneId"));
                                    tableIscizioni.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                            // La scelta di elementi è solo 1
                                    Iscrizione iscrizioneSelezionata = tableIscizioni.getSelectionModel().getSelectedItem();
                                    if (iscrizioneSelezionata == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.iscrizioneId.errore"));
                                    }
                                    String iscrizioneSelezionataStr = String.valueOf(iscrizioneSelezionata.getId());   // id del corso selezionatto

                                    // Voto
                                    @SuppressWarnings("unchecked")
                                    ComboBox<Double> comboVoto = (ComboBox<Double>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.voto"));
                                    Double votoSelezionato = comboVoto.getValue();
                                    double voto = votoSelezionato != null ? votoSelezionato : 0.0;


                                    boolean lode = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"))).isSelected();
                                    boolean rifiutato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"))).isSelected();
                                    boolean verbalizzato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"))).isSelected();

                                    return new Esame(String.valueOf(Main.getEsameManager().assegnaIndiceCorrente()), iscrizioneSelezionataStr, voto, lode, rifiutato, verbalizzato);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("I CFU devono essere un numero valido.");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            esame -> {
                                try{
                                    Main.getEsameManager().aggiungi(esame);
                                }catch (EsameConIdPresente e){
                                    Dialogs.showError(
                                            Main.getParametrizzazioneHelper().getBundle().getString("alert.header.error"),
                                            e.getMessage()
                                    );
                                    return;
                                }
                                ricaricaInterfacciaGraficaEsamiPannello2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.success2"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.iscrizioneId"), TabelleHelper.generaTabellaFkIscrizioni(SelectionMode.SINGLE));

                    // Genera la lista dei voti con step 0.25
                    List<Double> votiDisponibili = new ArrayList<>();
                    for (double v = 0; v <= 30; v += 0.25) {
                        votiDisponibili.add(v);
                    }

                    // ComboBox con valori di tipo Double
                    ComboBox<Double> comboVoto = new ComboBox<>(FXCollections.observableArrayList(votiDisponibili));
                    comboVoto.setPrefWidth(150);
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.voto"), comboVoto);

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode")));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato")));
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"), new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato")));
                    dialogBuilder.mostra();

                    StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
                    VerbaleLoader.caricaEsamiPerOgniVerbale(Main.getVerbaleManager(), Main.getIscrizioneManager(), Main.getEsameManager());
                    },
                esame -> {
                    // TODO: modifica esame
                    System.out.println("Modifica Esame: " + esame.getId());
                    mostraDialogModificaEsame(esame, builder);
                },
                esame -> {
                    Main.getEsameManager().rimuovi(esame);
                    ricaricaInterfacciaGraficaEsamiPannello2();
                    StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
                }
        );
    }

    /**
     * Crea una finestra di modifica esami sfruttando la logica di creazione.
     * @param esame L'esame a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista degli esami.
     */
    private static void mostraDialogModificaEsame(Esame esame, VistaConDettagliBuilder<Esame> builder) {
        DialogBuilder<Esame> dialogBuilder = new DialogBuilder<>(
                "Modifica Esame",
                "Modifica i dati dell'esame",
                campi -> {
                    // Tabella Iscrizioni
                    @SuppressWarnings("unchecked")
                    TableView<Iscrizione> tableIscrizioni = (TableView<Iscrizione>) campi.get("Iscrizione ID");
                    Iscrizione iscrizioneSelezionata = tableIscrizioni.getSelectionModel().getSelectedItem();
                    if (iscrizioneSelezionata == null) {
                        throw new IllegalArgumentException("Devi selezionare una Iscrizione!");
                    }

                    // Voto
                    @SuppressWarnings("unchecked")
                    ComboBox<Double> comboVoto = (ComboBox<Double>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.voto"));
                    Double votoSelezionato = comboVoto.getValue();
                    double voto = votoSelezionato != null ? votoSelezionato : 0.0;

                    boolean lode = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"))).isSelected();
                    boolean rifiutato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"))).isSelected();
                    boolean verbalizzato = ((CheckBox) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"))).isSelected();

                    Esame esameAggiornato=new Esame(
                            esame.getId(),
                            String.valueOf(iscrizioneSelezionata.getId()),
                            voto,
                            lode,
                            rifiutato,
                            verbalizzato);


                    try{
                        Main.getEsameManager().aggiorna(esameAggiornato);
                    } catch (EsameConIdPresente e){
                        Dialogs.showError(
                                Main.getParametrizzazioneHelper().getBundle().getString("alert.header.error"),
                                e.getMessage()
                        );
                        return null;
                    }

                    return esame;
                },
                e -> {
                    ricaricaInterfacciaGraficaEsamiPannello2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.editor.success2"));
                }
        );

        // Tabella iscrizioni con selezione predefinita
        TableView<Iscrizione> tableIscrizioni = TabelleHelper.generaTabellaFkIscrizioni(SelectionMode.SINGLE);
        tableIscrizioni.getItems().stream()
                .filter(i -> String.valueOf(i.getId()).equals(esame.getIscrizioneId()))
                .findFirst().ifPresent(i -> tableIscrizioni.getSelectionModel().select(i));

        // Combo voti con step 0.25
        List<Double> votiDisponibili = new ArrayList<>();
        for (double v = 0; v <= 30; v += 0.25) {
            votiDisponibili.add(v);
        }
        ComboBox<Double> comboVoto = new ComboBox<>(FXCollections.observableArrayList(votiDisponibili));
        comboVoto.setValue(esame.getVoto());

        // CheckBox preimpostati
        CheckBox checkLode = new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"));
        checkLode.setSelected(esame.isLode());

        CheckBox checkRifiutato = new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"));
        checkRifiutato.setSelected(esame.isRifiutato());

        CheckBox checkVerbalizzato = new CheckBox(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"));
        checkVerbalizzato.setSelected(esame.isVerbalizzato());

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.iscrizioneId"), tableIscrizioni);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.voto"), comboVoto);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.lode"), checkLode);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.rifiutato"), checkRifiutato);
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.edificiPannello2.verbalizzato"), checkVerbalizzato);

        dialogBuilder.mostra();

        // Aggiornamento relazioni dopo modifica
        StudenteLoader.caricaEsamiPerOgniStudente(Main.getStudenteManager(), Main.getIscrizioneManager(), Main.getEsameManager());
        VerbaleLoader.caricaEsamiPerOgniVerbale(Main.getVerbaleManager(), Main.getIscrizioneManager(), Main.getEsameManager());
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
        Esame selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaEsame(selezionato, builder);

        //
        Main.getEsameManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Esame selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getEsameManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaEsamiPannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista degli esami.
     *
     * @return il {@link VistaConDettagliBuilder} associato agli esami.
     */
    public static VistaConDettagliBuilder<Esame> getBuilder() {
        return builder;
    }
}
