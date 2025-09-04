package it.univaq.unigest.util.loader;

import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.service.EsameService;
import it.univaq.unigest.service.IscrizioneService;
import it.univaq.unigest.service.VerbaleService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class VerbaleLoader {

    private VerbaleLoader() {}

    /**
     * Per ogni verbale (che ha appelloId):
     *  - trova le iscrizioni che puntano a quell'appello
     *  - raccoglie gli esami con iscrizioneId in quell'insieme
     *  - setta la lista esami del verbale
     */
    public static void loadEsamiForVerbali(VerbaleService verbaleService,
                                           IscrizioneService iscrizioneService,
                                           EsameService esameService) {

        Map<String, List<Esame>> esamiByIscrizione = esameService.findAll().stream()
                .filter(e -> e.getIscrizioneId() != null)
                .collect(Collectors.groupingBy(Esame::getIscrizioneId));

        List<Iscrizione> tutteIscrizioni = iscrizioneService.findAll();

        verbaleService.findAll().forEach(v -> {
            String appelloId = v.getAppelloId();
            if (appelloId == null || appelloId.isBlank()) {
                v.setEsami(List.of());
                return;
            }

            // Iscrizioni che fanno riferimento a quell'appello
            List<String> iscrizioniDiAppello = tutteIscrizioni.stream()
                    .filter(i -> String.valueOf(i.getRidAppello()).equals(appelloId))
                    .map(Iscrizione::getId)
                    .toList();

            // Esami associati a quelle iscrizioni
            List<Esame> esami = iscrizioniDiAppello.stream()
                    .map(id -> esamiByIscrizione.getOrDefault(id, List.of()))
                    .flatMap(List::stream)
                    .toList();

            v.setEsami(esami);
        });
    }
}
