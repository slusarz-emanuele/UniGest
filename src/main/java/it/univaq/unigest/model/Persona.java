package it.univaq.unigest.model;

import it.univaq.unigest.util.LocalDateUtil;

import java.time.LocalDate;
import java.time.Period;

/**
 * La classe astratta {@code Persona} rappresenta
 * un'entità generica di persona.
 *
 * <p>Contiene informazioni comuni quali:
 * <ul>
 *   <li>Codice fiscale (cf)</li>
 *   <li>Nome</li>
 *   <li>Cognome</li>
 *   <li>Email</li>
 *   <li>Data di nascita</li>
 *   <li>Data di ingresso nell'università</li>
 * </ul> 
 *
 * <p>Questa classe serve come superclasse per entità specifiche quali {@link Docente} 
 * e {@link Studente} fornendo getter/setter.
 */
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
        //this.email = generaEmail();
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
        return this.generaEmail();
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
    public void setEmail(String email){
        this.email = email;
    }

    public void setCf(String cf){
        this.cf = cf;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
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
