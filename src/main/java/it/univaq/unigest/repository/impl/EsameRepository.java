package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class EsameRepository extends FileJsonRepository<Esame>{
    public EsameRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/esami.json",
                new TypeToken<List<Esame>>() {}.getType());
    }
}
