package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;
import java.util.stream.Collectors;

public class AulaManager extends AbstractManager<Aula> {

    public AulaManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/aule.json",
                new TypeToken<List<Aula>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(Aula nuova) {

        return false;
    }

    @Override
    public List<Aula> filtra(Aula filtro) {
        return lista.stream()
                .filter(a -> filtro.getId() == null || a.getId().equalsIgnoreCase(filtro.getId()))
                .filter(a -> filtro.getCapienza() == 0 || a.getCapienza() == filtro.getCapienza())
                .filter(a -> filtro.getEdificio() == null || a.getEdificio().toLowerCase().contains(filtro.getEdificio().toLowerCase()))
                .collect(Collectors.toList());
    }

    public String getAulaNomeDaId(String id) {

        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(id)){
                return lista.get(i).getEdificioNome() + " " + lista.get(i).getId();
            }
        }

        return "";
    }
}
