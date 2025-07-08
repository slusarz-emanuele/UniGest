package it.univaq.unigest.model;

import java.time.LocalDate;

public class Iscrizione {

    // Attributi
    private Integer id;
    private String ridStudenteCf;     // La FK che collega il cf dello stdente all'iscrizione
    private Integer ridAppello;           // La FK che collega l'appello all'iscrizione
    private LocalDate dataIscrizione; // Data iscrizone all'appello
    private boolean ritirato;         // Ritirato prima dell'esame

    // Costruttore
    public Iscrizione(){

    }

    // Costruttore parametrizzato
    public Iscrizione(Integer id,
                      String ridStudenteCf,
                      int ridAppello,
                      LocalDate dataIscrizione,
                      boolean ritirato){
        this.id = id;
        this.ridStudenteCf = ridStudenteCf;
        this.ridAppello = ridAppello;
        this.dataIscrizione = dataIscrizione;
        this.ritirato = ritirato;
    }

    // Metodi getter
    public Integer getId() {
        return this.id;
    }

    public String getRidStudenteCf(){
        return this.ridStudenteCf;
    }

    public int getRidAppello(){
        return this.ridAppello;
    }

    public LocalDate getDataIscrizione(){
        return this.dataIscrizione;
    }

    public boolean getRitirato(){
        return this.ritirato;
    }

    // Metodi setter
    public void setId(Integer id){
        this.id = id;
    }

    public void setRidStudenteCf(String ridStudenteCf){
        this.ridStudenteCf = ridStudenteCf;
    }

    public void setRidAppello(int ridAppello){
        this.ridAppello = ridAppello;
    }

    public void setDataIscrizione(LocalDate dataIscrizione){
        this.dataIscrizione = dataIscrizione;
    }

    public void setRitirato(boolean ritirato){
        this.ritirato = ritirato;
    }

    // ToString
    public String toString (){
        return "id: " + this.id + " " +
                "ridStudenteCf: " + this.ridStudenteCf + " " +
                "ridAppello: " + this.ridAppello + " " +
                "DataIscrizione: " + this.dataIscrizione + " " +
                "Ritirato: " + this.ritirato;
    }
}
