package it.univaq.unigest.service.query;

import it.univaq.unigest.model.*;
import java.util.List;
import java.util.Optional;

/**
 * Servizio di query “di dominio” che espone viste/relazioni tra entità
 * aggregando i dati dai vari servizi CRUD (StudenteService, AppelloService, ecc.).
 *
 * <p>Scopi principali:
 * <ul>
 *   <li>Incapsulare la logica di join/filter tra modelli (es. “tutti gli appelli di un docente”).</li>
 *   <li>Fornire un punto unico e riusabile per le query usate dalla GUI.</li>
 * </ul>
 *
 * <p><b>Note d’uso:</b>
 * <ul>
 *   <li>Le liste restituite non sono mai {@code null} (vuote se nessun risultato).</li>
 * </ul>
 */
public interface DomainQueryService {


    // -----------------------------
    // studente -> esami, iscrizioni
    // -----------------------------

    /**
     * Restituisce tutti gli esami sostenuti/registrati dallo studente indicato.
     *
     * @param studenteCf CF dello studente.
     * @return lista (non null) di esami dello studente.
     */
    List<Esame>       esamiByStudente(String studenteCf);

    /**
     * Restituisce tutte le iscrizioni agli appelli effettuate dallo studente indicato.
     *
     * @param studenteCf CF dello studente.
     * @return lista (non null) di iscrizioni dello studente.
     */
    List<Iscrizione>  iscrizioniByStudente(String studenteCf);

    // --------------------------------------
    // docenti -> insegnamenti, appelli, verbali
    // --------------------------------------

    /**
     * Restituisce gli insegnamenti tenuti/associati al docente indicato.
     *
     * @param docenteId CF del docente.
     * @return lista (non null) di insegnamenti.
     */
    List<Insegnamento> insegnamentiByDocente(String docenteId);

    /**
     * Restituisce gli appelli in cui il docente è coinvolto (es. come titolare o commissario).
     *
     * @param docenteId id/CF del docente.
     * @return lista (non null) di appelli.
     */
    List<Appello>      appelliByDocente(String docenteId);

    /**
     * Restituisce i verbali collegati ad appelli del docente indicato.
     *
     * @param docenteId id/CF del docente.
     * @return lista (non null) di verbali.
     */
    List<Verbale>      verbaliByDocente(String docenteId);

    // ---------------------------------------
    // corsi di laurea -> insegnamenti, studenti
    // ---------------------------------------

    /**
     * Restituisce gli insegnamenti afferenti al corso di laurea indicato.
     *
     * @param corsoId id del corso di laurea.
     * @return lista (non null) di insegnamenti.
     */
    List<Insegnamento> insegnamentiByCorso(String corsoId);

    /**
     * Restituisce gli studenti iscritti al corso di laurea indicato.
     *
     * @param corsoId id del corso di laurea.
     * @return lista (non null) di studenti.
     */
    List<Studente>     studentiByCorso(String corsoId);

    // --------------------------
    // insegnamenti -> appelli
    // --------------------------

    /**
     * Restituisce gli appelli relativi all’insegnamento indicato.
     *
     * @param insegnamentoId id dell’insegnamento.
     * @return lista (non null) di appelli.
     */
    List<Appello> appelliByInsegnamento(String insegnamentoId);

    // -----------------------------
    // appelli -> iscrizioni, verbali
    // -----------------------------

    /**
     * Restituisce le iscrizioni all’appello indicato.
     *
     * @param appelloId id dell’appello.
     * @return lista (non null) di iscrizioni.
     */
    List<Iscrizione> iscrizioniByAppello(String appelloId);

    /**
     * Restituisce il verbale relativo all’appello indicato, se esiste.
     *
     * @param appelloId id dell’appello.
     * @return {@link Optional} con il verbale, vuoto se assente.
     */
    Optional<Verbale> verbaleByAppello(String appelloId);

    // -------------------------
    // iscrizioni -> esame
    // -------------------------

    /**
     * Restituisce l’esame collegato a una specifica iscrizione, se presente.
     *
     * @param iscrizioneId id dell’iscrizione.
     * @return {@link Optional} con l’esame, vuoto se assente.
     */
    Optional<Esame> esameByIscrizione(String iscrizioneId);

    // ------------------------------
    // verbali -> visualizza appello
    // ------------------------------

    /**
     * Restituisce l’appello al quale è associato il verbale indicato, se presente.
     *
     * @param verbaleId id del verbale.
     * @return {@link Optional} con l’appello, vuoto se assente.
     */
    Optional<Appello> appelloByVerbale(String verbaleId);

    // ------------------------------
    // Helper di conteggio (di comodo)
    // ------------------------------

    /**
     * Conteggio rapido delle iscrizioni di uno studente (equivalente a {@code iscrizioniByStudente(cf).size()}).
     *
     * @param cf CF dello studente.
     * @return numero di iscrizioni.
     */
    default long countIscrizioniByStudente(String cf) { return iscrizioniByStudente(cf).size(); }

    /**
     * Conteggio rapido degli esami di uno studente (equivalente a {@code esamiByStudente(cf).size()}).
     *
     * @param cf CF dello studente.
     * @return numero di esami.
     */
    default long countEsamiByStudente(String cf)      { return esamiByStudente(cf).size(); }
}
