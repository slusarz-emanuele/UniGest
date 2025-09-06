package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.manager.exceptions.EsameConIdPresente;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class EsameManager extends AbstractManager<Esame> {

    private static final Logger LOGGER = LogManager.getLogger(EsameManager.class);

    public EsameManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/esami.json",
                new TypeToken<List<Esame>>() {}.getType()
        );
    }

    /**
     * Aggiorna un esame selezionato tramite id con un nuovo esame avente sempre lo stesso id.
     *
     * @param esame   Nuovo oggetto Esame con i dati aggiornati
     * @return true se l'aggiornamento è andato a buon fine, false altrimenti
     */
    @Override
    public boolean aggiorna(Esame esame) {
        // 1. Cerco l'indice dell'esame esistente con lo stesso ID
        int indice = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(esame.getId())) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            LOGGER.warn("Esame non trovato con ID: " + esame.getId());
            return false;
        }

        boolean giaPresente = lista.stream()
                .anyMatch(e -> !e.getId().equals(esame.getId()) &&
                        e.getIscrizioneId().equals(esame.getIscrizioneId()));


        if(giaPresente){
            LOGGER.warn("Esame già presente per iscrizioneId: " + esame.getIscrizioneId());
            throw new EsameConIdPresente("Un iscrizione può essere associata ad un solo esame!");
        }

        this.lista.set(indice, esame);
        salvaSuFile();
        LOGGER.info("Esame " + esame.getId() + " aggiornato con successo.");
        return true;
    }

    public void aggiungi(Esame elemento) {
        boolean giaPresente = lista.stream()
                .anyMatch(e -> e.getIscrizioneId().equals(elemento.getIscrizioneId()));

        if (giaPresente) {
            LOGGER.warn("Esame già presente per iscrizioneId: " + elemento.getIscrizioneId());
            throw new EsameConIdPresente("Un iscrizione può essere associata ad un solo esame!");
        }

        lista.add(elemento);
        salvaSuFile();
        LOGGER.info(elemento.toString() + " aggiunto");
    }

    public Esame getEsameDaIscrizione(String idEsame){
        for(int i = 0; i < this.lista.size(); i++){
            if(this.lista.get(i).getId().equals(idEsame)){
                return this.lista.get(i);
            }
        }
        return null;
    }

    /**
     * Filtra la lista degli esami in base ai campi non nulli del filtro passato.
     *
     * @param filtro Oggetto Esame contenente i campi da filtrare
     * @return Lista filtrata di esami
     */
    @Override
    public List<Esame> filtra(Esame filtro) {
        return lista.stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getIscrizioneId() == null || e.getIscrizioneId().equalsIgnoreCase(filtro.getIscrizioneId()))
                .filter(e -> filtro.getVoto() == null || e.getVoto().equals(filtro.getVoto()))
                .filter(e -> filtro.getCfu() == null || e.getCfu().equals(filtro.getCfu()))
                .filter(e -> !filtro.isLode() || e.isLode())
                .filter(e -> !filtro.isRifiutato() || e.isRifiutato())
                .filter(e -> !filtro.isVerbalizzato() || e.isVerbalizzato())
                .collect(Collectors.toList());
    }
}
