package it.univaq.unigest.model;

import it.univaq.unigest.util.LocalDateUtil;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona{

    //attributi
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private String dataNascita;
    private String dataIngressoUniversita;

    //costruttore
    protected Persona( String cf,
                       String nome,
                       String cognome,
                       LocalDate dataNascita,
                       String dataIngressoUniversita){
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = generaEmail();
        this.dataNascita = LocalDateUtil.toString(dataNascita);
        this.dataIngressoUniversita = dataIngressoUniversita;
    }

    protected Persona(){

    }

    //metodi getter
    public String getCf(){
        return this.cf;
    }

    public String getNome(){
        return this.nome;
    }
    public String getCognome(){
        return this.cognome;
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
        LocalDate nascita = LocalDateUtil.fromString(this.dataNascita);
        if(nascita == null) return "-";
        LocalDate oggi = LocalDate.now();
        Period eta = Period.between(nascita,oggi);
        return eta.getYears() + " anni";
    }

    //metodi setter
    public void setCf(String cf){
        this.cf = cf;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setDataNascita(String DataNascita){
        this.dataNascita = DataNascita;
    }

    public void setDataIngressoUniversita(String DataIngressoUniversita){
        this.dataIngressoUniversita = DataIngressoUniversita;
    }

    //metodo astratto
    protected abstract String generaEmail();

    //metodo toString
    @Override
    public String toString(){
        return "cf(id): " + this.cf + ", " +
                "Nome: " + this.nome + ", " +
                "Cognome: " + this.cognome + ", " +
                "Email: " + this.email + ", " +
                "DataNascita: " + this.dataNascita + ", " +
                "DataIngressoUniversita: " + this.dataNascita + ", ";
    }

}
