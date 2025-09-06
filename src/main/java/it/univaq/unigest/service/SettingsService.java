package it.univaq.unigest.service;

import it.univaq.unigest.model.Settings;

/**
 * Interfaccia dedicata alle impostazioni dell'applicazione.
 */
public interface SettingsService {

     /**
     * Restituisce le impostazioni attuali dell'applicazione.
     *
     * @return l'oggetto {@link Settings} che rappresenta le configurazioni in uso
     */
    Settings get();

     /**
     * Aggiorna il path della directory di backup.
     *
     * @param path il nuovo percorso della directory di backup
     * @return l'oggetto {@link Settings} aggiornato
     */
    Settings updateCartellaBackup(String path);
}
