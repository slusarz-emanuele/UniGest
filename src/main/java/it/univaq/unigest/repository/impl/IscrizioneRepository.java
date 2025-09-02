package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class IscrizioneRepository extends FileJsonRepository<Iscrizione>{
    public IscrizioneRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/iscrizioni.json",
                new TypeToken<List<Iscrizione>>() {}.getType());
    }
}
