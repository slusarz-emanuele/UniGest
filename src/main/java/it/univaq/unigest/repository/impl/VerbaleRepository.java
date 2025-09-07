package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.repository.support.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione dei {@link Verbale}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei verbali.
 */
public class VerbaleRepository extends FileJsonRepository<Verbale>{

    /**
     * Genera un nuovo {@code VerbaleRepository} inizializzandolo
     * grazie al path del file JSON riservato ai verbali.
     */

    public VerbaleRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/verbale.json",
                new TypeToken<List<Verbale>>() {}.getType());
    }
}
