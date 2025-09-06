package it.univaq.unigest.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.util.LocalDateAdapter;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.univaq.unigest.util.LocalTimeAdapter;
import java.time.LocalTime;


public abstract class AbstractManager<T> implements CrudManager<T> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractManager.class);

    protected List<T> lista;
    protected final String path;
    protected final Type tipoLista;

    int indiceCorrente;

    public AbstractManager(String path, Type tipoLista) {
        this.path = path;
        this.tipoLista = tipoLista;
        this.lista = new ArrayList<>();
    }

    public void caricaDaFile() {
        try (FileReader reader = new FileReader(path);){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            lista = gson.fromJson(reader, tipoLista);
            if (lista == null) lista = new ArrayList<>();
            LOGGER.debug("Caricamento da " + path + " riuscito");
        } catch (IOException e) {
            LOGGER.error("Errore caricamento da " + path + ": " + e.getMessage());
        }
    }

    public void salvaSuFile() {
        try (FileWriter writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(lista, writer);
            LOGGER.debug("Scrittura su " + path + " riuscita");
        } catch (IOException e) {
            LOGGER.error("Errore salvataggio su " + path + ": " + e.getMessage());
        }
    }


    public void aggiungi(T elemento) {
        if (!lista.contains(elemento)) {
            lista.add(elemento);
            salvaSuFile();
            LOGGER.info(elemento.toString() + " aggiunto");
        } else {
            LOGGER.warning("Elemento già presente: " + elemento.toString());
        }
    }

    public void rimuovi(T elemento) {
        if (lista.remove(elemento)) {
            salvaSuFile();
            LOGGER.info(elemento.toString() + " rimosso");
        }
    }

    public List<T> getAll() {
        return lista;
    }

    public abstract boolean aggiorna(T nuovo);

    public abstract List<T> filtra(T filtro);

    public int getIndiceCorrente() {
        return indiceCorrente;
    }

    public void setIndiceCorrente(int indiceCorrente) {
        this.indiceCorrente = indiceCorrente;
    }

    public String toString(){
        String string = "";
        for (T elemento : lista) {
            string = string + elemento.toString() + "\n";
        }
        return string;
    }

    // TEST

    private String getPathIndiceCorrente() {
        return path.replace(".json", "_meta.json");
    }

    public void salvaIndiceCorrente() {
        try (FileWriter writer = new FileWriter(getPathIndiceCorrente())) {
            writer.write("{\"indiceCorrente\": " + indiceCorrente + "}");
            LOGGER.debug("Indice corrente salvato su " + getPathIndiceCorrente());
        } catch (IOException e) {
            LOGGER.error("Errore salvataggio indiceCorrente: " + e.getMessage());
        }
    }

    public void caricaIndiceCorrente() {
        try (FileReader reader = new FileReader(getPathIndiceCorrente())) {
            char[] buffer = new char[64];
            int length = reader.read(buffer);
            String json = new String(buffer, 0, length);
            json = json.replaceAll("[^0-9]", ""); // rimuove tutto tranne i numeri
            if (!json.isEmpty()) {
                indiceCorrente = Integer.parseInt(json);
            } else {
                indiceCorrente = 1;
            }
            LOGGER.debug("Indice corrente caricato da " + getPathIndiceCorrente() + ": " + indiceCorrente);
        } catch (IOException e) {
            indiceCorrente = 1;
            LOGGER.debug("File meta non trovato per " + getPathIndiceCorrente() + ", impostato indiceCorrente = 1");
        }
    }

    public int assegnaIndiceCorrente(){
        int indiceCorrenteOld=indiceCorrente;
        this.indiceCorrente++;
        salvaIndiceCorrente();
        return indiceCorrenteOld;
    }

}
