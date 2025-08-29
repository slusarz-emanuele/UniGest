package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsegnamentoManager extends AbstractManager<Insegnamento> {

    public InsegnamentoManager() {
        super(DatabaseHelper.PERCORSO_CARTELLA_DATI + "/insegnamenti.json",
                new TypeToken<List<Insegnamento>>() {}.getType());
    }

    @Override
    public boolean aggiorna(Insegnamento nuovo) {

        return false;
    }

    public List<Insegnamento> getInsegnamentiDaDocente(String idDocente) {
        List<Insegnamento> insegnamenti = new ArrayList<>();
        for (Insegnamento ins : this.lista) {
            for (String docenteStr : ins.getDocenti()) {
                String idEstratto = Parser.estraiIdDaStringaDocente(docenteStr);
                if (idEstratto.equalsIgnoreCase(idDocente)) {
                    insegnamenti.add(ins);
                    break; // se già trovato, non serve continuare
                }
            }
        }
        return insegnamenti;
    }

    public List<Insegnamento> getInsegnamentoDaCorsoDiLaurea(String idCDL){
        List<Insegnamento> insegnamenti = new ArrayList<>();
        for(int i = 0; i < this.lista.size(); i++){
            if(this.lista.get(i).getCorsoDiLaureaId().equals(idCDL)){
                insegnamenti.add(this.lista.get(i));
            }
        }
        return insegnamenti;
    }



    @Override
    public List<Insegnamento> filtra(Insegnamento filtro) {
        return lista.stream()
                .filter(i -> filtro.getId() == null || i.getId().equalsIgnoreCase(filtro.getId()))
                .filter(i -> filtro.getNome() == null || i.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(i -> filtro.getCfu() == null || i.getCfu().equals(filtro.getCfu()))
                .filter(i -> filtro.getCorsoDiLaureaId() == null || i.getCorsoDiLaureaId().equalsIgnoreCase(filtro.getCorsoDiLaureaId()))
                .filter(i -> filtro.getDocenti() == null || filtro.getDocenti().isEmpty() ||
                        i.getDocenti().stream().anyMatch(doc -> filtro.getDocenti().contains(doc)))
                .filter(i -> filtro.getAnno() == null || i.getAnno().equals(filtro.getAnno()))
                .filter(i -> filtro.getSemestre() == null || i.getSemestre().equals(filtro.getSemestre()))
                .collect(Collectors.toList());
    }
}
