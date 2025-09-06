package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;
import it.univaq.unigest.util.DatabaseHelper;

public class Settings implements Identificabile<String> {

    private String id;
    private String cartellaBackup;

    public static Settings defaults() {
        Settings s = new Settings();
        s.id = "global";
        s.cartellaBackup = DatabaseHelper.PERCORSO_CARTELLA_DATI + "/backup";
        return s;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public String getCartellaBackup() { return cartellaBackup; }
    public void setCartellaBackup(String cartellaBackup) { this.cartellaBackup = cartellaBackup; }
}
