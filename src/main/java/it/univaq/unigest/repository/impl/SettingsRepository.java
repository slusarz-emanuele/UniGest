package it.univaq.unigest.repository.impl;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Settings;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class SettingsRepository extends FileJsonRepository<Settings> {
    public SettingsRepository() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/settings.json",
                new TypeToken<List<Settings>>(){}.getType());
    }
}
