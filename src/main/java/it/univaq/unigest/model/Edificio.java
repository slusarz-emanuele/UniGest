package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;

public class Edificio implements Identificabile<String> {

    private String id;
    private String nome;

    public Edificio(String id,String nome){
        this.id=id;
        this.nome=nome;
    }
      public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}