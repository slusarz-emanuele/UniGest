package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.util.List;

public class IscrizioneManager extends AbstractManager<Iscrizione> {

    public IscrizioneManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/iscrizioni.json",
                new TypeToken<List<Iscrizione>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(String idIscrizione, Iscrizione iscrizione) {
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(idIscrizione)){
                lista.set(i, iscrizione);
                salvaSuFile();
                LogHelper.saveLog(LogType.INFO, "La modifica per l'iscrizione " + idIscrizione + " è stata effettuata");
                return true;
            }
        }

        LogHelper.saveLog(LogType.ERROR, "Nessuna iscrizione trovata con l'ID: " + idIscrizione);
        return false;
    }

    @Override
    public List<Iscrizione> filtra(Iscrizione filtro) {
        return lista.stream()
                .filter(i -> filtro.getId() == null || i.getId().equals(filtro.getId()))
                .filter(i -> filtro.getRidStudenteCf() == null || i.getRidStudenteCf().equalsIgnoreCase(filtro.getRidStudenteCf()))
                .filter(i -> filtro.getRidAppello() == 0 || i.getRidAppello() == filtro.getRidAppello())
                .filter(i -> filtro.getDataIscrizione() == null || i.getDataIscrizione().equals(filtro.getDataIscrizione()))
                .filter(i -> !filtro.getRitirato() || i.getRitirato())
                .toList();
    }

}
