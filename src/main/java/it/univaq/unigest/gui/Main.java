package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.StartView;
import it.univaq.unigest.repository.impl.*;
import it.univaq.unigest.service.*;
import it.univaq.unigest.service.impl.*;
import it.univaq.unigest.util.*;
import it.univaq.unigest.util.backup.BackupManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

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

    private static BackupManager backupManager = new BackupManager();
    private static Stage stagePrimario;

    public static void main(String[] args) { launch(args); }

    public void init() {
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


            Reloader.ricaricaInterfacciaGrafica();
            StartView dashboard = new StartView();
            dashboard.start(stagePrimario);

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.saveLog(LogType.ERROR, "[Main.restartApp] Errore durante il riavvio: " + e.getMessage());
        }
    }

    public static Stage getPrimaryStage() {
        return stagePrimario;
    }

    public static DocenteService getDocenteService() {
        return docenteService;
    }

    public static StudenteService getStudenteService() {
        return studenteService;
    }

    public static CorsoDiLaureaService getCorsoDiLaureaService() {
        return corsoDiLaureaService;
    }

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

}
