package it.univaq.unigest.model;

import  it.univaq.unigest.manager.EdificioManager;

public class Aula{
    //attributi
    private String id;
    private int capienza;
    private String edificio;

    //costruttore
    public Aula(String id,
    int capienza,
    String edificio){
        this.id = id;
        this.capienza = capienza;
        this.edificio = edificio;
    
    }

    //metodi getter
      public String getId(){
        return this.id;
    }

    public int getCapienza(){
        return this.capienza;
    }

    public String getEdificio(){
        return this.edificio;
    }
    
     public String getEdificioNome() {
        return ""; // TODO: da implementare!
    }
    
    //metodi setter
    public void setId(String id){
        this.id = id;
    }

    public void setCapienza(int capienza){
        this.capienza = capienza;
    }

    public void setEdificio(String edificio){
        this.edificio = edificio;
    }

   
    public String toString(){
        return "id: " + this.id + " " +
                "capienza: " + this.capienza + " " +
                "edificio: " + this.edificio;
    }

    
}
