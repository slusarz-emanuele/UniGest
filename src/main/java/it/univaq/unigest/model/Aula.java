package it.univaq.unigest.model;

public class Aula{
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
      public String getId(){
        return this.id;
    }

    public int getCapienza(){
        return this.capienza;
    }

    public String getEdificio(){return this.edificio;}

    
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