package it.univaq.unigest.model;

import java.time.LocalDate;

public class Verbale {

    // Attributi
    private Integer id;                 // UUID
    private String appelloId;         // FK verso Appello
    private LocalDate dataChiusura;   // Data di chiusura
    private boolean chiuso;           // true = non modificabile
    private boolean firmato;          // true = firmato digitalmente
    private String note;              // Osservazioni opzionali

    // Costruttore
    public Verbale(){

    }

    // Costruttore parametrizzato
    public Verbale(Integer id,
                   String appelloId,
                   LocalDate dataChiusura,
                   boolean chiuso,
                   boolean firmato,
                   String note){
        this.id = id;
        this.appelloId = appelloId;
        this.dataChiusura = dataChiusura;
        this.chiuso = chiuso;
        this.firmato = firmato;
        this.note = note;
    }

    // Metodi getter
    public Integer getId(){
        return this.id;
    }

    public String getAppelloId(){
        return this.appelloId;
    }

    public LocalDate getDataChiusura(){
        return this.dataChiusura;
    }

    public boolean getChiuso(){
        return this.chiuso;
    }

    public boolean getFirmato(){
        return this.firmato;
    }

    public String getNote(){
        return this.note;
    }

    // Metodi setter
    public void setId(Integer id){
        this.id = id;
    }

    public void setAppelloId(String appelloId){
        this.appelloId = appelloId;
    }

    public void setDataChiusura(LocalDate dataChiusura){
        this.dataChiusura = dataChiusura;
    }

    public void setChiuso(boolean chiuso){
        this.chiuso = chiuso;
    }

    public void setFirmato(boolean firmato){
        this.firmato = firmato;
    }

    public void setNote(String note){
        this.note = note;
    }

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "appelloId: " + this.appelloId + " " +
                "dataChiusura: " + this.dataChiusura + " " +
                "chiuso: " + this.chiuso + " " +
                "firmato: " + this.firmato + " " +
                "note: " + this.note;
    }
}
