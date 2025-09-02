package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class DocenteRepository extends FileJsonRepository<Docente> {
    public DocenteRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/professori.json",
                new TypeToken<List<Docente>>() {}.getType());
    }
}
