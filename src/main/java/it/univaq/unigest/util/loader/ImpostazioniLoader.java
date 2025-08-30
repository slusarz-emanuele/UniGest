package it.univaq.unigest.util.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.Impostazioni;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImpostazioniLoader {

    private static final String PERCORSO_IMPOSTAZIONI = DatabaseHelper.PERCORSO_CARTELLA_DATI + "/impostazioni.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Impostazioni caricaImpostazioniDaFile() {
        try {
            File file = new File(PERCORSO_IMPOSTAZIONI);
            if (!file.exists()) {
                Impostazioni impostazioni = new Impostazioni(); // default se non, sottolineo NON esiste
                salvaImpostazioniSuFile(impostazioni);
                return impostazioni;
            }
            try (FileReader reader = new FileReader(file)) {
                Impostazioni imp = gson.fromJson(reader, Impostazioni.class);
                if (imp == null) {
                    imp = new Impostazioni();
                    salvaImpostazioniSuFile(imp);
                }
                return imp;
            }
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Problema con la lettura del file delle Impostazioni: " + e.getMessage());
            return new Impostazioni(); // è un fallback per evitare di non restituire le impostazioni
        } catch (Exception e) {
            LogHelper.saveLog(LogType.ERROR, "Errore imprevisto nel caricamento delle Impostazioni : " + e.getMessage());
            return new Impostazioni(); // è un fallback per evitare di non restituire le impostazioni
        }
    }

    public static void salvaImpostazioniSuFile(Impostazioni impostazioni) {
        try (FileWriter writer = new FileWriter(PERCORSO_IMPOSTAZIONI)) {
            gson.toJson(impostazioni, writer);
            LogHelper.saveLog(LogType.DEBUG, "Salvataggio delle Impostazioni riuscito");
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Problema con la scrittura del file delle Impostazioni: " + e.getMessage());
        } catch (Exception e) {
            LogHelper.saveLog(LogType.ERROR, "Errore imprevisto nel salvataggio delle Impostazioni : " + e.getMessage());
        }
    }

}
