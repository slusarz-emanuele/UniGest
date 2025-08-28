package it.univaq.unigest.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appello{ 
    //attributi
    private int id;
    private String ridInsegnamento;            //la fk che collega l'insegnamento all'appello
    private LocalDate data;                    //data dell'appello
    private LocalTime ora;                     //ora dell'appello
    private String ridAula;                    //la fk che collega l'aula dell'appello
    private String ridDocente;                 //la fk che collega il docente all'appello
    private String ridVerbale;                 //la fk che collega il verbale all'appello

    //costruttore
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

    //metodi getter
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

    // metodi setter
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

    //metodo che stampa le generalità
    public String getGeneralita(){
        return "ridInsegnamento: " + this.ridInsegnamento + " " +
                "data: " + this.data + " " +
                "ora: " + this.ora + " " +
                "ridAula: " + this.ridAula + " " +
                "ridDocente: " + this.ridDocente + " " +
                "ridVerbale: " + this.ridVerbale;
    }

    //metodo toString
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

