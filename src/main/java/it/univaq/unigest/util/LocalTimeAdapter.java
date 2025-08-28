package it.univaq.unigest.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;

/**
 * Adattatore personalizzato per la gestione della serializzazione e deserializzazione
 * di {@link LocalTime} con la libreria Gson
 */
public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    /**
     * Scrive il valore su JSON della data.
     * @param out Il writer JSON di tipo {@link JsonWriter}.
     * @param value Il valore data di tipo {@link LocalTime}.
     * @throws IOException Se il formato della data è errato o il writer è errato.
     */
    @Override
    public void write(JsonWriter out, LocalTime value) throws IOException {
        out.value(value.toString());
    }

    /**
     * Legge e restituisce il valore da JSON della data.
     * @param in Il reader JSON di tipo {@link JsonReader}.
     * @return Oggetto di tipo {@link LocalTime} letto da JSON.
     * @throws IOException Se il formato della data è errato o il reader è errato.
     */
    @Override
    public LocalTime read(JsonReader in) throws IOException {
        return LocalTime.parse(in.nextString());
    }
}