package it.univaq.unigest.util;

import it.univaq.unigest.gui.Main;

import java.io.File;
import java.io.IOException;

/**
 * Classe di utilità per la gestione della persistenza dei dati e la loro inizializzazione all'avvio dell'applicazione.
 * <p>
 * Questa classe si occupa di:
 * <ul>
 *   <li>Verificare l'esistenza della cartella di lavoro per la persistenza dei dati.</li>
 *   <li>Verificare e creare, se mancanti, i file dati necessari al funzionamento del software.</li>
 *   <li>Verificare la presenza del file di log.</li>
 *   <li>Caricare in memoria i dati all'avvio del programma.</li>
 * </ul>
 * </p>
 */
public class DatabaseHelper {

    /**
     * Percorso della cartella principale in cui vengono salvati tutti i file dati dell'applicazione.
     */
    public static final String PERCORSO_CARTELLA_DATI = "src/main/resources/data";

    /**
     * Verifica la presenza della cartella dati.
     * <p>
     * Se la cartella non esiste, viene creata automaticamente.
     * Il risultato dell'operazione viene registrato nel log.
     * </p>
     */
    public static void verDirData() {
        File cartella = new File(PERCORSO_CARTELLA_DATI);

        if (!cartella.exists()) {
            if (cartella.mkdirs()) {
                LogHelper.saveLog(LogType.INFO, "Cartella creata");
            }
        } else {
            LogHelper.saveLog(LogType.INFO, "Cartella già esistente");
        }
    }

    /**
     * Verifica la presenza di tutti i file necessari alla persistenza dei dati.
     * <p>
     * Se uno o più file non esistono, vengono creati automaticamente come file vuoti.
     * Ogni operazione viene registrata nel log.
     * </p>
     */
    public static void verFilesData() {
        File[] files = {
                new File(PERCORSO_CARTELLA_DATI, "studenti.json"),
                new File(PERCORSO_CARTELLA_DATI, "professori.json"),
                new File(PERCORSO_CARTELLA_DATI, "appelli.json"),
                new File(PERCORSO_CARTELLA_DATI, "aule.json"),
                new File(PERCORSO_CARTELLA_DATI, "corsiDiLaurea.json"),
                new File(PERCORSO_CARTELLA_DATI, "esami.json"),
                new File(PERCORSO_CARTELLA_DATI, "insegnamenti.json"),
                new File(PERCORSO_CARTELLA_DATI, "iscrizioni.json"),
                new File(PERCORSO_CARTELLA_DATI, "verbale.json"),
                new File(PERCORSO_CARTELLA_DATI, "impostazioni.json")
        };

        for (File file : files) {
            try {
                if (!file.exists()) {
                    if (file.createNewFile()) {
                        LogHelper.saveLog(LogType.INFO, "Il file " + file.getName() + " è stato creato con successo");
                    }
                } else {
                    LogHelper.saveLog(LogType.INFO, "Il file " + file.getName() + " è esistente");
                }
            } catch (IOException e) {
                LogHelper.saveLog(LogType.ERROR,
                        "Il file " + file.getName() + " non è stato creato a causa di un'eccezione: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * Verifica la presenza del file di log.
     * <p>
     * Se il file non esiste, viene creato automaticamente.
     * </p>
     */
    public static void verFileLog() {
        File file = new File(PERCORSO_CARTELLA_DATI, "log.txt");

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    LogHelper.saveLog(LogType.INFO, "Il file " + file.getName() + " è stato creato con successo");
                }
            } else {
                LogHelper.saveLog(LogType.INFO, "Il file " + file.getName() + " è esistente");
            }
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR,
                    "Il file " + file.getName() + " non è stato creato a causa di un'eccezione: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void caricaDatiInMemoria() {
        Main.getStudenteManager().caricaDaFile();
        Main.getStudenteManager().caricaIndiceCorrente();

        //Main.getDocenteManager().caricaDaFile();
        //Main.getDocenteManager().caricaIndiceCorrente();

        Main.getAppelloManager().caricaDaFile();
        Main.getAppelloManager().caricaIndiceCorrente();

        Main.getAulaManager().caricaDaFile();
        Main.getAulaManager().caricaIndiceCorrente();

        Main.getCorsoDiLaureaManager().caricaDaFile();
        Main.getCorsoDiLaureaManager().caricaIndiceCorrente();

        Main.getEsameManager().caricaDaFile();
        Main.getEsameManager().caricaIndiceCorrente();

        Main.getInsegnamentoManager().caricaDaFile();
        Main.getInsegnamentoManager().caricaIndiceCorrente();

        Main.getIscrizioneManager().caricaDaFile();
        Main.getIscrizioneManager().caricaIndiceCorrente();

        Main.getVerbaleManager().caricaDaFile();
        Main.getVerbaleManager().caricaIndiceCorrente();

        Main.getEdificioManager().caricaDaFile();
        Main.getEdificioManager().caricaIndiceCorrente();

        LogHelper.saveLog(LogType.INFO, "I dati sono stati caricati in memoria dai file.");
    }

}
