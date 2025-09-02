package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class AppelloRepository extends FileJsonRepository<Appello>{
    public AppelloRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/appelli.json",
                new TypeToken<List<Appello>>() {}.getType());
    }
}
