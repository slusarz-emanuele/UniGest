package it.univaq.unigest.util;

import java.io.File;
import java.io.IOException;

public class DatabaseHelper {

    // Attributi di classe
    public static final String PERCORSO_CARTELLA_DATI = "src/main/resources/data";

    /**
     * Verifica la presenza delle cartelle necessarie al funzionamento della persistenza di dati
     *
     */
    public static void verDirData(){
        File cartella = new File(PERCORSO_CARTELLA_DATI);

        if(!cartella.exists()){
            if(cartella.mkdirs()){
                LogHelper.saveLog(LogType.INFO,"Cartella creata");
            }
        }else{
            // La cartella non esiste
            LogHelper.saveLog(LogType.INFO,"Cartella già esistente");
        }
    }

    /**
     * Verifica la presenza di tutti i file necessari alla persistenza
     *
     */
    public static void verFilesData(){
        File[] files = new File[9];

        files[0] = new File(PERCORSO_CARTELLA_DATI, "studenti.json");
        files[1] = new File(PERCORSO_CARTELLA_DATI, "professori.json");
        files[2] = new File(PERCORSO_CARTELLA_DATI, "appelli.json");
        files[3] = new File(PERCORSO_CARTELLA_DATI, "aule.json");
        files[4] = new File(PERCORSO_CARTELLA_DATI, "corsiDiLaurea.json");
        files[5] = new File(PERCORSO_CARTELLA_DATI, "esami.json");
        files[6] = new File(PERCORSO_CARTELLA_DATI, "insegnamenti.json");
        files[7] = new File(PERCORSO_CARTELLA_DATI, "iscrizioni.json");
        files[8] = new File(PERCORSO_CARTELLA_DATI, "verbale.json");

        for(int i = 0; i < files.length; i++){
            try{
                if(!files[i].exists()){
                    if(files[i].createNewFile()){
                        LogHelper.saveLog(LogType.INFO,"LOG PROVVISORIO: Il file " + files[i].getName() + " è stato creato con successo");
                    }
                }else{
                    // Il file esiste
                    LogHelper.saveLog(LogType.INFO,"LOG PROVVISORIO: Il file " + files[i].getName() + " è esistente");
                }
            }catch (IOException e){
                LogHelper.saveLog(LogType.ERROR,"LOG PROVVISORIO: Il file " + files[i].getName() + " non è stato creato per via di un eccezione" + e.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * Verifica la presenza del file di log
     *
     */
    public static void verFileLog(){
        File file = new File(PERCORSO_CARTELLA_DATI, "log.txt");

        try {
            if(!file.exists()){
                if(file.createNewFile()){
                    System.out.println("LOG PROVVISORIO: Il file " + file.getName() + " è stato creato con successo");
                }
            }else{
                System.out.println("LOG PROVVISORIO: Il file " + file.getName() + " è esistente");
            }
        }catch (IOException e){
            System.out.println("LOG PROVVISORIO: Il file " + file.getName() + " non è stato creato per via di un eccezione" + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Carica in memoria tutti i dati necessari all'avvio del software
     */
    public void caricaDatiInMemoria(){
        //StudenteManager.caricaDaFile();
    }
}
