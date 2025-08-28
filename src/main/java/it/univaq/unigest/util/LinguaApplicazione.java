package it.univaq.unigest.util;

public enum LinguaApplicazione {
    ITALIANO("Italiano"),
    ENGLISH("English");

    private final String nome;

    LinguaApplicazione(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return nome;
    }
}