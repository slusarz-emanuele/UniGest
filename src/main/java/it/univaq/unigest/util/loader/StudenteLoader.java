package it.univaq.unigest.util.loader;

import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.StudenteService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StudenteLoader {

    private StudenteLoader() {}

    /**
     * Per ogni studente:
     *  - trova le iscrizioni di quello studente
     *  - mappa alle liste di esami (via iscrizioneId)
     *  - setta la lista esami (e opzionalmente ricalcola CFU)
     */
    public static void loadEsamiForStudenti(StudenteService studenteService,
                                            IscrizioneService iscrizioneService,
                                            EsameService esameService) {

        // indicizziamo gli esami per iscrizioneId
        Map<String, List<Esame>> esamiByIscrizione = esameService.findAll().stream()
                .filter(e -> e.getIscrizioneId() != null)
                .collect(Collectors.groupingBy(Esame::getIscrizioneId));

        List<Iscrizione> tutteIscrizioni = iscrizioneService.findAll();

        studenteService.findAll().forEach(s -> {
            if (s == null || s.getCf() == null) return;

            List<String> iscrizioniDelloStudente = tutteIscrizioni.stream()
                    .filter(i -> i.getRidStudenteCf() != null && i.getRidStudenteCf().equalsIgnoreCase(s.getCf()))
                    .map(Iscrizione::getId) // String
                    .toList();

            List<Esame> esami = iscrizioniDelloStudente.stream()
                    .map(id -> esamiByIscrizione.getOrDefault(id, List.of()))
                    .flatMap(List::stream)
                    .toList();

            s.setEsami(esami);
            // opzionale (il getter ricalcola comunque, ma sincronizziamo il campo)
            s.setCfu(s.calcolaCfu());
        });
    }
}
