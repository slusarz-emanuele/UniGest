package it.univaq.unigest.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.univaq.unigest.common.Identificabile;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.util.LocalDateAdapter;
import it.univaq.unigest.util.LocalTimeAdapter;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileJsonRepository<T extends Identificabile<String>> implements Repository<T, String> {

    private List<T> list = new ArrayList<>();
    private final String path;
    private final Type typeList;
    private int currentIndex;

    public FileJsonRepository (String path, Type typeList){
        this.path = path;
        this.typeList = typeList;
        loadCurrentIndex();
        loadByFile();
    }

    @Override
    public List<T> findAll (){
        return new ArrayList<>(list);
    }

    @Override
    public Optional<T> findById (String id){
        return list.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst();
    }

    @Override
    public T save (T e){
        if(e.getId() == null || e.getId().isBlank()){
            e.setId(nextId());
        }
        findById(e.getId()).ifPresentOrElse(
                old -> {list.remove(old);list.add(e); },
            () -> {list.add(e); }
        );
        saveOnFile();
        return e;
    }

    @Override
    public void deleteById (String id){
        list.removeIf(it -> Objects.equals(it.getId(), id));
        saveOnFile();
    }

    @Override
    public String nextId (){
        int old = currentIndex;
        currentIndex++;
        saveCurrentIndex();
        return String.valueOf(old);
    }

    private void loadByFile (){
        try (FileReader reader = new FileReader(path)){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            list = gson.fromJson(reader, typeList);
            if(list == null) list = new ArrayList<>();
            LogHelper.saveLog(LogType.DEBUG, "Load operation from " + path + " successfully");
        }catch (IOException e){
            LogHelper.saveLog(LogType.ERROR, "Load operation from " + path + " was not successful");
            System.exit(1);
        }
    }

    private void saveOnFile (){
        try (FileWriter writer = new FileWriter(path)){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                    .create();
            gson.toJson(list, writer);
            LogHelper.saveLog(LogType.DEBUG, "Write operation on " + path + " successfully");
        }catch (IOException e){
            LogHelper.saveLog(LogType.ERROR, "Write operation on " + path + " was not successful");
        }
    }

    private String getPathCurrentIndex (){
        return path.replace(".json", "_meta.json");
    }

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
            LogHelper.saveLog(LogType.DEBUG, "Current Index loaded from " + getPathCurrentIndex() + ": " + currentIndex);
        }catch (IOException e){
            currentIndex = 1;
            LogHelper.saveLog(LogType.DEBUG, "Meta file was not foud for " + getPathCurrentIndex() + ", current index set to 1, this may cause some inconsistency");
        }catch (Exception e){
            currentIndex = 1;
            LogHelper.saveLog(LogType.DEBUG, "Generic error for " + getPathCurrentIndex() + ", current index set to 1, this may cause some inconsistency");
        }
    }

    private void saveCurrentIndex(){
        try (FileWriter writer = new FileWriter(getPathCurrentIndex())){
            writer.write("{\"indiceCorrente\": " + currentIndex + "}");
            LogHelper.saveLog(LogType.DEBUG, "Current index saved on " + getPathCurrentIndex());
        }catch (IOException e){
            LogHelper.saveLog(LogType.ERROR, "Error during the saving of current index: " + e.getMessage());
        }
    }

}
