package it.univaq.unigest.model;

public class CorsoDiLaurea {

    // Attributi
    private String id;                // Sigla (es. “INF-TRI”) o UUID
    private String nome;              // Nome esteso del corso
    private int cfuTotali;            // Totale CFU necessari alla laurea
    private String dipartimento;      // Nome del dipartimento (opzionale)
    private String coordinatoreId;    // Persona responsabile (opzionale)

    // Costruttore
    public CorsoDiLaurea(){

    }

    // Costruttore parametrizzato
    public CorsoDiLaurea(String id,
                         String nome,
                         int cfuTotali,
                         String dipartimento,
                         String coordinatoreId){
        this.id = id;
        this.nome = nome;
        this.cfuTotali = cfuTotali;
        this.dipartimento = dipartimento;
        this.coordinatoreId = coordinatoreId;
    }


    // Metodi getter
    public String getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public int getCfuTotali(){
        return this.cfuTotali;
    }

    public String getDipartimento(){
        return this.dipartimento;
    }

    public String getCoordinatoreId(){
        return this.coordinatoreId;
    }

    // Metodi setter
    public void setId(String id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCfuTotali(int cfuTotali){
        this.cfuTotali = cfuTotali;
    }

    public void setDipartimento(String dipartimento){
        this.dipartimento = dipartimento;
    }

    public void setCoordinatoreId(String coordinatoreId){
        this.coordinatoreId = coordinatoreId;
    }

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "nome: " + this.nome + " " +
                "cfuTotali: " + this.cfuTotali + " " +
                "dipartimento: " + this.dipartimento + " " +
                "coordinatoreId: " + this.coordinatoreId;
    }
}
