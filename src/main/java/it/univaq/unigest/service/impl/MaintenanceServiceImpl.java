package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Settings;
import it.univaq.unigest.service.MaintenanceService;
import it.univaq.unigest.service.SettingsService;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.backup.BackupManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class MaintenanceServiceImpl implements MaintenanceService {

    private final BackupManager backupManager;
    private final SettingsService settingsService;

    public MaintenanceServiceImpl(BackupManager backupManager, SettingsService settingsService) {
        this.backupManager = backupManager;
        this.settingsService = settingsService;
    }

    @Override
    public boolean creaBackup() {
        // Se vuoi usare la cartella dalle Settings in futuro, la leggi qui:
        Settings s = settingsService.get();
        // L’engine attuale usa la cartella interna; va bene: la GUI non lo sa.
        try {
            return backupManager.creaBackup();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean ripristinaBackup(File zip) {
        return backupManager.ripristinaBackup(zip);
    }

    @Override
    public boolean resettaDati() {
        Path dataPath = Paths.get(DatabaseHelper.PERCORSO_CARTELLA_DATI);
        Path backupPath = dataPath.resolve("backup");

        try (Stream<Path> files = Files.list(dataPath)) {
            files.forEach(p -> {
                try {
                    // non cancelliamo la cartella "backup"
                    if (Files.isDirectory(p)) {
                        if (!p.equals(backupPath)) {
                            deleteRecursively(p);
                        }
                    } else {
                        // elimina solo json/txt per sicurezza
                        String name = p.getFileName().toString().toLowerCase();
                        if (name.endsWith(".json") || name.endsWith(".txt")) {
                            Files.deleteIfExists(p);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void deleteRecursively(Path dir) throws IOException {
        if (!Files.exists(dir)) return;
        Files.walk(dir)
                .sorted((a, b) -> b.getNameCount() - a.getNameCount()) // prima i figli
                .forEach(p -> {
                    try { Files.deleteIfExists(p); } catch (IOException e) { throw new RuntimeException(e); }
                });
    }
}
