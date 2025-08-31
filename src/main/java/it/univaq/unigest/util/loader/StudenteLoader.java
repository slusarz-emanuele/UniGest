//package it.univaq.unigest.util.loader;
//
//import it.univaq.unigest.manager.EsameManager;
//import it.univaq.unigest.manager.IscrizioneManager;
//import it.univaq.unigest.manager.StudenteManager;
//import it.univaq.unigest.model.Studente;
//
//public class StudenteLoader {
//
//    public static void initStudenti(StudenteManager studenteManager, IscrizioneManager iscrizioneManager, EsameManager esameManager){
//        caricaEsamiPerOgniStudente(studenteManager, iscrizioneManager, esameManager);
//    }
//
//    public static void caricaEsamiPerOgniStudente(StudenteManager studenteManager, IscrizioneManager iscrizioneManager, EsameManager esameManager){
//        for (Studente s : studenteManager.getAll()) {
//            s.caricaEsamiDinamicamente(iscrizioneManager, esameManager);
//        }
//    }
//
//}
