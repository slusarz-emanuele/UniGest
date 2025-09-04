package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.StartView;
import it.univaq.unigest.repository.impl.*;
import it.univaq.unigest.service.*;
import it.univaq.unigest.service.impl.*;
import it.univaq.unigest.service.query.DomainQueryService;
import it.univaq.unigest.service.query.impl.DomainQueryServiceImpl;
import it.univaq.unigest.util.*;
import it.univaq.unigest.util.backup.BackupManager;
import it.univaq.unigest.util.loader.DomainInitializer;
import it.univaq.unigest.util.loader.DomainRefresher;
import it.univaq.unigest.gui.navigation.ViewDispatcher;
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
    private static SettingsService settingsService;
    private static MaintenanceService maintenanceService;

    private static Stage stagePrimario;
    private static DomainQueryService domainQueryService;

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

        settingsService = new SettingsServiceImpl(new SettingsRepository());
        maintenanceService = new MaintenanceServiceImpl(new BackupManager(), settingsService);

        DomainInitializer.initAll(
                studenteService,
                iscrizioneService,
                esameService,
                verbaleService,
                insegnamentoService,
                docenteService
        );

        DomainRefresher.init(
                studenteService,
                iscrizioneService,
                esameService,
                verbaleService,
                insegnamentoService,
                docenteService
        );

        domainQueryService = new DomainQueryServiceImpl(
                appelloService, aulaService, corsoDiLaureaService, docenteService,
                edificioService, esameService, insegnamentoService, iscrizioneService,
                studenteService, verbaleService
        );

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
            // opzionale: puoi anche non chiuderlo; StartView farà show()
            stagePrimario.close();

            // 1) Ricrea repository/services di dominio
            appelloService        = new AppelloServiceImpl(new AppelloRepository());
            aulaService           = new AulaServiceImpl(new AulaRepository());
            corsoDiLaureaService  = new CorsoDiLaureaServiceImpl(new CorsoDiLaureaRepository());
            docenteService        = new DocenteServiceImpl(new DocenteRepository());
            edificioService       = new EdificioServiceImpl(new EdificioRepository());
            esameService          = new EsameServiceImpl(new EsameRepository());
            insegnamentoService   = new InsegnamentoServiceImpl(new InsegnamentoRepository());
            iscrizioneService     = new IscrizioneServiceImpl(new IscrizioneRepository());
            studenteService       = new StudenteServiceImpl(new StudenteRepository());
            verbaleService        = new VerbaleServiceImpl(new VerbaleRepository());

            // 2) Settings/maintenance
            settingsService   = new SettingsServiceImpl(new SettingsRepository());
            backupManager     = new BackupManager();
            maintenanceService = new MaintenanceServiceImpl(backupManager, settingsService);

            // 3) (Ri)carica relazioni/derivate
            DomainInitializer.initAll(
                    studenteService,
                    iscrizioneService,
                    esameService,
                    verbaleService,
                    insegnamentoService,
                    docenteService
            );
            DomainRefresher.init(
                    studenteService,
                    iscrizioneService,
                    esameService,
                    verbaleService,
                    insegnamentoService,
                    docenteService
            );

            // 4) Query service
            domainQueryService = new DomainQueryServiceImpl(
                    appelloService, aulaService, corsoDiLaureaService, docenteService,
                    edificioService, esameService, insegnamentoService, iscrizioneService,
                    studenteService, verbaleService
            );

            // 5) Ricrea UI (StartView inizializza di nuovo il ViewDispatcher)
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

    public static DomainQueryService getDomainQueryService() {
        return domainQueryService;
    }

    public static SettingsService getSettingsService() { return settingsService; }
    public static BackupManager getBackupManager() { return backupManager; }
    public static MaintenanceService getMaintenanceService() { return maintenanceService; }

}
