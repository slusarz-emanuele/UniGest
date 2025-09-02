package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class VerbaleRepository extends FileJsonRepository<Verbale>{
    public VerbaleRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/verbale.json",
                new TypeToken<List<Verbale>>() {}.getType());
    }
}
