package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.manager.exceptions.EsameConIdPresente;
import it.univaq.unigest.manager.exceptions.VerbaleConAppelloPresente;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class VerbaleManager extends AbstractManager<Verbale> {

    private static final Logger LOGGER = LogManager.getLogger(VerbaleManager.class);

    public VerbaleManager(){
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/verbale.json",
                new TypeToken<List<Verbale>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(Verbale nuovo) {
        int indice = -1;
        for(int i = 0; i < lista.size(); i++){
            if(lista.get(i).getId().equals(nuovo.getId())){
                indice = i;
                break;
            }
        }

        if(indice == -1){
            LOGGER.warn("Verbale non trovato con ID: " + nuovo.getId());
            return false;
        }

        boolean giaPresente = lista.stream()
                .anyMatch(v -> !v.getId().equals(nuovo.getId()) &&
                        v.getAppelloId().equals(nuovo.getAppelloId()));


        if(giaPresente){
            LOGGER.warn("Verbale già presente per appelloId: " + nuovo.getAppelloId());
            throw new EsameConIdPresente("Un appello può avere un solo verbale!");
        }

        nuovo.setEsami(this.lista.get(indice).getEsami());

        this.lista.set(indice, nuovo);
        salvaSuFile();
        LOGGER.info("Verbale " + nuovo.getId() + " aggiornato con successo.");
        return true;
    }

    public void aggiungi(Verbale elemento) {
        boolean giaPresente = lista.stream()
                .anyMatch(v -> v.getAppelloId().equals(elemento.getAppelloId()));

        if (giaPresente) {
            LOGGER.warn("Verbale già presente per appelloId: " + elemento.getAppelloId());
            throw new VerbaleConAppelloPresente("Un appello può avere un solo verbale!");
        }

        lista.add(elemento);
        salvaSuFile();
        LOGGER.info(elemento.toString() + " aggiunto");
    }


    public List<Verbale> getVerbaliDaAppello(String idAppello){
        List<Verbale> verbali = new ArrayList<>();
        for(int i = 0; i < this.lista.size(); i++){
            if(String.valueOf(this.lista.get(i).getAppelloId()).equals(idAppello)){
                verbali.add(this.lista.get(i));
            }
        }
        return verbali;
    }

    @Override
    public List<Verbale> filtra(Verbale filtro) {
        return lista.stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                .filter(v -> filtro.getAppelloId() == null || v.getAppelloId().equalsIgnoreCase(filtro.getAppelloId()))
                .filter(v -> filtro.getDataChiusura() == null || v.getDataChiusura().equals(filtro.getDataChiusura()))
                .filter(v -> !filtro.getChiuso() || v.getChiuso()) // true → filtra solo chiusi
                .filter(v -> !filtro.getFirmato() || v.getFirmato()) // true → filtra solo firmati
                .filter(v -> filtro.getNote() == null || (v.getNote() != null && v.getNote().toLowerCase().contains(filtro.getNote().toLowerCase())))
                .toList();
    }

}
