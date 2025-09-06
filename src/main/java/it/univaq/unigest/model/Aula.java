package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;

public class Aula implements Identificabile<String> {
    private String id;
    private int capienza;
    private String edificio;

    public Aula(String id,
                int capienza,
                String edificio){
        this.id=id;
        this.capienza=capienza;
        this.edificio=edificio;

    }

    @Override
    public String getId(){
        return this.id;
    }

    public int getCapienza(){
        return this.capienza;
    }

    public String getEdificio(){return this.edificio;}


    @Override
    public void setId(String id){
        this.id = id;
    }

    public void setCapienza(int capienza){
        this.capienza = capienza;
    }

    public void setEdificio(String edificio){
        this.edificio = edificio;
    }

    @Override
    public String toString(){
        return "id: " + this.id + " " +
                "capienza: " + this.capienza + " " +
                "edificio: " + this.edificio;
    }

    public String getEdificioNome() {
        return "";
    }
}