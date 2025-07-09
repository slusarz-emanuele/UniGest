package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.util.List;

public class VerbaleManager extends AbstractManager<Verbale> {

    public VerbaleManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/verbale.json",
                new TypeToken<List<Verbale>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(String id, Verbale nuovo) {
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(id)){
                lista.set(i, nuovo);
                salvaSuFile();
                LogHelper.saveLog(LogType.INFO, "La modifica per il verbale " + id + " è stata effettuata");
                return true;
            }
        }

        LogHelper.saveLog(LogType.ERROR, "Nessuno verbale trovato con l'id: " + id);
        return false;
    }

    @Override
    public List<Verbale> filtra(Verbale filtro) {
        return lista.stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                .filter(v -> filtro.getAppelloId() == null || v.getAppelloId().equalsIgnoreCase(filtro.getAppelloId()))
                .filter(v -> filtro.getDataChiusura() == null || v.getDataChiusura().equals(filtro.getDataChiusura()))
                .filter(v -> !filtro.getChiuso() || v.getChiuso()) // true → filtra solo chiusi
                .filter(v -> !filtro.getFirmato() || v.getFirmato()) // true → filtra solo firmati
                .filter(v -> filtro.getNote() == null || (v.getNote() != null && v.getNote().toLowerCase().contains(filtro.getNote().toLowerCase())))
                .toList();
    }

}
