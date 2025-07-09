package it.univaq.unigest.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHelper {

    // Attributi di classe
    public static final String PERCORSO_FILE_DI_LOG = "src/main/resources/data/log.txt";
    public static final DateTimeFormatter formattazioneData = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Implementa il salvataggio dei log su file txt
     *
     * @param tipo Indica il tipo di log, INFO, ERROR, WARNING, DEBUG
     * @param messaggio Indica il contenuto del log
     */
    public static void saveLog(LogType tipo, String messaggio){
        String timestamp = LocalDateTime.now().format(formattazioneData);
        String code = tipo.name();

        String log = "[" + timestamp + "]" + " " + "[" + code + "]" + messaggio;

        System.out.println(log);

        try {
            FileWriter file = new FileWriter(PERCORSO_FILE_DI_LOG, true);
            file.write(log + "\n");
            file.close();
        }catch (IOException e){
            System.out.println("ERRORE LOG: Il file non è stato modificato per via di un errore: " + e.getMessage());
        }
    }


}
