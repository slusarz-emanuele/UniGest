package it.univaq.unigest.model;

public class Esame {

    // Attributi
    private String id;              // UUID
    private String iscrizioneId;    // FK per identificare lo studente e l’appello
    private Integer voto;               // Voto da 18 a 30
    private boolean lode;           // Solo se voto = 30
    private boolean rifiutato;      // true se lo studente ha rifiutato
    private boolean verbalizzato;   // true se inserito in un verbale

    // Costruttore
    public Esame(){

    }

    // Costruttore parametrizzato
    public Esame(String id,
                 String iscrizioneId,
                 int voto,
                 boolean lode,
                 boolean rifiutato,
                 boolean verbalizzato){
        this.id = id;
        this.iscrizioneId = iscrizioneId;
        this.voto = voto;
        this.lode = lode;
        this.rifiutato = rifiutato;
        this.verbalizzato = verbalizzato;
    }

    // Metodi getter
    public String getId(){
        return this.id;
    }

    public String getIscrizioneId(){
        return this.iscrizioneId;
    }

    public Integer getVoto(){
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
        // TODO: Da fixare con l'implementazione degli Helper per Esame e Inseganmento
        return 1;
    }

    // Metodi setter
    public void setId(String id){
        this.id = id;
    }

    public void setIscrizioneId(String iscrizioneId){
        this.iscrizioneId = iscrizioneId;
    }

    public void setVoto(Integer voto){
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

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "iscrizioneId: " + this.iscrizioneId + " " +
                "voto: " + this.voto + " " +
                "lode: " + this.lode + " " +
                "rifiutato: " + this.rifiutato + " " +
                "verbalizzato: " + this.verbalizzato;
    }
}
