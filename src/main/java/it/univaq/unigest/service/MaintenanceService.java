package it.univaq.unigest.service;

import java.io.File;

public interface MaintenanceService {
    boolean creaBackup();
    boolean ripristinaBackup(File zip);
    boolean resettaDati();
}
