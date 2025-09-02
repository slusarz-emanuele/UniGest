package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;

import java.lang.reflect.Type;
import java.util.List;

public class CorsoDiLaureaRepository extends FileJsonRepository<CorsoDiLaurea> {
    public CorsoDiLaureaRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/corsiDiLaurea.json",
                new TypeToken<List<CorsoDiLaurea>>() {}.getType());
    }
}
