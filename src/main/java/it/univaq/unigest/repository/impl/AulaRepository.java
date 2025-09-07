package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione delle {@link Aula}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class AulaRepository extends FileJsonRepository<Aula>{

    /**
     * Genera un nuovo {@code AulaRepository} inizializzandolo
     * grazie al path del file JSON riservato alle aule.
     */
    public AulaRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/aule.json",
                new TypeToken<List<Aula>>() {}.getType());
    }
}
