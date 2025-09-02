package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class AulaRepository extends FileJsonRepository<Aula>{
    public AulaRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/aule.json",
                new TypeToken<List<Aula>>() {}.getType());
    }
}
