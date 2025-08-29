package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.gui.Main;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.util.DatabaseHelper;

import java.util.List;

public class CorsoDiLaureaManager extends AbstractManager<CorsoDiLaurea> {

    public CorsoDiLaureaManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/corsiDiLaurea.json",
                new TypeToken<List<CorsoDiLaurea>>() {}.getType()
        );
    }

    public String getNomeCorsoDiLauraDaID(String id){
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(id)){
                return lista.get(i).getNome();
            }
        }
        return "";
    }

    @Override
    public boolean aggiorna(CorsoDiLaurea nuovo) {

        for(int i = 0; i < this.lista.size(); i++){
            if(nuovo.getId().equals(this.lista.get(i).getId())){
                this.lista.get(i).setCfuTotali(nuovo.getCfuTotali());
                this.lista.get(i).setDipartimento(nuovo.getDipartimento());
                this.lista.get(i).setNome(nuovo.getNome());
                this.lista.get(i).setCoordinatoreId(nuovo.getNome());
                return true;
            }
        }
        return false;
    }

    public int getNumeroStudentiAppartenentiAlCorsoDalNome(String nomeCorsoDiLaurea) {
        // Trova il corso corrispondente al nome
        String idCorso = lista.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nomeCorsoDiLaurea))
                .map(CorsoDiLaurea::getId)
                .findFirst()
                .orElse(null);

        if (idCorso == null) {
            return 0; // Nessun corso trovato con quel nome
        }

        // Conta gli studenti che hanno come corsoDiLaurea l'ID trovato
        return (int) Main.getStudenteManager().getAll().stream()
                .filter(s -> idCorso.equals(s.getCorsoDiLaurea()))
                .count();
    }



    public List<CorsoDiLaurea> filtra(CorsoDiLaurea filtro){
        return lista.stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                //.filter()
                .toList();
    }

}
