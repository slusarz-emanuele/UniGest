package it.univaq.unigest.model;

public class Edificio{
    //attributi 
    private String id;
    private String nome;

    //costruttore
    public Edificio(String id,String nome){
        this.id = id;
        this.nome = nome;
    }
     //metodi setter
      public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    //metodi getter
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
