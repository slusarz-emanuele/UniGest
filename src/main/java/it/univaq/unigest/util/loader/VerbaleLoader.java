package it.univaq.unigest.util.loader;

import it.univaq.unigest.manager.EsameManager;
import it.univaq.unigest.manager.IscrizioneManager;
import it.univaq.unigest.manager.VerbaleManager;
import it.univaq.unigest.model.Verbale;

public class VerbaleLoader {

    public static void initVerbali(VerbaleManager verbaleManager, IscrizioneManager iscrizioneManager, EsameManager esameManager){
        caricaEsamiPerOgniVerbale(verbaleManager, iscrizioneManager, esameManager);
    }

    public static void caricaEsamiPerOgniVerbale(VerbaleManager verbaleManager, IscrizioneManager iscrizioneManager, EsameManager esameManager){
        for(Verbale v : verbaleManager.getAll()){
            v.caricaEsamiDinamicamente(iscrizioneManager, esameManager);
        }
    }

}
