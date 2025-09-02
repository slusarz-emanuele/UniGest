package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.StartView;
import it.univaq.unigest.manager.*;
import it.univaq.unigest.repository.impl.*;
import it.univaq.unigest.service.*;
import it.univaq.unigest.service.impl.*;
import it.univaq.unigest.util.*;
import it.univaq.unigest.util.backup.BackupManager;
import it.univaq.unigest.util.loader.ImpostazioniLoader;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// >>> nuovi import per i servizi docenti


public class Main extends Application {

    private static StudenteManager studenteManager = new StudenteManager();
    private static DocenteManager docenteManager = new DocenteManager(); // <- rimane per compatibilità momentanea
    private static AppelloManager appelloManager = new AppelloManager();
    private static AulaManager aulaManager = new AulaManager();
    private static CorsoDiLaureaManager corsoDiLaureaManager = new CorsoDiLaureaManager();
    private static EsameManager esameManager = new EsameManager();
    private static InsegnamentoManager insegnamentoManager = new InsegnamentoManager();
    private static IscrizioneManager iscrizioneManager = new IscrizioneManager();
    private static VerbaleManager verbaleManager = new VerbaleManager();
    private static EdificioManager edificioManager = new EdificioManager();

    // Quelli nuovi da sostituire
    private static AppelloService appelloService;
    private static AulaService aulaService;
    private static CorsoDiLaureaService corsoDiLaureaService;
    private static DocenteService docenteService;
    private static EdificioService edificioService;
    private static EsameService esameService;
    private static InsegnamentoService insegnamentoService;
    private static IscrizioneService iscrizioneService;
    private static StudenteService studenteService;
    private static VerbaleService verbaleService;

    private static Impostazioni impostazioni;
    private static BackupManager backupManager = new BackupManager();
    private static ParametrizzazioneHelper parametrizzazioneHelper;
    private static Stage stagePrimario;

    public static void main(String[] args) { launch(args); }

    public void init() {
        DatabaseHelper.verFileLog();
        DatabaseHelper.verDirData();
        DatabaseHelper.verFilesData();

        impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
        parametrizzazioneHelper = new ParametrizzazioneHelper();

        // Inizializzo subito il service dei docenti (repo -> service)
        appelloService = new AppelloServiceImpl(new AppelloRepository());
        aulaService = new AulaServiceImpl(new AulaRepository());
        corsoDiLaureaService = new CorsoDiLaureaServiceImpl(new CorsoDiLaureaRepository());
        docenteService = new DocenteServiceImpl(new DocenteRepository());
        edificioService = new EdificioServiceImpl(new EdificioRepository());
        esameService = new EsameServiceImpl(new EsameRepository());
        insegnamentoService = new InsegnamentoServiceImpl(new InsegnamentoRepository());
        iscrizioneService = new IscrizioneServiceImpl(new IscrizioneRepository());
        studenteService = new StudenteServiceImpl(new StudenteRepository());
        verbaleService = new VerbaleServiceImpl(new VerbaleRepository());

        LogHelper.saveLog(LogType.INFO, "[Main.init] L'applicazione è stata avviata");
    }

    @Override
    public void start(Stage stagePrimarioP) {
        stagePrimario = stagePrimarioP;

        // Splash
        ProgressIndicator indicator = new ProgressIndicator();
        VBox splashLayout = new VBox(20, indicator);
        splashLayout.setAlignment(Pos.CENTER);
        Scene splashScene = new Scene(splashLayout, 300, 200);
        Stage splashStage = new Stage();
        splashStage.setScene(splashScene);
        splashStage.setTitle("Unigest");
        splashStage.show();

        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() {
                impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
                parametrizzazioneHelper = new ParametrizzazioneHelper();
                DatabaseHelper.caricaDatiInMemoria();
                // service docenti già pronto in init(); se vuoi ricaricare il repo, ricrea il service qui
                // docenteService = new DocenteServiceImpl(new DocenteRepository());
                return null;
            }
        };

        loadingTask.setOnSucceeded(event -> {
            splashStage.close();
            StartView dashboard = new StartView(); // StartView userà Main.getDocenteService()
            dashboard.start(stagePrimario);
            LogHelper.saveLog(LogType.DEBUG, "[Main.start] Caricamento dei dati completato.");
        });

