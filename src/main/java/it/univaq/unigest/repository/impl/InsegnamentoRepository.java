package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.repository.support.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione degli {@link Insegnamento}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class InsegnamentoRepository extends FileJsonRepository<Insegnamento>{

    /**
     * Genera un nuovo {@code InsegnamentoRepository} inizializzandolo
     * grazie al path del file JSON riservato agli insegnamenti.
     */
    public InsegnamentoRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/insegnamenti.json",
                new TypeToken<List<Insegnamento>>() {}.getType());
    }
}
