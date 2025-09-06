package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

/**
 * Implementazione del repository che si occupa
 * della gestione delle {@link Iscrizione}.
 * <p>
 * Estende {@link FileJsonRepository} e si serve di un file JSON
 * per attuare la persistenza dei dati.
 */
public class IscrizioneRepository extends FileJsonRepository<Iscrizione>{

    /**
     * Genera un nuovo {@code IscrizioneRepository} inizializzandolo
     * grazie al path del file JSON riservato alle iscrizioni.
     */
    public IscrizioneRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/iscrizioni.json",
                new TypeToken<List<Iscrizione>>() {}.getType());
    }
}
