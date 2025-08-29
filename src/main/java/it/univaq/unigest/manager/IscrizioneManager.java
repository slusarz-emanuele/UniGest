package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class IscrizioneManager extends AbstractManager<Iscrizione> {

    public IscrizioneManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/iscrizioni.json",
                new TypeToken<List<Iscrizione>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(Iscrizione iscrizione) {


        return false;
    }

    public List<Iscrizione> getIscrizioniDaStudente(String cfStudente){
        List<Iscrizione> iscrizioni= new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            if(this.lista.get(i).getRidStudenteCf().toLowerCase().equals(cfStudente.toLowerCase())){
                iscrizioni.add(this.lista.get(i));
            }
        }
        return iscrizioni;
    }

    public List<Iscrizione> getIscrizioniDaAppello(String idAppello){
        List<Iscrizione> iscrizioni = new ArrayList<>();
        for(int i = 0; i < this.lista.size(); i++){
            if(String.valueOf(this.lista.get(i).getRidAppello()).equals(idAppello)){
                iscrizioni.add(this.lista.get(i));
            }
        }
        return iscrizioni;
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
