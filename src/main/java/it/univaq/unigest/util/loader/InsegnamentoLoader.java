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

/**
 * Utility per normalizzare la lista dei docenti associati ad ogni {@link Insegnamento}.
 *
 * <p><b>Scopo</b>:
 * <ul>
 *   <li>Ripulire valori null/vuoti e spazi superflui</li>
 *   <li>Supportare sia il formato "CF" sia "Nome Cognome (CF)" → estraendo il solo CF</li>
 *   <li>Tenere solo CF presenti realmente nell'anagrafica docenti</li>
 *   <li>Rimuovere duplicati preservando l'ordine di apparizione</li>
 * </ul>
 *
 * <p><b>Uso tipico</b>: invocare questo loader all’avvio/appena dopo aver caricato i dati
 * (es. in una fase di boot o di “data refresh”) per riallineare le referenze tra Insegnamenti e Docenti.
 *
 */
public final class InsegnamentoLoader {

    // utility class: nessuna istanza
    private InsegnamentoLoader() {}

    /**
     * Normalizza la lista dei docenti (CF) per ogni insegnamento.
     *
     * <p>Pipeline applicata a ciascun elemento della lista {@code ins.getDocenti()}:
     * <ol>
     *   <li>Filtra {@code null} e stringhe vuote (dopo {@code trim()})</li>
     *   <li>Se il valore è nel formato "Qualcosa (CF)", estrae il CF tra parentesi</li>
     *   <li>Conserva solo i CF effettivamente presenti in {@link DocenteService#findAll()}</li>
     *   <li>Rimuove duplicati mantenendo il primo incontro (via {@code distinct()})</li>
     * </ol>
     *
     * @param insegnamentoService service per accedere/modificare gli insegnamenti
     * @param docenteService      service per validare l’esistenza dei CF docenti
     *
     * @throws NullPointerException se uno dei parametri è {@code null}
     *
     * @implNote La regex {@code .*\\(([^)]+)\\).*} cattura il contenuto tra l’ultima parentesi aperta
     *           e la prima parentesi chiusa successiva. Esempi validi: {@code "Mario Rossi (RSSMRA...)" → "RSSMRA..."}.
     *           Se il testo non contiene parentesi, il valore è lasciato così com’è (es. già CF).
     *
     * @apiNote Metodo idempotente: chiamarlo più volte non cambia il risultato dopo la prima normalizzazione.
     */
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
