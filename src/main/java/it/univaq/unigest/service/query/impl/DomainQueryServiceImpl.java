package it.univaq.unigest.service.query.impl;

import it.univaq.unigest.model.*;
import it.univaq.unigest.service.*;
import it.univaq.unigest.service.query.DomainQueryService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementazione di {@link DomainQueryService} che compone i dati
 * provenienti dai servizi CRUD di dominio (AppelloService, StudenteService, ecc.)
 * per offrire query “trasversali” usate dalla GUI.
 *
 * <p><b>Caratteristiche:</b>
 * <ul>
 *   <li>Restituisce liste non {@code null} (vuote) e usa {@link Optional} per i singoli risultati.</li>
 * </ul>
 */
public class DomainQueryServiceImpl implements DomainQueryService {

    private final AppelloService appelloService;
    private final AulaService aulaService;
    private final CorsoDiLaureaService corsoService;
    private final DocenteService docenteService;
    private final EdificioService edificioService;
    private final EsameService esameService;
    private final InsegnamentoService insegnamentoService;
    private final IscrizioneService iscrizioneService;
    private final StudenteService studenteService;
    private final VerbaleService verbaleService;

    /**
     * Crea il servizio di query con tutte le dipendenze CRUD necessarie.
     */
    public DomainQueryServiceImpl(AppelloService appelloService,
                                  AulaService aulaService,
                                  CorsoDiLaureaService corsoService,
                                  DocenteService docenteService,
                                  EdificioService edificioService,
                                  EsameService esameService,
                                  InsegnamentoService insegnamentoService,
                                  IscrizioneService iscrizioneService,
                                  StudenteService studenteService,
                                  VerbaleService verbaleService) {
        this.appelloService = appelloService;
        this.aulaService = aulaService;
        this.corsoService = corsoService;
        this.docenteService = docenteService;
        this.edificioService = edificioService;
        this.esameService = esameService;
        this.insegnamentoService = insegnamentoService;
        this.iscrizioneService = iscrizioneService;
        this.studenteService = studenteService;
        this.verbaleService = verbaleService;
    }

    // ===== Studente =====

    /** {@inheritDoc} */
    @Override
    public List<Esame> esamiByStudente(String studenteCf) {
        Set<String> iscrIds = iscrizioneService.findAll().stream()
                .filter(i -> i.getRidStudenteCf() != null && i.getRidStudenteCf().equalsIgnoreCase(studenteCf))
                .map(Iscrizione::getId)
                .collect(Collectors.toSet());
        return esameService.findAll().stream()
                .filter(e -> e.getIscrizioneId() != null && iscrIds.contains(e.getIscrizioneId()))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Iscrizione> iscrizioniByStudente(String studenteCf) {
        return iscrizioneService.findAll().stream()
                .filter(i -> i.getRidStudenteCf() != null && i.getRidStudenteCf().equalsIgnoreCase(studenteCf))
                .toList();
    }

    public String corsoByStudente(String studenteCf) {

        String idCDLStudente = studenteService.findAll().stream()
                .filter(s -> s.getCf() != null && s.getCf().equalsIgnoreCase(studenteCf))
                .map(Studente::getCorsoDiLaurea).
                filter(Objects::nonNull).
                findFirst()
                .orElse(null);

        return corsoService.findAll().stream()
                .filter(c -> c.getId() != null && c.getId().equalsIgnoreCase(idCDLStudente))
                .map(CorsoDiLaurea::getNome)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    // ===== Docente =====

    /** {@inheritDoc} */
    @Override
    public List<Insegnamento> insegnamentiByDocente(String docenteId) {
        return insegnamentoService.findAll().stream()
                .filter(ins -> ins.getDocenti() != null && ins.getDocenti().contains(docenteId))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Appello> appelliByDocente(String docenteId) {
        return appelloService.findAll().stream()
                .filter(a -> a.getRidDocente() != null && a.getRidDocente().equals(docenteId))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Verbale> verbaliByDocente(String docenteId) {
        Set<String> appelliDocenteIds = appelliByDocente(docenteId).stream()
                .map(Appello::getId)
                .collect(Collectors.toSet());
        return verbaleService.findAll().stream()
                .filter(v -> v.getAppelloId() != null && appelliDocenteIds.contains(v.getAppelloId()))
                .toList();
    }

    // ===== Corso di Laurea =====

    /** {@inheritDoc} */
    @Override
    public List<Insegnamento> insegnamentiByCorso(String corsoId) {
        return insegnamentoService.findAll().stream()
                .filter(ins -> Objects.equals(ins.getCorsoDiLaureaId(), corsoId))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public List<Studente> studentiByCorso(String corsoId) {
        return studenteService.findAll().stream()
                .filter(s -> s.getCorsoDiLaurea() != null && s.getCorsoDiLaurea().equals(corsoId))
                .toList();
    }

    // ===== Insegnamento =====

    /** {@inheritDoc} */
    @Override
    public List<Appello> appelliByInsegnamento(String insegnamentoId) {
        return appelloService.findAll().stream()
                .filter(a -> a.getRidInsegnamento() != null && a.getRidInsegnamento().equals(insegnamentoId))
                .toList();
    }

    // ===== Appello =====

    /** {@inheritDoc} */
    @Override
    public List<Iscrizione> iscrizioniByAppello(String appelloId) {
        // Iscrizione.ridAppello è int; Appello.getId() è String
        return iscrizioneService.findAll().stream()
                .filter(i -> String.valueOf(i.getRidAppello()).equals(appelloId))
                .toList();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Verbale> verbaleByAppello(String appelloId) {
        return verbaleService.findAll().stream()
                .filter(v -> Objects.equals(v.getAppelloId(), appelloId))
                .findFirst();
    }

    // ===== Iscrizione =====

    /** {@inheritDoc} */
    @Override
    public Optional<Esame> esameByIscrizione(String iscrizioneId) {
        return esameService.findAll().stream()
                .filter(e -> Objects.equals(e.getIscrizioneId(), iscrizioneId))
                .findFirst();
    }

    // ===== Verbale =====

    /** {@inheritDoc} */
    @Override
    public Optional<Appello> appelloByVerbale(String verbaleId) {
        return appelloService.findAll().stream()
                .filter(a -> Objects.equals(a.getRidVerbale(), verbaleId))
                .findFirst();
    }
}
