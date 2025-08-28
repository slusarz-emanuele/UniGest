package it.univaq.unigest.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Adapter personalizzato per la serializzazione e deserializzazione di {@link LocalDate} con la libreria Gson.
 * <p>
 * Gson non supporta nativamente la conversione di {@code LocalDate}, pertanto questa classe fornisce
 * un {@link TypeAdapter} per gestire automaticamente:
 * <ul>
 *     <li>La conversione di un oggetto {@link LocalDate} in stringa durante la serializzazione</li>
 *     <li>La conversione di una stringa formattata in un oggetto {@link LocalDate} durante la deserializzazione</li>
 * </ul>
 * </p>
 *
 * <p>
 * Il formato utilizzato per la conversione è quello predefinito di {@link LocalDate#toString()},
 * ovvero <b>yyyy-MM-dd</b>.
 * </p>
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    /**
     * Serializza un oggetto {@link LocalDate} in formato stringa utilizzando il formato {@code yyyy-MM-dd}.
     *
     * @param out   il writer JSON fornito da Gson.
     * @param value il valore {@link LocalDate} da scrivere; se {@code null} viene scritto un valore JSON {@code null}.
     * @throws IOException se si verifica un errore durante la scrittura del valore.
     */
    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    /**
     * Deserializza una stringa in formato {@code yyyy-MM-dd} in un oggetto {@link LocalDate}.
     *
     * @param in il reader JSON fornito da Gson.
     * @return l'oggetto {@link LocalDate} corrispondente alla stringa letta.
     * @throws IOException se si verifica un errore durante la lettura o se la stringa non rappresenta una data valida.
     */
    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String date = in.nextString();
        return LocalDate.parse(date);
    }
}
