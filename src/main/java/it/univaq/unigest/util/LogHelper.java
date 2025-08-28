package it.univaq.unigest.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe di utilità per la gestione dei log applicativi.
 * <p>
 * I log vengono scritti sia in console che su un file di testo.
 * Ogni log è formattato con un timestamp e un codice che rappresenta il tipo di log.
 * </p>
 * 
 * <p>Esempio di log generato:</p>
 * <pre>
 * [2025-08-03 17:30:21] [it.univaq.unigest...] [INFO] Applicazione avviata correttamente
 * </pre>
 * 
 * <p>
 * Il file di log di default è salvato in {@code src/main/resources/data/log.txt}.
 * </p>
 */
public class LogHelper {

    /**
     * Percorso del file in cui verranno salvati i log.
     */
    private static final String PERCORSO_FILE_DI_LOG = "src/main/resources/data/log.txt";

    /**
     * Formattazione utilizzata per i timestamp dei log.
     */
    private static final DateTimeFormatter formattazioneData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Salva un messaggio di log sia su console che su file.
     * 
     * @param tipo      il tipo di log (INFO, ERROR, WARNING, DEBUG) come definito in {@link LogType}.
     * @param messaggio il contenuto del messaggio da registrare.
     */
    public static void saveLog(LogType tipo, String messaggio){
        // Classe e metodo chiamante
        StackTraceElement classeChiamante = Thread.currentThread().getStackTrace()[2];
        String classeChiamanteInformazioni = classeChiamante.getClassName() + "." + classeChiamante.getMethodName();

        // Data, ora, secondo
        String timestamp = LocalDateTime.now().format(formattazioneData);
        String code = tipo.name();

        // Costruzione del LOG
        String log = String.format("[%s] [%s] [%s] %s", timestamp, code, classeChiamanteInformazioni, messaggio);

        // Stampa del LOG sulla console
        System.out.println(log);

        // Scrittura su file
        try {
            FileWriter file = new FileWriter(PERCORSO_FILE_DI_LOG, true);
            file.write(log + "\n");
            file.close();
        }catch (IOException e){
            System.out.println("ERRORE LOG: Il file non è stato modificato per via di un errore: " + e.getMessage());
        }

    }
}