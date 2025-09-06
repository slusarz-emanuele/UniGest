package it.univaq.unigest.util;

/**
 * Utility di parsing testuale usate nell'applicazione.
 *
 * <p>Offre due funzioni principali:
 * <ul>
 *   <li>{@link #getToStringParsed(String)}: rende più leggibile una stringa prodotta da {@code toString()},</li>
 *   <li>{@link #estraiIdDaStringaDocente(String)}: estrae l'ID racchiuso tra parentesi da una stringa tipo
 *       {@code "Nome Cognome (ID)"}.</li>
 * </ul>
 *
 * <h3>Esempi rapidi</h3>
 * <pre>
 * // 1) Formattazione "toString"
 * String raw = "cf:ABCDEF12G34H567I nome:Mario cognome:Rossi matricola:12345";
 * System.out.println(Parser.getToStringParsed(raw));
 * // Output (semplificato):
 * // Dettagli oggetto
 * // ----------------------
 * // Cf : ABCDEF12G34H567I
 * // Nome : Mario
 * // Cognome : Rossi
 * // Matricola : 12345
 *
 * // 2) Estrazione ID da docente
 * String s = "Antonio Ioppolo (PPLNTN80A01G273D)";
 * String id = Parser.estraiIdDaStringaDocente(s); // -> "PPLNTN80A01G273D"
 * </pre>
 *
 * <p><b>Nota:</b> il parser di {@link #getToStringParsed(String)} è volutamente semplice:
 * considera come “coppia chiave:valore” solo i token che contengono i due punti
 * (es. {@code nome:Mario}). Se il valore contiene spazi (es. {@code nome:Mario Rossi}),
 * {@code Rossi} verrà stampato su una riga a parte. Per risultati ottimali,
 * struttura il {@code toString()} come sequenza di token {@code chiave:valore} senza spazi
 * nel valore, oppure adatta il parser secondo le tue esigenze.
 */
public class Parser {

    /**
     * Rende più leggibile una stringa prodotta da {@code toString()} generando
     * un blocco testuale con intestazione e coppie chiave/valore.
     *
     * <p>Regole:
     * <ul>
     *   <li>Se la stringa è nulla o vuota, ritorna "Nessun dato disponibile".</li>
     *   <li>Divide per spazi: ogni token che contiene {@code ":"} è trattato come {@code chiave:valore}
     *       e stampato su una riga con chiave capitalizzata; gli altri token sono stampati su righe separate.</li>
     * </ul>
     *
     * @param toStringValue stringa originaria (tipicamente il risultato di {@code obj.toString()})
     * @return testo formattato pronto per la visualizzazione/stampa
     */
    public static String getToStringParsed(String toStringValue) {
        if (toStringValue == null || toStringValue.isBlank()) {
            return "Nessun dato disponibile";
        }

        // Spezza sugli spazi, ma mantiene i valori separati da "key: value"
        String[] parts = toStringValue.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("Dettagli oggetto\n");
        sb.append("----------------------\n");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.contains(":")) {
                String[] keyValue = part.split(":", 2);
                String key = keyValue[0].trim();
                String value = keyValue.length > 1 ? keyValue[1].trim() : "";
                sb.append(capitalize(key)).append(" : ").append(value);
            } else {
                sb.append(part).append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Capitalizza la prima lettera della stringa (se presente).
     *
     * @param str testo di input
     * @return stringa con l’iniziale maiuscola, o la stringa originale se nulla/vuota
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Estrae l’ID tra parentesi tonde dall’ultima coppia di parentesi presente nella stringa.
     *
     * <p>Esempio:
     * <pre>
     * "Antonio Ioppolo (PPLNTN80A01G273D)" -> "PPLNTN80A01G273D"
     * "SoloID"                             -> "SoloID" (nessuna parentesi: ritorna l'input)
     * </pre>
     *
     * @param docenteString stringa del tipo "Nome Cognome (ID)" oppure solo "ID"
     * @return contenuto tra le ultime parentesi {@code (...)} se presenti; altrimenti la stringa originale
     */
    public static String estraiIdDaStringaDocente(String docenteString) {
        if (docenteString == null) return "";
        int start = docenteString.lastIndexOf("(");
        int end = docenteString.lastIndexOf(")");
        if (start != -1 && end != -1 && end > start) {
            return docenteString.substring(start + 1, end).trim();
        }
        return docenteString;
    }


}
