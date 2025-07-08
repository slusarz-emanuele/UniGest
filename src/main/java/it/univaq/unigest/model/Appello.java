package it.univaq.unigest.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appello {

    // Attributi
    private int id;
    private String ridInsegnamento;    // La FK che collega l'insegnamento all'appello
    private LocalDate data;            // Data dell'appello
    private LocalTime ora;             // Ora dell'appello
    private String ridAula;            // La FK che collega l'aula all'appello
    private String ridDocente;         // La FK (facoltativa) che collega il docente all'appello
    private String ridVerbale;         // La FK (facoltativa) che collega il verbale all'appello

    // Costruttore
    public Appello(){

    }

    // Costruttore parametrizzato
    public Appello(int id,
                   String ridInsegnamento,
                   LocalDate data,
                   LocalTime ora,
                   String ridAula,
                   String ridDocente,
                   String ridVerbale){
        this.id = id;
        this.ridInsegnamento = ridInsegnamento;
        this.data = data;
        this.ora = ora;
        this.ridAula = ridAula;
        this.ridDocente = ridDocente;
        this.ridVerbale = ridVerbale;
    }

    // Metodi getter
    public int getId(){
        return this.id;
    }

    public String getRidInsegnamento(){
        return this.ridInsegnamento;
    }

    public LocalDate getData(){
        return this.data;
    }

    public LocalTime getOra(){
        return this.ora;
    }

    public String getRidAula(){
        return this.ridAula;
    }

    public String getRidDocente(){
        return this.ridDocente;
    }

    public String getRidVerbale(){
        return this.ridVerbale;
    }

    // Metodi setter
    public void setId(int id){
        this.id = id;
    }

    public void setRidInsegnamento(String ridInsegnamento){
        this.ridInsegnamento = ridInsegnamento;
    }

    public void setData(LocalDate data){
        this.data = data;
    }

    public void setOra(LocalTime ora){
        this.ora = ora;
    }

    public void setRidAula(String ridAula){
        this.ridAula = ridAula;
    }

    public void setRidDocente(String ridDocente){
        this.ridDocente = ridDocente;
    }

    public void setRidVerbale(String ridVerbale){
        this.ridVerbale = ridVerbale;
    }

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "ridInsegnamento: " + this.ridInsegnamento + " " +
                "data: " + this.data + " " +
                "ora: " + this.ora + " " +
                "ridAula: " + this.ridAula + " " +
                "ridDocente: " + this.ridDocente + " " +
                "ridVerbale: " + this.ridVerbale;
    }
}
