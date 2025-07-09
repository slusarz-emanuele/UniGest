package it.univaq.unigest.manager;

import com.google.gson.reflect.TypeToken;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.util.DatabaseHelper;
import it.univaq.unigest.util.LogHelper;
import it.univaq.unigest.util.LogType;

import java.util.List;
import java.util.stream.Collectors;

public class StudenteManager extends AbstractManager<Studente> {

    public StudenteManager() {
        super(
                DatabaseHelper.PERCORSO_CARTELLA_DATI + "/studenti.json",
                new TypeToken<List<Studente>>() {}.getType()
        );
    }

    @Override
    public boolean aggiorna(String cfStudente, Studente studente) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCf().equalsIgnoreCase(cfStudente)) {
                lista.set(i, studente);
                salvaSuFile();
                LogHelper.saveLog(LogType.INFO, "La modifica per lo studente " + cfStudente + " è stata effettuata");
                return true;
            }
        }
        LogHelper.saveLog(LogType.ERROR, "Nessuno studente trovato con il CF: " + cfStudente);
        return false;
    }

    @Override
    public List<Studente> filtra(Studente filtro) {
        return lista.stream()
                .filter(s -> filtro.getCf() == null || s.getCf().equalsIgnoreCase(filtro.getCf()))
                .filter(s -> filtro.getNome() == null || s.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(s -> filtro.getCognome() == null || s.getCognome().toLowerCase().contains(filtro.getCognome().toLowerCase()))
                .filter(s -> filtro.getEmail() == null || s.getEmail().toLowerCase().contains(filtro.getEmail().toLowerCase()))
                .filter(s -> filtro.getDataNascita() == null || s.getDataNascita().equals(filtro.getDataNascita()))
                .filter(s -> filtro.getDataIngressoUniversita() == null || s.getDataIngressoUniversita().equals(filtro.getDataIngressoUniversita()))
                .filter(s -> filtro.getMatricola() == null || s.getMatricola().toLowerCase().contains(filtro.getMatricola().toLowerCase()))
                .filter(s -> filtro.getCorsoDiLaurea() == null || s.getCorsoDiLaurea().equals(filtro.getCorsoDiLaurea()))
                .filter(s -> filtro.getDataImmatricolazione() == null || s.getDataImmatricolazione().equals(filtro.getDataImmatricolazione()))
                .filter(s -> filtro.getEsami() == null || s.getEsami().equals(filtro.getEsami()))
                .filter(s -> filtro.getCfu() == null || s.getCfu().equals(filtro.getCfu()))
                .filter(s -> filtro.getMediaPonderata() == null || filtro.getMediaPonderata().equals(s.getMediaPonderata()))
                .filter(s -> filtro.getMediaAritmetica() == null || filtro.getMediaAritmetica().equals(s.getMediaAritmetica()))
                .collect(Collectors.toList());
    }
}
