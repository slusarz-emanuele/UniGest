package it.univaq.unigest.util.backup;

import it.univaq.unigest.repository.support.DatabaseHelper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * Implementazione di {@link BackupService} che crea e ripristina backup dei dati applicativi.
 *
 * <p><b>Cosa salva:</b> tutti i file <code>.txt</code> e <code>.json</code> presenti nella cartella dati
 * {@link DatabaseHelper#PERCORSO_CARTELLA_DATI}. I backup vengono salvati in
 * <code>{PERCORSO_CARTELLA_DATI}/backup</code> con nome basato su timestamp (pattern
 * {@code yyyyMMdd_HHmmss}.zip).</p>
 *
 * <p><b>Cosa fa il ripristino:</b> elimina prima i file <code>.txt</code> e <code>.json</code> correnti nella
 * cartella dati, poi estrae i file contenuti nell'archivio ZIP indicato.</p>
 *
 * <p><b>Note d’uso:</b>
 * <ul>
 *   <li>Gestire a livello superiore la UI/UX (conferme, progress, logging utente).</li>
 * </ul>
 * </p>
 */
public class BackupManager implements BackupService{

    /** Percorso cartella dati (origine dei file da salvare / destinazione del ripristino). */
    private static final String DATA_PATH = DatabaseHelper.PERCORSO_CARTELLA_DATI;

    /** Percorso cartella di destinazione dei file ZIP di backup. */
    public static final String BACKUP_PATH = DatabaseHelper.PERCORSO_CARTELLA_DATI + "/backup";

    /**
     * Crea un archivio ZIP contenente tutti i file <code>.txt</code> e <code>.json</code> nella cartella dati.
     *
     * <p>Passi eseguiti:
     * <ol>
     *   <li>Crea la cartella di backup se non esiste.</li>
     *   <li>Genera un nome file basato su timestamp (formato {@code yyyyMMdd_HHmmss}.zip).</li>
     *   <li>Scansiona la cartella dati e aggiunge al ZIP tutti i file regolari con estensione .txt/.json.</li>
     * </ol>
     * </p>
     *
     * @return {@code true} se l’archivio viene creato correttamente
     * @throws IOException se avviene un errore di I/O durante la creazione dell’archivio
     */
    @Override
    public boolean creaBackup() throws IOException {
        Files.createDirectories(Paths.get(BACKUP_PATH));

        String nomeBackup = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".zip";
        File zip = new File(BACKUP_PATH, nomeBackup);

        try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(zip))) {
            // prendi tutti i file .txt e .json
            try (Stream<Path> files = Files.list(Paths.get(DATA_PATH))) {
                files.filter(path -> {
                    String name = path.getFileName().toString().toLowerCase();
                    return (name.endsWith(".txt") || name.endsWith(".json")) && Files.isRegularFile(path);
                }).forEach(path -> {
                    try {
                        ZipArchiveEntry entry = new ZipArchiveEntry(path.toFile(), path.getFileName().toString());
                        zipOut.putArchiveEntry(entry);
                        try (InputStream in = Files.newInputStream(path)) {
                            IOUtils.copy(in, zipOut);
                        }
                        zipOut.closeArchiveEntry();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }
        }
        return true;
    }

    /**
     * Ripristina i dati a partire da un file ZIP di backup: prima elimina i file correnti
     * <code>.txt</code>/<code>.json</code> nella cartella dati e poi estrae i contenuti dall'archivio.
     *
     * @param backup file ZIP di backup da cui ripristinare
     * @return {@code true} se il ripristino va a buon fine, {@code false} altrimenti
     */
    @Override
    public boolean ripristinaBackup(File backup) {
        if (backup == null || !backup.exists()) {
            System.err.println("File di backup non valido: " + backup);
            return false;
        }

        try {
            // 1) Rimuovi tutti i .txt e .json esistenti in DATA_PATH
            try (Stream<Path> files = Files.list(Paths.get(DATA_PATH))) {
                files.filter(path -> {
                    String name = path.getFileName().toString().toLowerCase();
                    return (name.endsWith(".txt") || name.endsWith(".json")) && Files.isRegularFile(path);
                }).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            }

            // 2) Estrai i file dallo ZIP
            try (org.apache.commons.compress.archivers.zip.ZipFile zipFile =
                         new org.apache.commons.compress.archivers.zip.ZipFile(backup)) {
                var entries = zipFile.getEntries();
                while (entries.hasMoreElements()) {
                    ZipArchiveEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        Path outPath = Paths.get(DATA_PATH, entry.getName());
                        try (InputStream is = zipFile.getInputStream(entry)) {
                            Files.copy(is, outPath);
                        }
                    }
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
