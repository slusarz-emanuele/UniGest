package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione degli {@link Studente}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati degli studenti.
 */
public class StudenteRepository extends FileJsonRepository<Studente>{

    /**
     * Genera un nuovo {@code StudenteRepository} inizializzandolo
     * grazie al path del file JSON riservato agli studenti.
     */
    public StudenteRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/studenti.json",
                new TypeToken<List<Studente>>() {}.getType());
    }
}
