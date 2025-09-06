package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione dei {@link Docente}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class DocenteRepository extends FileJsonRepository<Docente> {

    /**
     * Genera un nuovo {@code DocenteRepository} inizializzandolo
     * grazie al path del file JSON riservato ai docenti.
     */
    public DocenteRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/professori.json",
                new TypeToken<List<Docente>>() {}.getType());
    }
}
