package it.univaq.unigest.gui.actions;

import it.univaq.unigest.gui.componenti.ColonneMiniFactory;
import it.univaq.unigest.gui.componenti.RelatedListStage;
import it.univaq.unigest.model.*;
import it.univaq.unigest.service.query.DomainQueryService;

public class QueryActions {

    private final DomainQueryService query;

    public QueryActions(DomainQueryService query) {
        this.query = query;
    }

    // corsi di laurea -> insegnamenti
    public void openInsegnamentiPerCorso(CorsoDiLaurea corso) {
        RelatedListStage.open(
                "Insegnamenti del Corso: " + corso.getNome(),
                () -> query.insegnamentiByCorso(corso.getId()),
                ColonneMiniFactory.insegnamentoMini(),
                "Insegnamenti_" + corso.getNome()
        );
    }

    // corsi di laurea -> studenti
    public void openStudentiPerCorso(CorsoDiLaurea corso) {
        RelatedListStage.open(
                "Studenti del Corso: " + corso.getNome(),
                () -> query.studentiByCorso(corso.getId()),
                ColonneMiniFactory.studenteMini(),
                "Studenti_" + corso.getNome()
        );
    }

    // studente -> esami
    public void openEsamiPerStudente(Studente s) {
        RelatedListStage.open(
                "Esami di " + s.getNome() + " " + s.getCognome(),
                () -> query.esamiByStudente(s.getCf()),
                ColonneMiniFactory.esameMini(),
                "Esami_" + s.getMatricola()
        );
    }

    // studente -> iscrizioni
    public void openIscrizioniPerStudente(Studente s) {
        RelatedListStage.open(
                "Iscrizioni di " + s.getNome() + " " + s.getCognome(),
                () -> query.iscrizioniByStudente(s.getCf()),
                ColonneMiniFactory.iscrizioneMini(),
                "Iscrizioni_" + s.getMatricola()
        );
    }

    // docenti -> insegnamenti, appelli, verbali
    public void openInsegnamentiPerDocente(Docente d) {
        RelatedListStage.open(
                "Insegnamenti del Docente: " + d.getCognome(),
                () -> query.insegnamentiByDocente(d.getCf()),
                ColonneMiniFactory.insegnamentoMini(),
                "Insegnamenti_Docente_" + d.getCf()
        );
    }

    public void openAppelliPerDocente(Docente d) {
        RelatedListStage.open(
                "Appelli del Docente: " + d.getCognome(),
                () -> query.appelliByDocente(d.getCf()),
                ColonneMiniFactory.appelloMini(),
                "Appelli_Docente_" + d.getCf()
        );
    }

    public void openVerbaliPerDocente(Docente d) {
        RelatedListStage.open(
                "Verbali del Docente: " + d.getCognome(),
                () -> query.verbaliByDocente(d.getCf()),
                ColonneMiniFactory.verbaleMini(),
                "Verbali_Docente_" + d.getCf()
        );
    }

    // insegnamento -> appelli
    public void openAppelliPerInsegnamento(Insegnamento i) {
        RelatedListStage.open(
                "Appelli per: " + i.getNome(),
                () -> query.appelliByInsegnamento(i.getId()),
                ColonneMiniFactory.appelloMini(),
                "Appelli_" + i.getNome()
        );
    }

    // appello -> iscrizioni
    public void openIscrizioniPerAppello(Appello a) {
        RelatedListStage.open(
                "Iscrizioni Appello #" + a.getId(),
                () -> query.iscrizioniByAppello(a.getId()),
                ColonneMiniFactory.iscrizioneMini(),
                "Iscrizioni_Appello_" + a.getId()
        );
    }
}
