package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;

import java.time.LocalDate;

public class Iscrizione implements Identificabile<String> {

    private Integer id;
    private String  ridStudenteCf;
    private Integer ridAppello;
    private LocalDate dataIscrizione;
    private boolean ritirato;

    public Iscrizione(Integer id,
    String ridStudenteCf,
    Integer ridAppello,
    LocalDate dataIscrizione,
    boolean ritirato
    ){
        this.id=id;
        this.ridStudenteCf=ridStudenteCf;
        this.ridAppello=ridAppello;
        this.dataIscrizione=dataIscrizione;
        this.ritirato=ritirato;

    } 
      public Integer getId() {return this.id;}

    public String getRidStudenteCf(){
        return this.ridStudenteCf;
    }

    public int getRidAppello(){
        return this.ridAppello;
    }

    public LocalDate getDataIscrizione(){return this.dataIscrizione;}

    public boolean getRitirato(){
        return this.ritirato;
    }

   
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
    public String getGeneralita(){
         return "Studente: " + this.ridStudenteCf + " " +
                "Appello: " + this.ridAppello + " " +
                "Data Iscizione: " + this.dataIscrizione + " " +
                "Ritirato: " + this.ritirato;
    }
     public String toString (){
        return "id: " + this.id + " " +
                "ridStudenteCf: " + this.ridStudenteCf + " " +
                "ridAppello: " + this.ridAppello + " " +
                "DataIscrizione: " + this.dataIscrizione + " " +
                "Ritirato: " + this.ritirato;
    }
}