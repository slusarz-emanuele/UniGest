package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;
import java.util.stream.Collectors;

public class EdificioManager extends AbstractManager<Edificio> {

    public EdificioManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/edifici.json",
                new TypeToken<List<Edificio>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(Edificio edificio) {

        return false;
    }

    @Override
    public List<Edificio> filtra(Edificio filtro) {
        return lista.stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getNome() == null || e.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .collect(Collectors.toList());
    }


    public String getNomeEdificioDaId(String id) {
        return lista.stream()
                .filter(e -> e.getId().equalsIgnoreCase(id))
                .map(Edificio::getNome)
                .findFirst()
                .orElse("Sconosciuto");
    }

}
