package it.univaq.unigest.model;

import it.univaq.unigest.util.LocalDateUtil;

import java.time.LocalDate;

public abstract class Persona {

    // Attributi di instanza
    private String cf;                              // Viene utilizzato come ID
    private String nome;
    private String cognome;
    private String email;
    private String dataNascita;
    private String dataIngressoUniversita;

    // Metodo costruttore
    protected Persona(){

    }

    // Metodo costruttore parametrizzato
    protected Persona(String cf,
                      String nome,
                      String cognome,
                      String email,
                      LocalDate dataNascita,
                      String dataIngressoUniversita){
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.dataNascita = LocalDateUtil.toString(dataNascita);
        this.dataIngressoUniversita = dataIngressoUniversita;
    }

    // Metodi getter
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

    // Metodi setter
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

    public void setDataNascita(String dataNascita){
        this.dataNascita = dataNascita;
    }

    public void setDataIngressoUniversita(String dataIngressoUniversita){
        this.dataIngressoUniversita = dataIngressoUniversita;
    }

    // ToString
    public String toString(){
        return "cf(id): " + this.cf + ", " +
                "Nome: " + this.nome + ", " +
                "Cognome: " + this.cognome + ", " +
                "Email: " + this.email + ", " +
                "DataNascita: " + this.dataNascita + ", " +
                "DataIngressoUniversita: " + this.dataNascita + ", ";
    }
}
