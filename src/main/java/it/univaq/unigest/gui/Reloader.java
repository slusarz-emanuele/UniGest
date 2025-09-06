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
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

public class Reloader {

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

    public static void registerAppelliPannello(AppelliPannello1 p){
        appelliPanel = p;
    }

    public static void registerAulePannello(AulePannello1 p){
        aulePanel = p;
    }

    public static void registerEdificiPannello(EdificiPannello1 p){
        edificiPanel = p;
    }

    public static void registerEsamiPannello(EsamiPannello1 p){
        esamiPanel = p;
    }

    public static void registerInsegnamentiPannello(InsegnamentiPannello1 p){
        insegnamentiPanel = p;
    }

    public static void registerIscrizioniPannello(IscrizioniPannello1 p){
        iscrizioniPanel = p;
    }

    public static void registerVerbaliPannello(VerbaliPannello1 p){
        verbaliPanel = p;
    }

    public static void registerDocentiPannello(DocentiPannello1 p) {
        docentiPanel = p;
    }

    public static void registerStudentiPannello(StudentiPannello1 p) {
        studentiPanel = p;
    }

    public static void registerCorsiDiLaureaPannello(CorsiDiLaureaPannello1 p) {
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
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaStudentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaCorsiDiLaureaPannello2(){
        if (corsiDiLaureaPanel != null) {
            corsiDiLaureaPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaCDLPannello2()");
    }

    public static void ricaricaInterfacciaGraficaAppelliPannello2(){
        if(appelliPanel != null){
            appelliPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaAppelliPannello2()");
    }

    public static void ricaricaInterfacciaGraficaAulePannello2(){
        if(aulePanel != null){
            aulePanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaAulePannello2()");
    }

    public static void ricaricaInterfacciaGraficaEdificiPannello2(){
        if(edificiPanel != null){
            edificiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaEdificiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaEsamiPannello2(){
        if(esamiPanel != null){
            esamiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaEsamiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaInsegnamentiPannello2(){
        if(insegnamentiPanel != null){
            insegnamentiPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaInsegnamentiPannello2()");
    }

    public static void ricaricaInterfacciaGraficaIscrizioniPannello2(){
        if(iscrizioniPanel != null){
            iscrizioniPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaIscrizioniPannello2()");
    }

    public static void ricaricaInterfacciaGraficaVerbaliPannelli2(){
        if(verbaliPanel != null){
            verbaliPanel.refresh();
        }
        LogHelper.saveLog(LogType.DEBUG, "ricaricaInterfacciaGraficaVerbaliPannelli2()");
    }
}
