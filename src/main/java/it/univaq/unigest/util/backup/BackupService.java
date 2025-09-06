package it.univaq.unigest.util.backup;

import java.io.File;
import java.io.IOException;

/**
 * Contratto per la gestione dei backup dell’applicazione.
 *
 * <p>Un’implementazione tipica crea un archivio (ZIP) contenente i file
 * di dati dell’applicazione e consente di ripristinarli in seguito.
 */
public interface BackupService {

    /**
     * Crea un backup completo dei dati applicativi nella destinazione prevista
     * dall’implementazione.
     *
     * @return {@code true} se il backup è stato creato correttamente, {@code false} in caso contrario
     * @throws IOException se si verifica un errore di I/O durante la creazione del backup
     */
    boolean creaBackup() throws IOException;

    /**
     * Ripristina i dati applicativi a partire da un file di backup fornito.
     *
     * @param file il file di backup da cui ripristinare (ZIP)
     * @return {@code true} se il ripristino è andato a buon fine, {@code false} in caso contrario
     */
    boolean ripristinaBackup(File file);

}
