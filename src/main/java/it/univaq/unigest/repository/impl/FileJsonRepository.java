package it.univaq.unigest.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.univaq.unigest.common.Identificabile;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.util.LocalDateAdapter;
import it.univaq.unigest.util.LocalTimeAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Repository generico che gestisce la persistenza di entità {@link Identificabile} su file JSON.
 * <p>
 * La persistenza avviene tramite Gson.
 *
 * @param <T> il tipo dell'entità da gestire, che deve implementare {@link Identificabile<String>}
 */
public class FileJsonRepository<T extends Identificabile<String>> implements Repository<T, String> {

    private static final Logger LOGGER = LogManager.getLogger(FileJsonRepository.class);

    private List<T> list = new ArrayList<>();
    private final String path;
    private final Type typeList;
    private int currentIndex;

    /**
     * Crea un nuovo repository JSON per la gestione delle entità.
     *
     * @param path il path del file JSON su cui salvare i dati
     * @param typeList il tipo della lista di entità per la deserializzazione con Gson
     */
    public FileJsonRepository (String path, Type typeList){
        this.path = path;
        this.typeList = typeList;
        loadByFile();
        loadCurrentIndex();
    }

    /** {@inheritDoc} */
    @Override
    public List<T> findAll (){
        return new ArrayList<>(list);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<T> findById (String id){
        return list.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst();
    }

    /** {@inheritDoc} */
    @Override
    public T save (T e){
        if(e.getId() == null || e.getId().isBlank() || e.getId().equalsIgnoreCase("null") || e.getId().equals("0")){
            e.setId(nextId());
        }
        findById(e.getId()).ifPresentOrElse(
                old -> {list.remove(old);list.add(e); },
            () -> {list.add(e); }
        );
        saveOnFile();
        return e;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        list.removeIf(it -> Objects.equals(it.getId(), id));
        saveOnFile();
    }

    /** {@inheritDoc} */
    @Override
    public String nextId (){
        int old = currentIndex;
        currentIndex++;
        saveCurrentIndex();
        return String.valueOf(old);
    }

    /**
     * Carica tutte le entità dal file JSON.
     * Se il file non esiste, crea un file vuoto.
     */
    private void loadByFile() {
        try {
            Path p = java.nio.file.Paths.get(path);
            // assicura l'esistenza della cartella
            if (p.getParent() != null) {
                java.nio.file.Files.createDirectories(p.getParent());
            }

            if (java.nio.file.Files.notExists(p)) {
                // file assente: inizializza vuoto e crea il file
                list = new ArrayList<>();
                saveOnFile();
                LOGGER.debug("Data file non trovato: creato vuoto -> " + path);
                return;
            }

            try (java.io.Reader reader = java.nio.file.Files.newBufferedReader(p)) {
                com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                        .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                        .registerTypeAdapter(java.time.LocalTime.class, new LocalTimeAdapter())
                        .create();
                List<T> loaded = gson.fromJson(reader, typeList);
                list = (loaded != null) ? loaded : new ArrayList<>();
            }

            LOGGER.debug("Load operation from " + path + " successfully");
        } catch (Exception e) {
            // in caso di errore: non termina l'app, inizializza vuoto e prova a salvare
            LOGGER.error("Load operation from " + path + " failed: " + e.getMessage());
            list = new ArrayList<>();
            try { saveOnFile(); } catch (Exception ignored) {}
        }
    }

    /**
     * Salva tutte le entità nel file JSON.
     */
    private void saveOnFile (){
        try (FileWriter writer = new FileWriter(path)){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(list, writer);
            LOGGER.debug("Write operation on " + path + " successfully");
        }catch (IOException e){
            LOGGER.error("Write operation on " + path + " was not successful");
        }
    }

    /**
     * Restituisce il percorso del file meta che contiene l'indice corrente.
     *
     * @return il percorso del file meta
     */
    private String getPathCurrentIndex (){
        return path.replace(".json", "_meta.json");
    }

    /**
     * Carica l'indice corrente dal file meta. 
     */
    private void loadCurrentIndex (){
        try (FileReader reader = new FileReader(getPathCurrentIndex())){
            char[] buffer = new char[64];
            int length = reader.read(buffer);
            String json = new String(buffer, 0, length);
            json = json.replaceAll("[^0-9]", "");
            if (!json.isEmpty()){
                currentIndex = Integer.parseInt(json);
            } else {
                currentIndex = 1;
            }
            LOGGER.debug("Current Index loaded from " + getPathCurrentIndex() + ": " + currentIndex);
        }catch (IOException e){
            currentIndex = 1;
            LOGGER.debug("Meta file was not foud for " + getPathCurrentIndex() + ", current index set to 1, this may cause some inconsistency");
        }catch (Exception e){
            currentIndex = 1;
            LOGGER.debug("Generic error for " + getPathCurrentIndex() + ", current index set to 1, this may cause some inconsistency");
        }
    }

    /**
     * Salva l'indice corrente nel file meta.
     */
    private void saveCurrentIndex(){
        try (FileWriter writer = new FileWriter(getPathCurrentIndex())){
            writer.write("{\"indiceCorrente\": " + currentIndex + "}");
            LOGGER.debug("Current index saved on " + getPathCurrentIndex());
        }catch (IOException e){
            LOGGER.error("Error during the saving of current index: " + e.getMessage());
        }
    }

    /**
     * Calcola il prossimo indice disponibile dai dati presenti.
     *
     * @return il prossimo indice intero disponibile
     */
    private int nextIndexFromData() {
        return list.stream()
                .map(Identificabile::getId)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> s.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }

}
