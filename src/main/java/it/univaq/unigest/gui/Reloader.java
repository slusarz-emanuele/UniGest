package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.gui.modelview.pannelli.aule.AulePannello2;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.edifici.EdificiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.esami.EsamiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.insegnamenti.InsegnamentiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.iscrizioni.IscrizioniPannello2;
import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.verbali.VerbaliPannello2;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

public class Reloader {

    /**
     *  Ricarica interamente i componenti dell'interfaccia grafica dell'applicazione.
     *  <p>
     *  Invoca la ricarica per ogni pannello disponibile (Appelli, Aule, Corsi di Laurea, Docenti, Edifici, Esami, Insegnamenti, Iscrizioni, Studenti, Verbali).
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
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGrafica() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello AppelliPannello2
     */
    public static void ricaricaInterfacciaGraficaAppelliPannello2(){
        AppelliPannello2.getBuilder().refresh(Main.getAppelloManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAppelliPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello AulePannello2
     */
    public static void ricaricaInterfacciaGraficaAulePannello2(){
        //AulePannello2.getBuilder().refresh(Main.getAulaManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaAulePannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello CorsiDiLaureaPannello2
     */
    public static void ricaricaInterfacciaGraficaCorsiDiLaureaPannello2(){
        CorsiDiLaureaPannello2.getBuilder().refresh(Main.getCorsoDiLaureaManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaCorsiDiLaureaPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello DocentiPannello2
     */
    public static void ricaricaInterfacciaGraficaDocentiPannello2(){
        DocentiPannello2.getBuilder().refresh(Main.getDocenteManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaDocentiPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello EdificiPannello2
     */
    public static void ricaricaInterfacciaGraficaEdificiPannello2(){
        //EdificiPannello2.getBuilder().refresh(Main.getEdificioManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaEdificiPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello EsamiPannello2
     */
    public static void ricaricaInterfacciaGraficaEsamiPannello2(){
        //EsamiPannello2.getBuilder().refresh(Main.getEsameManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaEsamiPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello InsegnamentiPannello2
     */
    public static void ricaricaInterfacciaGraficaInsegnamentiPannello2(){
        //InsegnamentiPannello2.getBuilder().refresh(Main.getInsegnamentoManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaInsegnamentiPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello IscrizioniPannello2
     */
    public static void ricaricaInterfacciaGraficaIscrizioniPannello2(){
        //IscrizioniPannello2.getBuilder().refresh(Main.getIscrizioneManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaIscrizioniPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello StudentiPannello2
     */
    public static void ricaricaInterfacciaGraficaStudentiPannello2(){
        StudentiPannello2.getBuilder().refresh(Main.getStudenteManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaStudentiPannello2() invocato");
    }

    /**
     *  Ricarica interamente i componenti d'interfaccia grafica del pannello VerbaliPannelli2
     */
    public static void ricaricaInterfacciaGraficaVerbaliPannelli2(){
        //VerbaliPannello2.getBuilder().refresh(Main.getVerbaleManager().getAll()); TODO: benedetta lo finice a collegare
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGraficaVerbaliPannelli2() invocato");
    }

}
