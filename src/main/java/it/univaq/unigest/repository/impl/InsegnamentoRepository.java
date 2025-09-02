package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class InsegnamentoRepository extends FileJsonRepository<Insegnamento>{
    public InsegnamentoRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/insegnamenti.json",
                new TypeToken<List<Insegnamento>>() {}.getType());
    }
}
