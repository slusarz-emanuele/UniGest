package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello1;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello1;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello1;
import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello1;
import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello1;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility centralizzata per orchestrare il "refresh" della UI.
 * <p>
 * Questa classe mantiene dei riferimenti (statici) ai pannelli principali
 * dell’applicazione (uno per dominio) e fornisce metodi per:
 * <ul>
 *   <li>registrare un pannello quando viene creato/caricato;</li>
 *   <li>richiedere il refresh di uno specifico pannello o di tutti;</li>
 *   <li>loggare le operazioni effettuate.</li>
 * </ul>
 * @since 1.0
 */
public class Reloader {

    private static final Logger LOGGER = LogManager.getLogger(Reloader.class);

    /**
     * Richiede il refresh di <b>tutti</b> i pannelli registrati.
     * <p>
     * Esegue in sequenza i metodi specifici di refresh per ogni pannello.
     * I pannelli non registrati vengono ignorati (controllo null-safe).
     * Utile dopo operazioni globali (p.es. ripristino backup, reset).
     */
    public static void ricaricaInterfacciaGrafica(){
        ricaricaInterfacciaGraficaAppelliPannello2();
        ricaricaInterfacciaGraficaAulePannello2();
        ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
        ricaricaInterfacciaGraficaDocentiPannello2();
        ricaricaInterfacciaGraficaEdificiPannello2();
        ricaricaInterfacciaGraficaEsamiPannello2();
        ricaricaInterfacciaGraficaInsegnamentiPannello2();
        ricaricaInterfacciaGraficaIscrizioniPannello2();
        ricaricaInterfacciaGraficaStudentiPannello2();
        ricaricaInterfacciaGraficaVerbaliPannelli2();
        LOGGER.debug("invocato");
    }

    // Riferimenti statici ai pannelli
    private static AppelliPannello1 appelliPanel;
    private static AulePannello1 aulePanel;
    private static CorsiDiLaureaPannello1 corsiDiLaureaPanel;
    private static EdificiPannello1 edificiPanel;
    private static EsamiPannello1 esamiPanel;
    private static InsegnamentiPannello1 insegnamentiPanel;
    private static IscrizioniPannello1 iscrizioniPanel;
    private static VerbaliPannello1 verbaliPanel;
    private static DocentiPannello1 docentiPanel;
    private static StudentiPannello1 studentiPanel;

    /**
     * Registra il pannello Appelli da ricaricare in seguito.
     * @param p istanza del pannello Appelli (può essere {@code null} per disregistrare)
     */
    public static void registerAppelliPannello(AppelliPannello1 p){
        appelliPanel = p;
    }

    /**
     * Registra il pannello Aule.
     * @param p istanza del pannello Aule
     */
    public static void registerAulePannello(AulePannello1 p){
        aulePanel = p;
    }

    /**
     * Registra il pannello Edifici.
     * @param p istanza del pannello Edifici
     */
    public static void registerEdificiPannello(EdificiPannello1 p){
        edificiPanel = p;
    }

    /**
     * Registra il pannello Esami.
     * @param p istanza del pannello Esami
     */
    public static void registerEsamiPannello(EsamiPannello1 p){
        esamiPanel = p;
    }

    /**
     * Registra il pannello Insegnamenti.
     * @param p istanza del pannello Insegnamenti
     */
    public static void registerInsegnamentiPannello(InsegnamentiPannello1 p){
        insegnamentiPanel = p;
    }

    /**
     * Registra il pannello Iscrizioni.
     * @param p istanza del pannello Iscrizioni
     */
    public static void registerIscrizioniPannello(IscrizioniPannello1 p){
        iscrizioniPanel = p;
    }

    /**
     * Registra il pannello Verbali.
     * @param p istanza del pannello Verbali
     */
    public static void registerVerbaliPannello(VerbaliPannello1 p){
        verbaliPanel = p;
    }

    /**
     * Registra il pannello Docenti.
     * @param p istanza del pannello Docenti
     */
    public static void registerDocentiPannello(DocentiPannello1 p) {
        docentiPanel = p;
    }

    /**
     * Registra il pannello Studenti.
     * @param p istanza del pannello Studenti
     */
    public static void registerStudentiPannello(StudentiPannello1 p) {
        studentiPanel = p;
    }

    /**
     * Registra il pannello Corsi di Laurea.
     * @param p istanza del pannello Corsi di Laurea
     */
    public static void registerCorsiDiLaureaPannello(CorsiDiLaureaPannello1 p) {
        corsiDiLaureaPanel = p;
    }


    // ===== Refresh specifici =====

    /**
     * Richiede il refresh del pannello Docenti (se registrato).
     */
    public static void ricaricaInterfacciaGraficaDocentiPannello2(){
        if (docentiPanel != null) {
            docentiPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaDocentiPannello2()");
    }

    /**
     * Richiede il refresh del pannello Studenti (se registrato).
     */
    public static void ricaricaInterfacciaGraficaStudentiPannello2(){
        if (studentiPanel != null) {
            studentiPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaStudentiPannello2()");
    }

    /**
     * Richiede il refresh del pannello Corsi di Laurea (se registrato).
     */
    public static void ricaricaInterfacciaGraficaCorsiDiLaureaPannello2(){
        if (corsiDiLaureaPanel != null) {
            corsiDiLaureaPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaCDLPannello2()");
    }

    /**
     * Richiede il refresh del pannello Appelli (se registrato).
     */
    public static void ricaricaInterfacciaGraficaAppelliPannello2(){
        if(appelliPanel != null){
            appelliPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaAppelliPannello2()");
    }

    /**
     * Richiede il refresh del pannello Aule (se registrato).
     */
    public static void ricaricaInterfacciaGraficaAulePannello2(){
        if(aulePanel != null){
            aulePanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaAulePannello2()");
    }

    /**
     * Richiede il refresh del pannello Edifici (se registrato).
     */
    public static void ricaricaInterfacciaGraficaEdificiPannello2(){
        if(edificiPanel != null){
            edificiPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaEdificiPannello2()");
    }

    /**
     * Richiede il refresh del pannello Esami (se registrato).
     */
    public static void ricaricaInterfacciaGraficaEsamiPannello2(){
        if(esamiPanel != null){
            esamiPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaEsamiPannello2()");
    }

    /**
     * Richiede il refresh del pannello Insegnamenti (se registrato).
     */
    public static void ricaricaInterfacciaGraficaInsegnamentiPannello2(){
        if(insegnamentiPanel != null){
            insegnamentiPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaInsegnamentiPannello2()");
    }

    /**
     * Richiede il refresh del pannello Iscrizioni (se registrato).
     */
    public static void ricaricaInterfacciaGraficaIscrizioniPannello2(){
        if(iscrizioniPanel != null){
            iscrizioniPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaIscrizioniPannello2()");
    }

    /**
     * Richiede il refresh del pannello Verbali (se registrato).
     */
    public static void ricaricaInterfacciaGraficaVerbaliPannelli2(){
        if(verbaliPanel != null){
            verbaliPanel.refresh();
        }
        LOGGER.debug("ricaricaInterfacciaGraficaVerbaliPannelli2()");
    }
}
