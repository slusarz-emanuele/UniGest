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

public abstract class AbstractManager<T> {

    protected List<T> lista;
    protected final String path;
    protected final Type tipoLista;

    public AbstractManager(String path, Type tipoLista) {
        this.path = path;
        this.tipoLista = tipoLista;
        this.lista = new ArrayList<>();
        caricaDaFile();
    }

    public void caricaDaFile() {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            FileReader reader = new FileReader(path);
            lista = gson.fromJson(reader, tipoLista);
            if (lista == null) lista = new ArrayList<>();
            reader.close();
            LogHelper.saveLog(LogType.DEBUG, "Caricamento da " + path + " riuscito");
        } catch (IOException e) {
            LogHelper.saveLog(LogType.ERROR, "Errore caricamento da " + path + ": " + e.getMessage());
        }
    }

    public void salvaSuFile() {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .setPrettyPrinting()
                    .create();
            FileWriter writer = new FileWriter(path);
            gson.toJson(lista, writer);
            writer.close();
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

    public abstract boolean aggiorna(String id, T nuovo);

    public abstract List<T> filtra(T filtro);

    public String toString(){
        String string = "";
        for (T elemento : lista) {
            string = string + elemento.toString() + "\n";
        }
        return string;
    }
}
