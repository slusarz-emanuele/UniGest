package it.univaq.unigest.gui.componenti;

import it.univaq.unigest.model.*;
import it.univaq.unigest.util.LocalDateUtil;

import java.util.LinkedHashMap;
import java.util.function.Function;

public final class ColonneMiniFactory {

    private ColonneMiniFactory() {}

    public static LinkedHashMap<String, Function<Insegnamento, String>> insegnamentoMini() {
        LinkedHashMap<String, Function<Insegnamento, String>> m = new LinkedHashMap<>();
        m.put("ID", Insegnamento::getId);
        m.put("Nome", Insegnamento::getNome);
        m.put("CFU",  i -> i.getCfu() != null ? String.valueOf(i.getCfu()) : "");
        m.put("Anno", i -> i.getAnno() != null ? String.valueOf(i.getAnno()) : "");
        m.put("Semestre", i -> i.getSemestre() != null ? String.valueOf(i.getSemestre()) : "");
        return m;
    }

    public static LinkedHashMap<String, Function<Studente, String>> studenteMini() {
        LinkedHashMap<String, Function<Studente, String>> m = new LinkedHashMap<>();
        m.put("CF", Studente::getCf);
        m.put("Nome", Studente::getNome);
        m.put("Cognome", Studente::getCognome);
        m.put("Matricola", Studente::getMatricola);
        return m;
    }

    public static LinkedHashMap<String, Function<Appello, String>> appelloMini() {
        LinkedHashMap<String, Function<Appello, String>> m = new LinkedHashMap<>();
        m.put("ID", Appello::getId);
        m.put("Ins.", Appello::getRidInsegnamento);
        m.put("Data", a -> a.getData() != null ? LocalDateUtil.toString(a.getData()) : "");
        m.put("Ora",  a -> a.getOra() != null ? a.getOra().toString() : "");
        m.put("Aula", Appello::getRidAula);
        m.put("Doc.", Appello::getRidDocente);
        return m;
    }

    public static LinkedHashMap<String, Function<Iscrizione, String>> iscrizioneMini() {
        LinkedHashMap<String, Function<Iscrizione, String>> m = new LinkedHashMap<>();
        m.put("ID", Iscrizione::getId);
        m.put("Studente", Iscrizione::getRidStudenteCf);
        m.put("Appello", i -> String.valueOf(i.getRidAppello()));
        m.put("Data",    i -> i.getDataIscrizione() != null ? i.getDataIscrizione().toString() : "");
        m.put("Ritirato", i -> i.getRitirato() ? "Sì" : "No");
        return m;
    }

    public static LinkedHashMap<String, Function<Esame, String>> esameMini() {
        LinkedHashMap<String, Function<Esame, String>> m = new LinkedHashMap<>();
        m.put("ID", Esame::getId);
        m.put("Iscrizione", Esame::getIscrizioneId);
        m.put("Voto", e -> e.getVoto() != null ? String.valueOf(e.getVoto()) : "");
        m.put("Lode", e -> Boolean.TRUE.equals(e.isLode()) ? "Sì" : "No");
        m.put("Rifiutato", e -> Boolean.TRUE.equals(e.isRifiutato()) ? "Sì" : "No");
        m.put("Verbalizzato", e -> Boolean.TRUE.equals(e.isVerbalizzato()) ? "Sì" : "No");
        return m;
    }

    public static LinkedHashMap<String, Function<Verbale, String>> verbaleMini() {
        LinkedHashMap<String, Function<Verbale, String>> m = new LinkedHashMap<>();
        m.put("ID", v -> v.getId() != null ? v.getId() : "");
        m.put("Appello", Verbale::getAppelloId);
        m.put("Chiuso", v -> v.getChiuso() ? "Sì" : "No");
        m.put("Firmato", v -> v.getFirmato() ? "Sì" : "No");
        return m;
    }
}