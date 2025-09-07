package it.univaq.unigest.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility per la conversione tra {@link LocalDate} e {@link String}.
 *
 * <p>Formati supportati:
 * <ul>
 *   <li>Output: {@code yyyy-MM-dd} (ISO_LOCAL_DATE)</li>
 *   <li>Input: lo stesso formato; in caso di stringa nulla/vuota ritorna {@code null}.</li>
 * </ul>
 *
 * <p>In caso di parsing non valido, viene loggato un errore e restituito {@code null}.
 */
public class LocalDateUtil {

    private static final Logger LOGGER = LogManager.getLogger(LocalDateUtil.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Converte una data in stringa nel formato {@code yyyy-MM-dd}.
     *
     * @param data la data da formattare (può essere null)
     * @return la stringa formattata, oppure stringa vuota se {@code data} è null
     */
    public static String toString(LocalDate data) {
        if (data == null) return "";
        return data.format(FORMATTER);
    }

    /**
     * Effettua il parse di una stringa nel formato {@code yyyy-MM-dd}.
     *
     * @param data stringa di input (può essere null o vuota)
     * @return la {@link LocalDate} ottenuta, oppure {@code null} se input non valido
     */
    public static LocalDate fromString(String data) {
        if (data == null || data.isBlank()) return null;
        try {
            return LocalDate.parse(data, FORMATTER);
        } catch (DateTimeParseException e) {
            LOGGER.error("Data non valida: " + data);
            return null;
        }
    }

}
