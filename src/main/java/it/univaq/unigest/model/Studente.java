package it.univaq.unigest.model;

import java.time.LocalDate;
import java.util.List;

public class Studente extends Persona{

    // Attributi di instanza
    private String matricola;
    private CorsoDiLaurea corsoDiLaurea;
    private LocalDate dataImmatricolazione;
    private List<Esame> esami;
    private Integer cfu;
    private Double mediaPonderata;
    private Double mediaAritmetica;

    // Metodo costruttore
    public Studente(){
        super();
    }

    // Metodo costruttore parametrizzato
    public Studente(String cf,
                    String nome,
                    String cognome,
                    String email,
                    String dataNascita,
                    String dataIngressoUniversita,
                    String matricola,
                    CorsoDiLaurea corsoDiLaurea,
                    LocalDate dataImmatricolazione,
                    List<Esame> esami,
                    Integer cfu){
        super(cf, nome, cognome, email, dataNascita, dataIngressoUniversita);
        this.matricola = matricola;
        this.corsoDiLaurea = corsoDiLaurea;
        this.dataImmatricolazione = dataImmatricolazione;
        this.esami = esami;
        this.cfu = cfu;
        this.mediaPonderata = calcolaMediaPonderata();
        this.mediaAritmetica = calcolaMediaAritmetica();
    }

    // Metodi getter
    public String getMatricola(){
        return this.matricola;
    }

    public CorsoDiLaurea getCorsoDiLaurea(){
        return this.corsoDiLaurea;
    }

    public LocalDate getDataImmatricolazione(){
        return this.dataImmatricolazione;
    }

    public List<Esame> getEsami(){
        return this.esami;
    }

    public Integer getCfu(){
        return this.cfu;
    }

    public Double getMediaPonderata(){
        return calcolaMediaPonderata();
    }

    public Double getMediaAritmetica(){
        return calcolaMediaAritmetica();
    }

    // TODO: getEta

    // Metodi Setter
    public void setMatricola(String matricola){
        this.matricola = matricola;
    }

    public void setCorsoDiLaurea(CorsoDiLaurea corsoDiLaurea){
        this.corsoDiLaurea = corsoDiLaurea;
    }

    public void setDataImmatricolazione(LocalDate dataImmatricolazione){
        this.dataImmatricolazione = dataImmatricolazione;
    }

    public void setEsami(List<Esame> esami){
        this.esami = esami;
    }

    public void setCfu(Integer cfu){
        this.cfu = cfu;
    }

    // ToString
    public String toString(){
        return super.toString() +
                "Matricola: " + this.matricola + ", " +
                "CorsoDiLaurea: " + this.corsoDiLaurea + ", " +
                "DataImmatricolazione: " + this.dataImmatricolazione + ", " +
                "Cfu: " + this.cfu + ", "+
                "MediaPonderata: " + this.mediaPonderata;
    }

    // Metodo per calcolare la media ponderata
    public Double calcolaMediaPonderata(){

        if (this.esami == null || this.esami.isEmpty()) {
            return null;
        }

        Double somma = 0.0;
        Integer sommaCfu = 0;

        for (int i = 0; i < this.esami.size(); i++) {
            Integer voto = this.esami.get(i).getVoto();
            Integer cfu = this.esami.get(i).getCfu();

            somma = somma + (voto * cfu);
            sommaCfu += cfu;
        }

        if (sommaCfu == 0){
            return null;
        }

        return somma/sommaCfu;
    }

    // Metodo per calcolare la media ponderata
    public Double calcolaMediaAritmetica(){

        if (this.esami == null || this.esami.isEmpty()) {
            return null;
        }

        Double somma = 0.0;

        for (int i = 0; i < this.esami.size(); i++) {
            somma += this.esami.get(i).getVoto();
        }

        return somma / this.esami.size();
    }

    // Metodo per aggiungere gli esami
    public void aggiungiEsame(Esame esame){
        this.esami.add(esame);
        // TODO: EsameHelper deve aggiornare la lista degli esami
    }

}
