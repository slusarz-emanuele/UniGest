package it.univaq.unigest.gui.actions;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.ColonneMiniFactory;
import it.univaq.unigest.gui.componenti.RelatedListStage;
import it.univaq.unigest.model.*;
import it.univaq.unigest.service.query.DomainQueryService;

import java.util.Collections;

/**
 * Classe che si occupa delle query del dominio
 * e dell'apertura dei pannelli ad esse dedicate.
 * <p>
 * Ogni metodo della classe riceve un'entità (ad es. {@link CorsoDiLaurea}, {@link Studente},
 * {@link Docente}, {@link Appello}) e apre un pannello
 * {@link RelatedListStage} con i dati collegati, recuperati attraverso il
 * {@link DomainQueryService}.
 * </p>
 *
 * <p>Le colonne delle tabelle sono costituite utilizzando {@link ColonneMiniFactory}.</p>
 */
public class QueryActions {

    private final DomainQueryService query;

    /**
     * Costruisce un oggetto {@code QueryActions}.
     *
     * @param query service che riporta le query del dominio
     */
    public QueryActions(DomainQueryService query) {
        this.query = query;
    }


    // ===================== Corsi di Laurea =====================

    // corsi di laurea -> insegnamenti
    /**
     * Apre il pannello con la lista degli insegnamenti
     * previsti per un determinato corso di laurea.
     *
     * @param corso corso di laurea di riferimento
     */
    public void openInsegnamentiPerCorso(CorsoDiLaurea corso) {
        RelatedListStage.open(
                "Insegnamenti del Corso: " + corso.getNome(),
                () -> query.insegnamentiByCorso(corso.getId()),
                ColonneMiniFactory.insegnamentoMini(),
                "Insegnamenti_" + corso.getNome()
        );
    }

    // corsi di laurea -> studenti
     /**
     * Apre il pannello con la lista degli studenti
     * iscritti a un corso di laurea.
     *
     * @param corso corso di laurea di riferimento
     */
    public void openStudentiPerCorso(CorsoDiLaurea corso) {
        RelatedListStage.open(
                "Studenti del Corso: " + corso.getNome(),
                () -> query.studentiByCorso(corso.getId()),
                ColonneMiniFactory.studenteMini(),
                "Studenti_" + corso.getNome()
        );
    }

    // studente -> esami
    /**
     * Apre il pannello con la lista degli esami
     * svolti da uno studente.
     *
     * @param s studente di riferimento
     */
    public void openEsamiPerStudente(Studente s) {
        RelatedListStage.open(
                "Esami di " + s.getNome() + " " + s.getCognome(),
                () -> query.esamiByStudente(s.getCf()),
                ColonneMiniFactory.esameMini(),
                "Esami_" + s.getMatricola()
        );
    }

    // studente -> iscrizioni
    /**
     * Apre il pannello con la lista delle iscrizioni
     * relaative ad uno studente.
     *
     * @param s studente di riferimento
     */
    public void openIscrizioniPerStudente(Studente s) {
        RelatedListStage.open(
                "Iscrizioni di " + s.getNome() + " " + s.getCognome(),
                () -> query.iscrizioniByStudente(s.getCf()),
                ColonneMiniFactory.iscrizioneMini(),
                "Iscrizioni_" + s.getMatricola()
        );
    }

    // ===================== Docenti =====================

    // docenti -> insegnamenti, appelli, verbali
    /**
     * Apre il pannello con la lista degli insegnamenti
     * svolti da un docente.
     *
     * @param d docente di riferimento
     */
    public void openInsegnamentiPerDocente(Docente d) {
        RelatedListStage.open(
                "Insegnamenti del Docente: " + d.getCognome(),
                () -> query.insegnamentiByDocente(d.getCf()),
                ColonneMiniFactory.insegnamentoMini(),
                "Insegnamenti_Docente_" + d.getCf()
        );
    }

    /**
     * Apre il pannello con la lista degli appelli
     * gestiti da un docente.
     *
     * @param d docente di riferimento
     */
    public void openAppelliPerDocente(Docente d) {
        RelatedListStage.open(
                "Appelli del Docente: " + d.getCognome(),
                () -> query.appelliByDocente(d.getCf()),
                ColonneMiniFactory.appelloMini(),
                "Appelli_Docente_" + d.getCf()
        );
    }

    /**
     * Apre il pannello con la lista dei verbali
     * redatti da un docente.
     *
     * @param d docente di riferimento
     */
    public void openVerbaliPerDocente(Docente d) {
        RelatedListStage.open(
                "Verbali del Docente: " + d.getCognome(),
                () -> query.verbaliByDocente(d.getCf()),
                ColonneMiniFactory.verbaleMini(),
                "Verbali_Docente_" + d.getCf()
        );
    }

    // ===================== Insegnamenti =====================

    // insegnamento -> appelli
    /**
     * Apre il pannello con la lista degli appelli
     * legati ad un insegnamento.
     *
     * @param i insegnamento di riferimento
     */
    public void openAppelliPerInsegnamento(Insegnamento i) {
        RelatedListStage.open(
                "Appelli per: " + i.getNome(),
                () -> query.appelliByInsegnamento(i.getId()),
                ColonneMiniFactory.appelloMini(),
                "Appelli_" + i.getNome()
        );
    }

    // ===================== Appelli =====================

    // appello -> iscrizioni
    /**
     * Apre il pannello con la lista delle iscrizioni
     * riportate in un appello.
     *
     * @param a appello di riferimento
     */
    public void openIscrizioniPerAppello(Appello a) {
        RelatedListStage.open(
                "Iscrizioni Appello #" + a.getId(),
                () -> query.iscrizioniByAppello(a.getId()),
                ColonneMiniFactory.iscrizioneMini(),
                "Iscrizioni_Appello_" + a.getId()
        );
    }

    // appello -> verbale (al massimo uno)
     /**
     * Apre il pannello con il verbale associato a un appello.
     *
     * @param a appello di riferimento
     */
    public void openVerbalePerAppello(Appello a) {
        var opt = query.verbaleByAppello(a.getId());
        if (opt.isEmpty()) {
            Dialogs.showInfo("Nessun verbale", "L'appello #" + a.getId() + " non ha ancora un verbale.");
            return;
        }
        Verbale v = opt.get();
        RelatedListStage.open(
                "Verbale per Appello #" + a.getId(),
                () -> Collections.singletonList(v),
                ColonneMiniFactory.verbaleMini(),
                "Verbale_Appello_" + a.getId()
        );
    }
}
