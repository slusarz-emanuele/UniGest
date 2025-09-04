package it.univaq.unigest.util.loader;

import it.univaq.unigest.service.*;

public final class DomainInitializer {

    private DomainInitializer() {}

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

    /* Helper facoltativi da richiamare dopo certe operazioni CRUD */

    public static void afterEsameChange(StudenteService studenteService,
                                        IscrizioneService iscrizioneService,
                                        EsameService esameService,
                                        VerbaleService verbaleService) {
        StudenteLoader.loadEsamiForStudenti(studenteService, iscrizioneService, esameService);
        VerbaleLoader.loadEsamiForVerbali(verbaleService, iscrizioneService, esameService);
    }

    public static void afterIscrizioneChange(StudenteService studenteService,
                                             IscrizioneService iscrizioneService,
                                             EsameService esameService,
                                             VerbaleService verbaleService) {
        StudenteLoader.loadEsamiForStudenti(studenteService, iscrizioneService, esameService);
        VerbaleLoader.loadEsamiForVerbali(verbaleService, iscrizioneService, esameService);
    }
}
