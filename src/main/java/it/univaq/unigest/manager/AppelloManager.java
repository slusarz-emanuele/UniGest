package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.util.List;
import java.util.stream.Collectors;

public class AppelloManager extends AbstractManager<Appello> {

    public AppelloManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/appelli.json",
                new TypeToken<List<Appello>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(String id, Appello nuovo) {
        try {
            int idInt = Integer.parseInt(id);
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == idInt) {
                    lista.set(i, nuovo);
                    salvaSuFile();
                    LogHelper.saveLog(LogType.INFO, "Appello con ID " + id + " aggiornato.");
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            LogHelper.saveLog(LogType.ERROR, "ID appello non valido: " + id);
        }

        LogHelper.saveLog(LogType.ERROR, "Nessun appello trovato con ID: " + id);
        return false;
    }

    @Override
    public List<Appello> filtra(Appello filtro) {
        return lista.stream()
                .filter(a -> filtro.getId() == 0 || a.getId() == filtro.getId())
                .filter(a -> filtro.getRidInsegnamento() == null || a.getRidInsegnamento().equalsIgnoreCase(filtro.getRidInsegnamento()))
                .filter(a -> filtro.getData() == null || a.getData().equals(filtro.getData()))
                .filter(a -> filtro.getOra() == null || a.getOra().equals(filtro.getOra()))
                .filter(a -> filtro.getRidAula() == null || a.getRidAula().equalsIgnoreCase(filtro.getRidAula()))
                .filter(a -> filtro.getRidDocente() == null || a.getRidDocente().equalsIgnoreCase(filtro.getRidDocente()))
                .filter(a -> filtro.getRidVerbale() == null || a.getRidVerbale().equalsIgnoreCase(filtro.getRidVerbale()))
                .collect(Collectors.toList());
    }
}
