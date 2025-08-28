package it.univaq.unigest.model;

public class CorsoDiLaurea{

    //attributi
    private String id;                     //id
    private String nome;                   //nome del corso
    private int cfuTotali;                 // totale Cfu necessari alla laurea
    private String dipartimento;           // Rid del dipartimento
    private String coordinatoreId;         //persona responsabile

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

    //metodi getter
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

    //metodi setter
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

    // metodo toString
    public String toString(){
        return "id: " + this.id + " " +
                "nome: " + this.nome + " " +
                "cfuTotali: " + this.cfuTotali + " " +
                "dipartimento: " + this.dipartimento + " " +
                "coordinatoreId: " + this.coordinatoreId;
    }

}
