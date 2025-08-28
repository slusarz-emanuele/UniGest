package it.univaq.unigest.util;

public class Parser {

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

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

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
