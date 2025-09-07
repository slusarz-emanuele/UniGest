package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.repository.support.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione degli {@link Edificio}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class EdificioRepository extends FileJsonRepository<Edificio>{

    /**
     * Genera un nuovo {@code EdificioRepository} inizializzandolo
     * grazie al path del file JSON riservato agli edifici.
     */
    public EdificioRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/edifici.json",
                new TypeToken<List<Edificio>>() {}.getType());
    }
}
