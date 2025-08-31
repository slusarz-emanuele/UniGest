package it.univaq.unigest.gui;

import it.univaq.unigest.gui.modelview.StartView;
import it.univaq.unigest.manager.*;
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

public class Main extends Application {

    /**
     * Gestore degli studenti, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Studente}.
     */
    private static StudenteManager studenteManager = new StudenteManager();

    /**
     * Gestore dei docenti, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Docente}.
     */
    private static DocenteManager docenteManager = new DocenteManager();

    /**
     * Gestore degli Appelli, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Appello}.
     */
    private static AppelloManager appelloManager = new AppelloManager();

    /**
     * Gestore delle Aule, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Aula}.
     */
    private static AulaManager aulaManager = new AulaManager();

    /**
     * Gestore dei Corsi di Laurea, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.CorsoDiLaurea}.
     */
    private static CorsoDiLaureaManager corsoDiLaureaManager = new CorsoDiLaureaManager();

    /**
     * Gestore degli Esami, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Esame}.
     */
    private static EsameManager esameManager = new EsameManager();

    /**
     * Gestore degli Insegnamenti, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Insegnamento}.
     */
    private static InsegnamentoManager insegnamentoManager = new InsegnamentoManager();

    /**
     * Gestore delle Iscrizioni, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Iscrizione}.
     */
    private static IscrizioneManager iscrizioneManager = new IscrizioneManager();

    /**
     * Gestore dei Verbali, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Verbale}.
     */
    private static VerbaleManager verbaleManager = new VerbaleManager();

    /**
     * Gestore degli Edifici, responsabile delle operazioni CRUD sugli oggetti {@link it.univaq.unigest.model.Edificio}.
     */
    private static EdificioManager edificioManager = new EdificioManager();

    /**
     * Rappresenta le impostazioni correnti dell'applicazione.
     * Contiene parametri configurabili e preferenze salvate.
     */
    //private static Impostazioni impostazioni = new Impostazioni();
    private static Impostazioni impostazioni;

    /**
     * Gestore del sistema di backup, responsabile delle operazioni di
     * salvataggio e ripristino dei dati.
     */
    private static BackupManager backupManager = new BackupManager();

    //private static ParametrizzazioneHelper parametrizzazioneHelper = new ParametrizzazioneHelper();
    private static ParametrizzazioneHelper parametrizzazioneHelper;

    private static Stage stagePrimario;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Verifica la corretta presenza delle cartelle e file per la gestione della persistenza.
     */
    public void init() {
        DatabaseHelper.verFileLog();
        DatabaseHelper.verDirData();
        DatabaseHelper.verFilesData();

        impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
        parametrizzazioneHelper = new ParametrizzazioneHelper();

        LogHelper.saveLog(LogType.INFO, "[Main.init] L'applicazione è stata avviata");
    }

    @Override
    public void start(Stage stagePrimarioP) {
        stagePrimario = stagePrimarioP;

        // Finestra di caricamento dell'applicazione
        ProgressIndicator indicator = new ProgressIndicator();
        VBox splashLayout = new VBox(20, indicator);
        splashLayout.setAlignment(Pos.CENTER);
        Scene splashScene = new Scene(splashLayout, 300, 200);
        Stage splashStage = new Stage();
        splashStage.setScene(splashScene);
        splashStage.setTitle("Unigest");
        splashStage.show();

        // Task di caricamento dati (i manager)
        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call(){
                impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
                parametrizzazioneHelper = new ParametrizzazioneHelper();
                DatabaseHelper.caricaDatiInMemoria();

                return null;
            }
        };

        // Azione dopo il caricamento dei dati
        loadingTask.setOnSucceeded(event -> {
            splashStage.close();
            StartView dashboard = new StartView();
            dashboard.start(stagePrimario);

            LogHelper.saveLog(LogType.DEBUG, "[Main.start] Caricamento dei dati completato.");
        });

        LogHelper.saveLog(LogType.INFO, "[Main.start] L'applicazione ha avviato l'interfaccia grafica e i manager.");

        new Thread(loadingTask).start();
    }

    public static void restartApp() {
        try {
            stagePrimario.close();

            // Reset dei manager
            impostazioni = ImpostazioniLoader.caricaImpostazioniDaFile();
            parametrizzazioneHelper = new ParametrizzazioneHelper();
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

            // Ricarica i dati dai file
            DatabaseHelper.caricaDatiInMemoria();

            // Ricarica la GUI
            Reloader.ricaricaInterfacciaGrafica();
            StartView dashboard = new StartView();
            dashboard.start(stagePrimario);

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.saveLog(LogType.ERROR, "[Main.restartApp] Errore durante il riavvio dell'applicazione: " + e.getMessage());
        }
    }

    public static Stage getPrimaryStage() {
        return stagePrimario;
    }

    public static StudenteManager getStudenteManager() {
        return studenteManager;
    }

    public static DocenteManager getDocenteManager() {
        return docenteManager;
    }

    public static AppelloManager getAppelloManager() {
        return appelloManager;
    }

    public static AulaManager getAulaManager() {
        return aulaManager;
    }

    public static CorsoDiLaureaManager getCorsoDiLaureaManager() {
        return corsoDiLaureaManager;
    }

    public static EsameManager getEsameManager() {
        return esameManager;
    }

    public static InsegnamentoManager getInsegnamentoManager() {
        return insegnamentoManager;
    }

    public static IscrizioneManager getIscrizioneManager() {
        return iscrizioneManager;
    }

    public static VerbaleManager getVerbaleManager() {
        return verbaleManager;
    }

    public static EdificioManager getEdificioManager() {
        return edificioManager;
    }

    public static Impostazioni getImpostazioni() {
        return impostazioni;
    }

    public static ParametrizzazioneHelper getParametrizzazioneHelper() {
        return parametrizzazioneHelper;
    }

    public static BackupManager getBackupManager() {
        return backupManager;
    }

    public static Stage getStagePrimario() {
        return stagePrimario;
    }
}
