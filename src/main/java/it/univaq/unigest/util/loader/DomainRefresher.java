package it.univaq.unigest.util.loader;

import it.univaq.unigest.service.*;

public final class DomainRefresher {

    private static StudenteService studenteService;
    private static IscrizioneService iscrizioneService;
    private static EsameService esameService;
    private static VerbaleService verbaleService;
    private static InsegnamentoService insegnamentoService;
    private static DocenteService docenteService;

    private DomainRefresher() {}

    /** Chiamare una sola volta all’avvio (es. in Main.init) */
    public static void init(StudenteService studenteService,
                            IscrizioneService iscrizioneService,
                            EsameService esameService,
                            VerbaleService verbaleService,
                            InsegnamentoService insegnamentoService,
                            DocenteService docenteService) {
        DomainRefresher.studenteService = studenteService;
        DomainRefresher.iscrizioneService = iscrizioneService;
        DomainRefresher.esameService = esameService;
        DomainRefresher.verbaleService = verbaleService;
        DomainRefresher.insegnamentoService = insegnamentoService;
        DomainRefresher.docenteService = docenteService;
    }

    /** Da usare dopo create/update/delete di Esame */
    public static void onEsameChanged() {
        DomainInitializer.afterEsameChange(studenteService, iscrizioneService, esameService, verbaleService);
    }

    /** Da usare dopo create/update/delete di Iscrizione */
    public static void onIscrizioneChanged() {
        DomainInitializer.afterIscrizioneChange(studenteService, iscrizioneService, esameService, verbaleService);
    }

    /** Da usare dopo create/update/delete di Docente (impatti su Insegnamento.docenti) */
    public static void onDocenteChanged() {
        InsegnamentoLoader.sanitizeDocenti(insegnamentoService, docenteService);
    }
}
