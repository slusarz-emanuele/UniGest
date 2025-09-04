package it.univaq.unigest.util.loader;

import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.DocenteService;
import it.univaq.unigest.service.InsegnamentoService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

public final class InsegnamentoLoader {

    private InsegnamentoLoader() {}

    public static void sanitizeDocenti(InsegnamentoService insegnamentoService,
                                       DocenteService docenteService) {

        Set<String> cfValidi = docenteService.findAll().stream()
                .map(Docente::getCf)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Pattern p = Pattern.compile(".*\\(([^)]+)\\).*"); // estrae CF da "...(CF)"

        insegnamentoService.findAll().forEach(ins -> {
            List<String> cleaned = (ins.getDocenti() == null ? List.<String>of() : ins.getDocenti()).stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(s -> {
                        var m = p.matcher(s);
                        return m.matches() ? m.group(1).trim() : s; // "Nome (CF)" -> "CF", altrimenti già "CF"
                    })
                    .filter(cfValidi::contains)
                    .distinct()
                    .toList();
            ins.setDocenti(cleaned);
        });
    }
}
