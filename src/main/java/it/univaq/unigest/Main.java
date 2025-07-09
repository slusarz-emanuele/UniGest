package it.univaq.unigest;

import it.univaq.unigest.manager.StudenteManager;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LocalDateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String [] Args){

        DatabaseHelper.verFileLog();
        DatabaseHelper.verDirData();
        DatabaseHelper.verFilesData();

        StudenteManager studenteManager = new StudenteManager();

        Studente s = new Studente(
                "RSSMRA00A01H501Z",                        // cf
                "Mario",                                   // nome
                "Rossi",                                   // cognome
                "mario.rossi@studenti.univaq.it",          // email
                LocalDateUtil.fromString("2000-01-01"),                              // dataNascita (formato stringa)
                "2019-10-01",                              // dataIngressoUniversita (formato stringa)
                "123456",                                  // matricola
                new CorsoDiLaurea(),                        // corsoDiLaurea
                LocalDate.of(2019, 10, 1),                 // dataImmatricolazione
                new ArrayList<>(),                         // lista esami (vuota per ora)
                90                                         // cfu
        );


        // Aggiunta
        studenteManager.aggiungi(s);

        // Ricerca con filtro dinamico
        Studente filtro = new Studente();
        filtro.setCognome("Rossi");

        List<Studente> risultati = studenteManager.filtra(filtro);

        // Stampa risultati
        System.out.println("Studenti trovati:");
        for (Studente trovato : risultati) {
            System.out.println(trovato);
        }

        // Test aggiornamento
        Studente sAggiornato = new Studente(
                "RSSMRA00A01H501Z",
                "Mario",
                "Bianchi", // cambio cognome
                "mario.bianchi@studenti.univaq.it",
                LocalDateUtil.fromString("2000-01-01"),
                "2019-10-01",
                "123456",
                new CorsoDiLaurea(),
                LocalDate.of(2019, 10, 1),
                new ArrayList<>(),
                120 // aggiornato
        );
        studenteManager.aggiorna("RSSMRA00A01H501Z", sAggiornato);

        // Ristampa dopo aggiornamento
        System.out.println("Dopo aggiornamento:");
        for (Studente stud : studenteManager.getAll()) {
            System.out.println(stud);
        }


    }

}
