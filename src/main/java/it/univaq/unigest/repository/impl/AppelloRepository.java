package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.repository.support.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione degli {@link Appello}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati
 */
public class AppelloRepository extends FileJsonRepository<Appello>{

    /**
     * Genera un nuovo {@code AppelloRepository} inizializzandolo
     * grazie al path del file JSON riservato agli appelli.
     */
    public AppelloRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/appelli.json",
                new TypeToken<List<Appello>>() {}.getType());
    }
}
