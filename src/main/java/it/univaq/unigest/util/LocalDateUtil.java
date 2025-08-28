package it.univaq.unigest.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String toString(LocalDate data) {
        if (data == null) return "";
        return data.format(FORMATTER);
    }

    public static LocalDate fromString(String data) {
        if (data == null || data.isBlank()) return null;
        try {
            return LocalDate.parse(data, FORMATTER);
        } catch (DateTimeParseException e) {
            LogHelper.saveLog(LogType.ERROR, "Data non valida: " + data);
            return null;
        }
    }

}
