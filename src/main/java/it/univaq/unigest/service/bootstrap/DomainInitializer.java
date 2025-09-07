package it.univaq.unigest.service.bootstrap;

import it.univaq.unigest.service.*;

/**
 * Inizializza e riallinea le relazioni di dominio subito dopo il caricamento dei dati.
 *
 * <p><b>Scopo</b>:
 * <ul>
 *   <li>Popolare i campi derivati su {@code Studente} (esami dello studente) a partire da iscrizioni ed esami</li>
 *   <li>Popolare i campi derivati su {@code Verbale} (esami inclusi) a partire da iscrizioni ed esami</li>
 *   <li>Normalizzare i riferimenti docenti degli {@code Insegnamento} (CF, duplicati, formati misti)</li>
 * </ul>
 *
 * <p><b>Quando usarlo</b>:
 * <ul>
 *   <li>All’avvio dell’app, subito dopo aver caricato i repository</li>
 *   <li>Dopo un ripristino da backup</li>
 *   <li>Ogni volta che si ricaricano i dataset in massa</li>
 * </ul>
 */
public final class DomainInitializer {

    private DomainInitializer() {}

    /**
     * Inizializza tutte le relazioni principali del dominio.
     *
     * <p><b>Ordine operativo</b>:
     * <ol>
     *   <li>Aggiorna gli esami sugli studenti (dipende da iscrizioni + esami)</li>
     *   <li>Aggiorna gli esami nei verbali (dipende da iscrizioni + esami)</li>
     *   <li>Sanifica i riferimenti docenti negli insegnamenti</li>
     * </ol>
     *
     * @param studenteService       accesso agli studenti
     * @param iscrizioneService     accesso alle iscrizioni
     * @param esameService          accesso agli esami
     * @param verbaleService        accesso ai verbali
     * @param insegnamentoService   accesso agli insegnamenti
     * @param docenteService        accesso ai docenti
     */
    public static void initAll(StudenteService studenteService,
                               IscrizioneService iscrizioneService,
                               EsameService esameService,
                               VerbaleService verbaleService,
                               InsegnamentoService insegnamentoService,
                               DocenteService docenteService) {

        // Ordine: prima studenti/verbali (dipendono da iscrizioni+esami), poi insegnamenti
        StudenteLoader.loadEsamiForStudenti(studenteService, iscrizioneService, esameService);
        VerbaleLoader.loadEsamiForVerbali(verbaleService, iscrizioneService, esameService);
        InsegnamentoLoader.sanitizeDocenti(insegnamentoService, docenteService);
    }

    /**
     * Da richiamare dopo un cambiamento sugli esami
     * (create/update/delete) per riallineare studenti e verbali.
     *
     * @param studenteService   accesso agli studenti
     * @param iscrizioneService accesso alle iscrizioni
     * @param esameService      accesso agli esami
     * @param verbaleService    accesso ai verbali
     */
    public static void afterEsameChange(StudenteService studenteService,
                                        IscrizioneService iscrizioneService,
                                        EsameService esameService,
                                        VerbaleService verbaleService) {
        StudenteLoader.loadEsamiForStudenti(studenteService, iscrizioneService, esameService);
        VerbaleLoader.loadEsamiForVerbali(verbaleService, iscrizioneService, esameService);
    }

    /**
     * Da richiamare dopo un cambiamento sulle iscrizioni
     * (create/update/delete) per riallineare studenti e verbali.
     *
     * @param studenteService   accesso agli studenti
     * @param iscrizioneService accesso alle iscrizioni
     * @param esameService      accesso agli esami
     * @param verbaleService    accesso ai verbali
     */
    public static void afterIscrizioneChange(StudenteService studenteService,
                                             IscrizioneService iscrizioneService,
                                             EsameService esameService,
                                             VerbaleService verbaleService) {
        StudenteLoader.loadEsamiForStudenti(studenteService, iscrizioneService, esameService);
        VerbaleLoader.loadEsamiForVerbali(verbaleService, iscrizioneService, esameService);
    }
}
