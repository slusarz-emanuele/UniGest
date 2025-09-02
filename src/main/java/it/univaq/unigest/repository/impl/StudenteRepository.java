package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class StudenteRepository extends FileJsonRepository<Studente>{
    public StudenteRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/studenti.json",
                new TypeToken<List<Studente>>() {}.getType());
    }
}
