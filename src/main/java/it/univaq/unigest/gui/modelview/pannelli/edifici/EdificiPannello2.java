package it.univaq.unigest.gui.modelview.pannelli.edifici;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.modelview.pannelli.exceptions.CampoRichiestoVuoto;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.util.PdfHelper;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2;
import static it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaEdificiPannello2;

/**
 * Pannello grafico per la gestione degli edifici.
 * <p>
 * Fornisce una vista tabellare con dettagli aggiuntivi e funzioni per:
 * <ul>
 *   <li>Creare nuovi edifici</li>
 *   <li>Modificare edifici esistenti</li>
 *   <li>Eliminare edifici selezionati</li>
 * </ul>
 * Utilizza {@link VistaConDettagliBuilder} per costruire la vista e
 * {@link DialogBuilder} per la gestione delle finestre di input.
 * </p>
 */
public class EdificiPannello2 implements CrudPanel {

    /**
     * Lista degli edifici attualmente disponibili.
     * <p>
     * Inizializzata all'avvio da {@link Main#getEdificioManager()}.
     */
    private static List<Edificio> edifici = Main.getEdificioManager().getAll();

    /**
     * Builder grafico di {@link Edificio}.
     */
    private static VistaConDettagliBuilder<Edificio> builder = new VistaConDettagliBuilder<>(edifici);

    /**
     * Costruisce e restituisce la vista principale per la gestione degli edifici.
     * <p>
     * La vista comprende:
     * <ul>
     *     <li>Tabella degli edifici correnti con colonne base e dettagli aggiuntivi</li>
     *     <li>Azioni per creare, modificare ed eliminare edifici</li>
     * </ul>
     *
     * @return un oggetto {@link VBox} contenente la vista completa del pannello edifici.
     */
    public static VBox getView() {

        LinkedHashMap<String, Function<Edificio, String>> colonne = new LinkedHashMap<>();
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.id"), Edificio::getId);
        colonne.put(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.nome"), Edificio::getNome);

        LinkedHashMap<String, Function<Edificio, String>> dettagli = new LinkedHashMap<>(colonne);

        dettagli.put(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), s -> Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"));
        builder.setLinkAction(Main.getParametrizzazioneHelper().getBundle().getString("field.esportaEntita"), iscrizione -> PdfHelper.esportaEntita(iscrizione, Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.edificio.descrizione") + " " + iscrizione.getId(), Main.getParametrizzazioneHelper().getBundle().getString("string.esportaEntita.edificio.descrizione") + " " + iscrizione.getId()));

        return builder.build(
                Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.titolo"),
                colonne,
                dettagli,
                () -> {
                    DialogBuilder<Edificio> dialogBuilder = new DialogBuilder<>(
                            Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.builder.titolo"),
                            Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.builder.header"),
                            campi -> {
                                try {
                                    String nome = DialogsParser.validaCampo(campi, Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.nome"));
                                return new Edificio(String.valueOf(Main.getEdificioManager().assegnaIndiceCorrente()), nome);
                                } catch (CampoRichiestoVuoto e) {
                                    throw new CampoRichiestoVuoto(e.getMessage());
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("Errore nei dati: " + e.getMessage());
                                }


                            },
                            edificio -> {
                                Main.getEdificioManager().aggiungi(edificio);
                                ricaricaInterfacciaGraficaEdificiPannello2();
                                Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.builder.success2"));
                            }
                    );

                    dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.nome"), new TextField());
                    dialogBuilder.mostra();
                },
                edificio -> {
                    mostraDialogModificaEdificio(edificio, builder);
                    System.out.println(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.modifica.titolo") + edificio.getId());
                },
                edificio -> {
                    Main.getEdificioManager().rimuovi(edificio);
                    ricaricaInterfacciaGraficaEdificiPannello2();
                }
        );
    }

    /**
     * Crea una finestra di modifica per edifici selezionati sfruttando la logica di creazione.
     * @param edificio Gli edifici a cui apportare le modifiche.
     * @param builder Il builder grafico utilizzato per costruire la vista degli edifici.
     */
    private static void mostraDialogModificaEdificio(Edificio edificio, VistaConDettagliBuilder<Edificio> builder) {
        DialogBuilder<Edificio> dialogBuilder = new DialogBuilder<>(
                Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.modifica.titolo"),
                Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.modifica.header"),
                campi -> {
                    String nome = ((TextField) campi.get(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.nome"))).getText();

                    edificio.setNome(nome);
                    return edificio;
                },
                e -> {
                    ricaricaInterfacciaGraficaEdificiPannello2();
                    Dialogs.showInfo(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.builder.success1"), Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.modifica.success"));
                }
        );

        dialogBuilder.aggiungiCampo(Main.getParametrizzazioneHelper().getBundle().getString("string.esamiPannello2.nome"), new TextField(edificio.getNome()));

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

        //
        Edificio selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona un'appello da modificare.");
            return;
        }

        //
        mostraDialogModificaEdificio(selezionato, builder);

        //
        Main.getEdificioManager().salvaSuFile();
    }

    /**
     * Elimina lo studente selezionato dalla tabella.
     */
    public static void eliminaSelezionato() {
        if (builder == null || builder.getTabella() == null) return;

        //
        Edificio selezionato = builder.getTabella().getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            Dialogs.showError("Nessuna selezione", "Seleziona uno studente da eliminare.");
            return;
        }

        //
        Main.getEdificioManager().rimuovi(selezionato);

        //
        ricaricaInterfacciaGraficaEdificiPannello2();
    }

    /**
     * Restituisce il builder grafico utilizzato per costruire la vista degli edifici.
     *
     * @return il {@link VistaConDettagliBuilder} associato agli edifici.
     */
    public static VistaConDettagliBuilder<Edificio> getBuilder() {
        return builder;
    }
}
