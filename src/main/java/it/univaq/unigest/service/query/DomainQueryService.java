package it.univaq.unigest.service.query;

import it.univaq.unigest.model.*;
import java.util.List;
import java.util.Optional;

public interface DomainQueryService {

    // studente -> esami, iscrizioni
    List<Esame>       esamiByStudente(String studenteCf);
    List<Iscrizione>  iscrizioniByStudente(String studenteCf);

    // docenti -> insegnamenti, appelli, verbali
    List<Insegnamento> insegnamentiByDocente(String docenteId);
    List<Appello>      appelliByDocente(String docenteId);
    List<Verbale>      verbaliByDocente(String docenteId);

    // corsi di laurea -> insegnamenti, studenti
    List<Insegnamento> insegnamentiByCorso(String corsoId);
    List<Studente>     studentiByCorso(String corsoId);

    // insegnamenti -> appelli
    List<Appello> appelliByInsegnamento(String insegnamentoId);

    // appelli -> iscrizioni, verbali
    List<Iscrizione> iscrizioniByAppello(String appelloId);
    Optional<Verbale> verbaleByAppello(String appelloId);

    // iscrizioni -> esame
    Optional<Esame> esameByIscrizione(String iscrizioneId);

    // verbali -> visualizza appello
    Optional<Appello> appelloByVerbale(String verbaleId);
}
