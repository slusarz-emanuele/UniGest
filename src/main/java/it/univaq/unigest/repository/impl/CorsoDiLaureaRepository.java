package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione dei {@link CorsoDiLaurea}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class CorsoDiLaureaRepository extends FileJsonRepository<CorsoDiLaurea> {

    /**
     * Genera un nuovo {@code CorsoDiLaureaRepository} inizializzandolo
     * grazie al path del file JSON riservato ai corsi di laurea.
     */
    public CorsoDiLaureaRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/corsiDiLaurea.json",
                new TypeToken<List<CorsoDiLaurea>>() {
                }.getType());
    }
}
