package it.univaq.unigest.util.backup;

import it.univaq.unigest.util.DatabaseHelper;
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

public class BackupManager implements BackupService{

    // Attributi di classe
    private static final String DATA_PATH = DatabaseHelper.PERCORSO_CARTELLA_DATI;
    public static final String BACKUP_PATH = DatabaseHelper.PERCORSO_CARTELLA_DATI + "/backup";

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


    @Override
    public boolean verificaCadenzaBackup() {
        return true;
    }

}
