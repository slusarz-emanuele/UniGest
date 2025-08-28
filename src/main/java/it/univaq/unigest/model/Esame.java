package it.univaq.unigest.model;

public class Esame{
   //attributi
    private String id;                //id
    private String iscrizione;        // fk per identificare lo studente e l'appello
    private Double voto;
    private boolean lode;
    private boolean rifiutato;
    private boolean verbalizzato;

    //costruttore
    public Esame(String id,
                 String iscrizione,
                 Double voto,
                 boolean lode,
                 boolean rifiutato,
                 boolean verbalizzato,){
        this.id = id;
        this.iscrizione = iscrizione;
        this.voto = voto;
        this.lode = lode;
        this.rifiutato = rifiutato;
        this.verbalizzato = verbalizzato;
    }

    //metodi getter
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

    //metodi setter
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

   //metodo toString
    public String toString(){
        return "id: " + this.id + " " +
                "iscrizioneId: " + this.iscrizioneId + " " +
                "voto: " + this.voto + " " +
                "lode: " + this.lode + " " +
                "rifiutato: " + this.rifiutato + " " +
                "verbalizzato: " + this.verbalizzato;
    }
}
