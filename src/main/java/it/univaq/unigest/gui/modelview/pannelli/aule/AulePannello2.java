package it.univaq.unigest.gui.modelview.pannelli.aule;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.TabelleHelper;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.util.PdfHelper;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAulePannello2;

/**
 * Pannello grafico per la gestione delle aule.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuove aule</li>
 *   <li>Modificare aule esistenti</li>
 *   <li>Eliminare aule selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class AulePannello2 implements CrudPanel {

    /**
     * Lista delle Aule attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getAulaManager()}.
     */
    private static List<Aula> aule = Main.getAulaManager().getAll();

    /**
     * Builder grafico di {@link Aula}.
     */
    private static VistaConDettagliBuilder<Aula> builder = new VistaConDettagliBuilder<>(aule);

    /**
     * Costruisce e restituisce la vista principale per la gestione delle aule.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella delle aule correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare aule</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello aule.
     */
    public static VBox getView() {

        // Colonne principali
        LinkedHashMap<String, Function<Aula, String>> colonne = new LinkedHashMap<>();
        colonne.put("ID", Aula::getId);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.capienza"), a -> String.valueOf(a.getCapienza()));
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio"), aula -> Main.getEdificioManager().getNomeEdificioDaId(aula.getEdificio()));

        // Dettagli (uguali alle colonne)
        LinkedHashMap<String, Function<Aula, String>> dettagli = new LinkedHashMap<>(colonne);

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.aula.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.aula.descrizione") + " " + iscrizione.getId()));

        return builder.build(
                "Gestione Aule",
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Aula> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("string.aulePannello2.builder.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("string.aulePannello2.builder.header"),
                            campi -> {
                                try {
                                    String capienzaText = DialogsParser.validaCampo(campi, Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.capienza"));
                                    int capienza = Integer.parseInt(capienzaText);

                                    // Tabella corso di laurea SINGLE CHOICE!
                                    @SuppressWarnings("unchecked")
                                    TableView<Edificio> tableCDL = (TableView<Edificio>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio"));
                                    tableCDL.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                            // La scelta di elementi è solo 1
                                    Edificio cdlSelezionato = tableCDL.getSelectionModel().getSelectedItem();
                                    if (cdlSelezionato == null) {
                                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio.error"));
                                    }

                                    String cdlId = cdlSelezionato.getId();   // id del corso selezionatto

                                    return new Aula(String.valueOf(Main.getAulaManager().assegnaIndiceCorrente()), capienza, cdlId);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("La capienza deve essere un numero valido.");
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }
                            },
                            aula -> {
                                Main.getAulaManager().aggiungi(aula);
                                ricaricaInterfacciaGraficaAulePannello2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.builder.success"), Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.builder.messaggio"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.capienza"), new TextField());
                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio"), TabelleHelper.generaTabellaFkEdifici(SelectionMode.SINGLE));
                    dialogBuilder.mostra();

                },
                aula -> {
                    mostraDialogModificaAula(aula, builder);
                    System.out.println("Modifica Aula: " + aula.getId());
                },
                aula -> {
                    Main.getAulaManager().rimuovi(aula);
                    ricaricaInterfacciaGraficaAulePannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per un'aula selezionata sfruttando la logica di creazione.
     * @param aula L'aula a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista delle aule.
     */
    private static void mostraDialogModificaAula(Aula aula, VistaConDettagliBuilder<Aula> builder) {
        DialogBuilder<Aula> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.editor.titolo"),
                Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.editor.header"),
                campi -> {
                    int capienza = Integer.parseInt(((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.capienza"))).getText());

                    @SuppressWarnings("unchecked")
                    TableView<Edificio> tableEdifici = (TableView<Edificio>) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio"));
                    Edificio edificioSelezionato = tableEdifici.getSelectionModel().getSelectedItem();
                    if (edificioSelezionato == null) {
                        throw new IllegalArgumentException(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio.error2"));
                    }

                    // Aggiornamento oggetto aula
                    aula.setCapienza(capienza);
                    aula.setEdificio(edificioSelezionato.getId());

                    return aula;
                },
                a -> {
                    ricaricaInterfacciaGraficaAulePannello2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.builder.modifica.messaggio.successo"), Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.builder.modifica.messaggio"));
                }
        );

        // Pre-selezione edificio attuale
        TableView<Edificio> tableEdifici = TabelleHelper.generaTabellaFkEdifici(SelectionMode.SINGLE);
        tableEdifici.getItems().stream()
                .filter(e -> e.getId().equals(aula.getEdificio()))
                .findFirst()
                .ifPresent(e -> tableEdifici.getSelectionModel().select(e));

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.capienza"), new TextField(String.valueOf(aula.getCapienza())));
        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.aulePannello2.edificio"), tableEdifici);

        dialogBuilder.mostra();
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

        Aula selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }
        mostraDialogModificaAula(selezionato, builder);
        Main.getAulaManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        Aula selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }
        Main.getAulaManager().rimuovi(selezionato);
        ricaricaInterfacciaGraficaAulePannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista delle aule.
     *
     * @return il {@link VistaConDettagliBuilder} associato alle aule.
     */
    public static VistaConDettagliBuilder<Aula> getBuilder() {
        return builder;
    }

}
