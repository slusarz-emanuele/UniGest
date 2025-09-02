package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class EdificioRepository extends FileJsonRepository<Edificio>{
    public EdificioRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/edifici.json",
                new TypeToken<List<Edificio>>() {}.getType());
    }
}
