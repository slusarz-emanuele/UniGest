package it.univaq.unigest.model;

public class Aula {

    // Attributi
    private String id;         // Nome o codice aula (es. “Aula 3”)
    private int capienza;      // Numero massimo di posti
    private String edificio;   // Nome/etichetta dell’edificio

    // Costruttore
    public Aula(){

    }

    // Costruttore parametrizzato
    public Aula(String id,
                int capienza,
                String edificio){
        this.id = id;
        this.capienza = capienza;
        this.edificio = edificio;
    }

    // Metodi getter
    public String getId(){
        return this.id;
    }

    public int getCapienza(){
        return this.capienza;
    }

    public String getEdificio(){
        return this.edificio;
    }

    // Metodi setter
    public void setId(String id){
        this.id = id;
    }

    public void setCapienza(int capienza){
        this.capienza = capienza;
    }

    public void setEdificio(String edificio){
        this.edificio = edificio;
    }

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "capienza: " + this.capienza + " " +
                "edificio: " + this.edificio;
    }
}
