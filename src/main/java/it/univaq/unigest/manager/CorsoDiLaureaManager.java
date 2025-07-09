package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.util.List;

public class CorsoDiLaureaManager extends AbstractManager<CorsoDiLaurea> {

    public CorsoDiLaureaManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/studenti.json",
                new TypeToken<List<CorsoDiLaurea>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(String id, CorsoDiLaurea nuovo) {
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(id)){
                lista.set(i, nuovo);
                salvaSuFile();
                LogHelper.saveLog(LogType.INFO, "La modifica per il corso di laurea " + id + " è stata effettuata");
                return true;
            }
        }

        LogHelper.saveLog(LogType.ERROR, "Nessun corso di laurea trovato con l'id: " + id);
        return false;
    }

    public List<CorsoDiLaurea> filtra(CorsoDiLaurea filtro){
        return lista.stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                //.filter()
                .toList();
    }

}
