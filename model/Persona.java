package it.univaq.unigest.model;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona{
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private String dataNascita;
    private String dataIngressoUniversita;

    protected Persona(private String cf,
    private String nome,
    private String cognome,
    private LocalDate dataNascita,
    private String dataIngressoUniversita){
                   this.cf=cf;
                   this.nome=nome;
                   this.cognome=cognome;
                   this.email= generaEmail();
                   this.dataNascita= LocalDateUtil.toString(dataNascita);
                   this.dataIngressoUniversita=dataIngressoUniversita;
     }
     public String getCf(){
        return this.cf;
     }
     public String getNome(){
        return this.nome;
     }
     public String getEmail(){
        return this.email;
     }
     public String getDataNascita(){
        return this.dataNascita;
     }
     public String getDataIngressoUniversita(){
        return this.dataIngressoUniversita;
     }
     public String getEta(){
        //da fare
     }

    public void setCf(String cf){
        this.cf=cf;
    }
        public void setNome(String nome){
        this.nome=nome;
    }
        public void setCognome(String cognome){
        this.cognome=cognome;
    }
        public void setEmail(String email){
        this.email=email;
    }
        public void setDataNascita(String DataNascita){
        this.dataNascita=DataNascita;
    }
        public void setDataIngressoUniversita(String DataIngressoUniversita){
        this.dataIngressoUniversita=DataIngressoUniversita;
    }
    protected abstract String generaEmail();
    public String toString(){
        return "cf(id): " + this.cf + ", " +
                "Nome: " + this.nome + ", " +
                "Cognome: " + this.cognome + ", " +
                "Email: " + this.email + ", " +
                "DataNascita: " + this.dataNascita + ", " +
                "DataIngressoUniversita: " + this.dataNascita + ", ";
    }
    
}
