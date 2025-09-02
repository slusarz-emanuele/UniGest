package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.pannelli.appelli.AppelliPannello2;
import it.univaq.unigest.gui.modelview.pannelli.cdl.CorsiDiLaureaPannello2;
import it.univaq.unigest.gui.modelview.pannelli.docenti.DocentiPannello2;
import it.univaq.unigest.gui.modelview.pannelli.studenti.StudentiPannello2;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

public class Reloader {

    public static void ricaricaInterfacciaGrafica(){
        ricaricaInterfacciaGraficaAppelliPannello2();
        ricaricaInterfacciaGraficaAulePannello2();
        ricaricaInterfacciaGraficaCorsiDiLaureaPannello2();
        ricaricaInterfacciaGraficaDocentiPannello2();   // <- ora usa il service
        ricaricaInterfacciaGraficaEdificiPannello2();
        ricaricaInterfacciaGraficaEsamiPannello2();
        ricaricaInterfacciaGraficaInsegnamentiPannello2();
        ricaricaInterfacciaGraficaIscrizioniPannello2();
        ricaricaInterfacciaGraficaStudentiPannello2();
        ricaricaInterfacciaGraficaVerbaliPannelli2();
        LogHelper.saveLog(LogType.DEBUG, "it.univaq.unigest.gui.Reloader.ricaricaInterfacciaGrafica() invocato");
    }

    private static DocentiPannello2 docentiPanel;
    private static StudentiPannello2 studentiPanel;
    private static CorsiDiLaureaPannello2 corsiDiLaureaPanel;

    public static void registerDocentiPannello(DocentiPannello2 p) {
        docentiPanel = p;
    }

    public static void registerStudentiPannello(StudentiPannello2 p) {
        studentiPanel = p;
    }

    public static void registerCorsiDiLaureaPannello(CorsiDiLaureaPannello2 p) {
        corsiDiLaureaPanel = p;
    }

    public static void ricaricaInterfacciaGraficaDocentiPannello2(){
        if (docentiPanel != null) {
            docentiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaDocentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaStudentiPannello2(){
        if (studentiPanel != null) {
            studentiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaDocentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaCorsiDiLaureaPannello2(){
        if (studentiPanel != null) {
            studentiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaDocentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaAppelliPannello2(){
        AppelliPannello2.getBuilder().refresh(Main.getAppelloManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaAppelliPannello2()");
    }

    public static void ricaricaInterfacciaGraficaAulePannello2(){
        //AulePannello2.getBuilder().refresh(Main.getAulaManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaAulePannello2()");
    }

    public static void ricaricaInterfacciaGraficaEdificiPannello2(){
        //EdificiPannello2.getBuilder().refresh(Main.getEdificioManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaEdificiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaEsamiPannello2(){
        //EsamiPannello2.getBuilder().refresh(Main.getEsameManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaEsamiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaInsegnamentiPannello2(){
        //InsegnamentiPannello2.getBuilder().refresh(Main.getInsegnamentoManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaInsegnamentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaIscrizioniPannello2(){
        //IscrizioniPannello2.getBuilder().refresh(Main.getIscrizioneManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaIscrizioniPannello2()");
    }

    public static void ricaricaInterfacciaGraficaVerbaliPannelli2(){
        //VerbaliPannello2.getBuilder().refresh(Main.getVerbaleManager().getAll());
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaVerbaliPannelli2()");
    }
}