        LogHelper.saveLog(LogType.INFO, "[Main.start] L'applicazione ha avviato l'interfaccia grafica e i manager.");
        new Thread(loadingTask).start();
    }

    public static void restartApp() {
        try {
            stagePrimario.close();

            impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
            parametrizzazioneHelper = new ParametrizzazioneHelper();

            // Manager storici (ancora attivi per gli altri modelli)
            studenteManager = new StudenteManager();
            docenteManager = new DocenteManager();
            appelloManager = new AppelloManager();
            aulaManager = new AulaManager();
            corsoDiLaureaManager = new CorsoDiLaureaManager();
            esameManager = new EsameManager();
            insegnamentoManager = new InsegnamentoManager();
            iscrizioneManager = new IscrizioneManager();
            verbaleManager = new VerbaleManager();
            edificioManager = new EdificioManager();
            backupManager = new BackupManager();

            // Reinstanzia anche il service docenti
            appelloService = new AppelloServiceImpl(new AppelloRepository());
            aulaService = new AulaServiceImpl(new AulaRepository());
            corsoDiLaureaService = new CorsoDiLaureaServiceImpl(new CorsoDiLaureaRepository());
            docenteService = new DocenteServiceImpl(new DocenteRepository());
            edificioService = new EdificioServiceImpl(new EdificioRepository());
            esameService = new EsameServiceImpl(new EsameRepository());
            insegnamentoService = new InsegnamentoServiceImpl(new InsegnamentoRepository());
            iscrizioneService = new IscrizioneServiceImpl(new IscrizioneRepository());
            studenteService = new StudenteServiceImpl(new StudenteRepository());
            verbaleService = new VerbaleServiceImpl(new VerbaleRepository());

            DatabaseHelper.caricaDatiInMemoria();

            Reloader.ricaricaInterfacciaGrafica();
            StartView dashboard = new StartView();
            dashboard.start(stagePrimario);

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.saveLog(LogType.ERROR, "[Main.restartApp] Errore durante il riavvio: " + e.getMessage());
        }
    }

    // --- Getter classici (rimangono per retrocompatibilità con le viste non migrate)
    public static Stage getPrimaryStage() { return stagePrimario; }
    public static StudenteManager getStudenteManager() { return studenteManager; }
    public static DocenteManager getDocenteManager() { return docenteManager; } // da eliminare quando migri tutto
    public static AppelloManager getAppelloManager() { return appelloManager; }
    public static AulaManager getAulaManager() { return aulaManager; }
    public static CorsoDiLaureaManager getCorsoDiLaureaManager() { return corsoDiLaureaManager; }
    public static EsameManager getEsameManager() { return esameManager; }
    public static InsegnamentoManager getInsegnamentoManager() { return insegnamentoManager; }
    public static IscrizioneManager getIscrizioneManager() { return iscrizioneManager; }
    public static VerbaleManager getVerbaleManager() { return verbaleManager; }
    public static EdificioManager getEdificioManager() { return edificioManager; }
    public static Impostazioni getImpostazioni() { return impostazioni; }
    public static ParametrizzazioneHelper getParametrizzazioneHelper() { return parametrizzazioneHelper; }
    public static BackupManager getBackupManager() { return backupManager; }

    // --- Nuovo getter usato dalla GUI per Docenti
    public static DocenteService getDocenteService() { return docenteService; }
    public static StudenteService getStudenteService() { return studenteService; }
    public static CorsoDiLaureaService getCorsoDiLaureaService() { return corsoDiLaureaService; }

    public static AppelloService getAppelloService() {
        return appelloService;
    }

    public static AulaService getAulaService() {
        return aulaService;
    }

    public static EdificioService getEdificioService() {
        return edificioService;
    }

    public static EsameService getEsameService() {
        return esameService;
    }

    public static InsegnamentoService getInsegnamentoService() {
        return insegnamentoService;
    }

    public static IscrizioneService getIscrizioneService() {
        return iscrizioneService;
    }

    public static VerbaleService getVerbaleService() {
        return verbaleService;
    }

    public static Stage getStagePrimario() { return stagePrimario; }
}
