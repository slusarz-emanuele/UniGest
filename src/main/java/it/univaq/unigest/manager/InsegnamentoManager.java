package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;
import java.util.stream.Collectors;

public class InsegnamentoManager extends AbstractManager<Insegnamento> {

    public InsegnamentoManager() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/insegnamenti.json",
                new TypeToken<List<Insegnamento>>() {}.getType());
    }

    @Override
    public boolean aggiorna(String id, Insegnamento nuovo) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equalsIgnoreCase(id)) {
                lista.set(i, nuovo);
                salvaSuFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Insegnamento> filtra(Insegnamento filtro) {
        return lista.stream()
                .filter(i -> filtro.getId() == null || i.getId().equalsIgnoreCase(filtro.getId()))
                .filter(i -> filtro.getNome() == null || i.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(i -> filtro.getCfu() == null || i.getCfu().equals(filtro.getCfu()))
                .filter(i -> filtro.getCorsoDiLaureaId() == null || i.getCorsoDiLaureaId().equalsIgnoreCase(filtro.getCorsoDiLaureaId()))
                .filter(i -> filtro.getDocenteId() == null || i.getDocenteId().equalsIgnoreCase(filtro.getDocenteId()))
                .filter(i -> filtro.getAnno() == null || i.getAnno().equals(filtro.getAnno()))
                .filter(i -> filtro.getSemestre() == null || i.getSemestre().equals(filtro.getSemestre()))
                .collect(Collectors.toList());
    }
}
