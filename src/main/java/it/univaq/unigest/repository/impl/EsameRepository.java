package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione degli {@link Esame}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class EsameRepository extends FileJsonRepository<Esame>{

    /**
     * Genera un nuovo {@code EsameRepository} inizializzandolo
     * grazie al path del file JSON riservato agli esami.
     */
    public EsameRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/esami.json",
                new TypeToken<List<Esame>>() {}.getType());
    }
}
