package it.univaq.unigest.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.univaq.unigest.util.LocalDateAdapter;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

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
            LogHelper.saveLog(LogType.DEBUG, "Caricamento da " + path + " riuscito");
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Errore caricamento da " + path + ": " + e.getMessage());
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
            LogHelper.saveLog(LogType.DEBUG, "Scrittura su " + path + " riuscita");
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Errore salvataggio su " + path + ": " + e.getMessage());
        }
    }


    public void aggiungi(T elemento) {
        if (!lista.contains(elemento)) {
            lista.add(elemento);
            salvaSuFile();
            LogHelper.saveLog(LogType.INFO, elemento.toString() + " aggiunto");
        } else {
            LogHelper.saveLog(LogType.WARNING, "Elemento già presente: " + elemento.toString());
        }
    }

    public void rimuovi(T elemento) {
        if (lista.remove(elemento)) {
            salvaSuFile();
            LogHelper.saveLog(LogType.INFO, elemento.toString() + " rimosso");
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
            LogHelper.saveLog(LogType.DEBUG, "Indice corrente salvato su " + getPathIndiceCorrente());
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Errore salvataggio indiceCorrente: " + e.getMessage());
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
            LogHelper.saveLog(LogType.DEBUG, "Indice corrente caricato da " + getPathIndiceCorrente() + ": " + indiceCorrente);
        } catch (IOException e) {
            indiceCorrente = 1;
            LogHelper.saveLog(LogType.DEBUG, "File meta non trovato per " + getPathIndiceCorrente() + ", impostato indiceCorrente = 1");
        }
    }

    public int assegnaIndiceCorrente(){
        int indiceCorrenteOld=indiceCorrente;
        this.indiceCorrente++;
        salvaIndiceCorrente();
        return indiceCorrenteOld;
    }

}
