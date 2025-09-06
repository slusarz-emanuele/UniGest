package it.univaq.unigest.service;

import java.io.File;

/**
 * Interfaccia alla quale viene demandata la gestione
 * delle operazioni di backup e reset.
 */
public interface MaintenanceService {

    /**
     * Crea un backup dei dati presenti nell'applicazione.
     *
     * @return {@code true} se il backup è stato creato correttamente, {@code false} altrimenti
     */
    boolean creaBackup();

    /**
     * Ripristina i dati dell'applicazione prendendoli da un file di backup.
     *
     * @param zip il file di backup da cui importare i dati
     * @return {@code true} se il ripristino è avvenuto, {@code false} altrimenti
     */
    boolean ripristinaBackup(File zip);

     /**
     * Riporta l'applicazione al suo stato di origine eliminando ogni dato
     *
     * @return {@code true} se il reset è avvenuto, {@code false} altrimenti
     */
    boolean resettaDati();
}
