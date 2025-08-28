package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;
import java.util.stream.Collectors;

public class DocenteManager extends AbstractManager<Docente> {

    public DocenteManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/professori.json",
                new TypeToken<List<Docente>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(Docente docente) {

        return false;
    }

    // Metodi
    public String getGeneralitaDaCf(String cf){
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getCf().equals(cf)){
                return lista.get(i).getNome() + " " + lista.get(i).getCognome() + " (ID: " + lista.get(i).getCodiceDocente() + ")";
            }
        }
        return "";
    }

    @Override
    public List<Docente> filtra(Docente filtro) {
        return lista.stream()
                .filter(d -> filtro.getCodiceDocente() == null || d.getCodiceDocente().equalsIgnoreCase(filtro.getCodiceDocente()))
                .filter(d -> filtro.getNome() == null || d.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(d -> filtro.getCognome() == null || d.getCognome().toLowerCase().contains(filtro.getCognome().toLowerCase()))
                .filter(d -> filtro.getEmail() == null || d.getEmail().toLowerCase().contains(filtro.getEmail().toLowerCase()))
                .filter(d -> filtro.getDataNascita() == null || d.getDataNascita().equals(filtro.getDataNascita()))
                .filter(d -> filtro.getDataIngressoUniversita() == null || d.getDataIngressoUniversita().equals(filtro.getDataIngressoUniversita()))
                .filter(d -> filtro.getDipartimento() == null || d.getDipartimento().toLowerCase().contains(filtro.getDipartimento().toLowerCase()))
                .filter(d -> filtro.getQualifica() == null || d.getQualifica().toLowerCase().contains(filtro.getQualifica().toLowerCase()))
                .collect(Collectors.toList());
    }
}
