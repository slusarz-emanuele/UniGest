package it.univaq.unigest.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LocalDateAdapter;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EsameManager {

    private static final String PERCORSO_DATI = DatabaseHelper.PERCORSO_CARTELLA_DATI + "/esami.json";
    private List<Esame> esami;

    public EsameManager(){
        esami = new ArrayList<>();
    }

    /**
     * Permette di caricare dai file json tutti i dati in memoria
     */
    public void caricaDaFile(){

        try{
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                    .create();
            FileReader file = new FileReader(PERCORSO_DATI);
            Type listaDaJson = new TypeToken<List<Esame>>(){}.getType();
            this.esami = gson.fromJson(file, listaDaJson);

            if(esami == null){
                esami = new ArrayList<>();
            }

            file.close();
            LogHelper.saveLog(LogType.DEBUG, "I dati dal file " + PERCORSO_DATI + " sono stati caricati in memoria");
        }catch(IOException e){
            LogHelper.saveLog(LogType.ERROR, "Errore nella lettura del file " + PERCORSO_DATI + " , con errore: " + e.getMessage());
        }

    }

    /**
     * Salva su file tutte le modifiche effettuate per evitare di perdere nuovi record
     */
    public void salvaSuFile(){

        try{
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                    .create();
            FileWriter file = new FileWriter(PERCORSO_DATI);
            gson.toJson(this.esami, file);
            file.close();
            LogHelper.saveLog(LogType.DEBUG, "I dati dal file " + PERCORSO_DATI + " sono stati scritti sul file json");
        }catch (IOException e){
            LogHelper.saveLog(LogType.ERROR, "Errore nella scrittura del file " + PERCORSO_DATI + " , con errore: " + e.getMessage());
        }

    }

    /**
     * Inserisce un nuovo esame nella tabella dati esami
     * @param esame L'esame da passare alla funzione da aggiungere
     */
    public void aggiungiEsame(Esame esame){
        for(int i = 0; i < this.esami.size(); i++){
            if(this.esami.get(i).getId().equalsIgnoreCase(esame.getId())){
                LogHelper.saveLog(LogType.WARNING, "Esame già presente: " + esame.getId());
                return;
            }
        }

        this.esami.add(esame);
        salvaSuFile();
        LogHelper.saveLog(LogType.INFO, esame.toString() + " è stato aggiunto alla lista degli esami");
    }

    /**
     * Rimuove un esame dalla tabelle dati esami
     * @param esame L'esame da passare alla funzione da rimuovere
     */
    public void rimuoviEsame(Esame esame){

        if(this.esami.remove(esame)){
            salvaSuFile();
            LogHelper.saveLog(LogType.INFO, esame.toString() + " è stato rimosso alla lista degli esami");
        }

    }

    /**
     * Aggiorna un esame selezionato tramite id con un nuovo esame avente sempre lo stesso id e nuovi attributi
     * @param idEsame Codice identificativo dell'esame da aggiornare
     * @param esame Nuovo Ogetto ti tipo Esame conforme al codice identificativo da cambiare
     * @return Ritorna vero se l'operazione è andata a buon fine, falso per il contrario
     */
    public boolean aggiornaEsame(String idEsame, Esame esame){

        for(int i = 0; i < this.esami.size(); i++){
            Esame s = this.esami.get(i);

            if(idEsame.equals(s.getId())){
                esami.set(i, esame);
                salvaSuFile();
                LogHelper.saveLog(LogType.INFO, "La modifica per l'esame " + idEsame + " è stata effettuata");
                return true;
            }

        }

        LogHelper.saveLog(LogType.ERROR, "La modifica non è stata effettuata poichè i codici fiscali non corrispondono per lo studente: " + idEsame);
        return false;

    }

    /**
     * Restituisce l'elenco degli esami che metchano il filtro
     * @param filtro Ogetto di tipo Esami con i filtri
     * @return Lista di esami
     */
    public List<Esame> filtra(Esame filtro) {
        return esami.stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getIscrizioneId() == null || e.getIscrizioneId().equalsIgnoreCase(filtro.getIscrizioneId()))
                .filter(e -> filtro.getVoto() == null || e.getVoto().equals(filtro.getVoto()))
                .filter(e -> filtro.getCfu() == null || e.getCfu().equals(filtro.getCfu()))
                .filter(e -> !filtro.isLode() || e.isLode()) // se filtro.lode è true, vogliamo solo quelli con lode
                .filter(e -> !filtro.isRifiutato() || e.isRifiutato())
                .filter(e -> !filtro.isVerbalizzato() || e.isVerbalizzato())
                .collect(Collectors.toList());
    }

    /**
     * Restituisce l'intera lista di esami
     * @return
     */
    public List<Esame> getEsami(){
        return this.esami;
    }

}
