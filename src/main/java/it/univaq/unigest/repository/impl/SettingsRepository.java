package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Settings;
import it.univaq.unigest.repository.support.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione delle {@link Settings}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza delle impostazioni dell'applicazione.
 */
public class SettingsRepository extends FileJsonRepository<Settings> {
    public SettingsRepository() {

        /**
         * Genera un nuovo {@code SettingsRepository} inizializzandolo
         * grazie al path del file JSON riservato alle impostazioni.
         */
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/settings.json",
                new TypeToken<List<Settings>>() {
                }.getType());
    }
}
