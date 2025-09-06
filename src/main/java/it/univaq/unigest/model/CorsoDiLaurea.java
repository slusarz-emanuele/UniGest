package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;

public class CorsoDiLaurea implements Identificabile<String> {
    private String id;
    private String nome;
    private int cfuTotali;
    private String dipartimento;
    private String coordinatoreId;

    public CorsoDiLaurea(String id,
                         String nome,
                         int cfuTotali,
                         String dipartimento,
                         String coordinatoreId){
        this.id=id;
        this.nome=nome;
        this.cfuTotali=cfuTotali;
        this.dipartimento=dipartimento;
        this.coordinatoreId=coordinatoreId;
    }

    @Override
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


    @Override
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


    @Override
    public String toString(){
        return "id: " + this.id + " " +
                "nome: " + this.nome + " " +
                "cfuTotali: " + this.cfuTotali + " " +
                "dipartimento: " + this.dipartimento + " " +
                "coordinatoreId: " + this.coordinatoreId;
    }

}