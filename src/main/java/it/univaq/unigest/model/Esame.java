package it.univaq.unigest.model;

public class Esame{
    private String id;
    private String iscrizioneId;
    private Double voto;
    private boolean lode;
    private boolean rifiutato;
    private boolean verbalizzato;

    public Esame(String id,
    String iscrizione,
    Double voto,
    boolean lode,
    boolean rifiutato,
    boolean verbalizzato){
        this.id=id;
        this.iscrizioneId=iscrizione;
        this.voto=voto;
        this.lode=lode;
        this.rifiutato=rifiutato;
        this.verbalizzato=verbalizzato;
    }
      public String getId(){
        return this.id;
    }

    public String getIscrizioneId(){
        return this.iscrizioneId;
    }

    public Double getVoto(){
        return this.voto;
    }

    public boolean isLode(){
        return this.lode;
    }

    public boolean isRifiutato(){
        return this.rifiutato;
    }

    public boolean isVerbalizzato(){
        return this.verbalizzato;
    }
    public Integer getCfu(){
        return 1;
    }
     public void setId(String id){
        this.id = id;
    }

    public void setIscrizioneId(String iscrizioneId){
        this.iscrizioneId = iscrizioneId;
    }

    public void setVoto(Double voto){
        this.voto = voto;
    }

    public void setLode(boolean lode){
        this.lode = lode;
    }

    public void setRifiutato(boolean rifiutato){
        this.rifiutato = rifiutato;
    }

    public void setVerbalizzato(boolean verbalizzato){
        this.verbalizzato = verbalizzato;
    }

   
    public String toString(){
        return "id: " + this.id + " " +
                "iscrizioneId: " + this.iscrizioneId + " " +
                "voto: " + this.voto + " " +
                "lode: " + this.lode + " " +
                "rifiutato: " + this.rifiutato + " " +
                "verbalizzato: " + this.verbalizzato;
    }
}