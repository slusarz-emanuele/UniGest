package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collection;
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
    public boolean aggiorna(Appello nuovo) {

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

    public List<Appello> getAppelliDaInsegnamenti(String idInsegnamento){
        List<Appello> appelli = new ArrayList<>();
        for(int i = 0; i < this.lista.size(); i++){
            if(this.lista.get(i).getRidInsegnamento().equals(idInsegnamento)){
                appelli.add(this.lista.get(i));
            }
        }
        return appelli;
    }

    public Appello getAppelloDaVerbale(String idVerbale){
        for(int i = 0; i < this.lista.size(); i++){
            if(String.valueOf(this.lista.get(i).getId()).equals(idVerbale)){
                return this.lista.get(i);
            }
        }
        return null;
    }

    public Collection<Appello> getAppelliDaInsegnamento(String id) {
        List<Appello> appelli = new ArrayList<>();
        System.out.println("ENTRO NELLA FUNZIONE");
        for (int i = 0; i < this.lista.size(); i++) {
            Appello appello = this.lista.get(i);
            System.out.println("INS EXT :" + appello.getRidInsegnamento() + "INS IMPORT:" + id);
            if (id.equalsIgnoreCase(appello.getRidInsegnamento())) {
                appelli.add(appello);
            }
        }
        return appelli;
    }

}
