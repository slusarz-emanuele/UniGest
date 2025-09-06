package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;
import it.univaq.unigest.manager.EsameManager;
import it.univaq.unigest.manager.IscrizioneManager;

import java.time.LocalDate;
import java.util.List;

public class Studente extends Persona implements Identificabile<String> {

    // Attributi di istanza
    private String matricola;
    private String corsoDiLaurea;
    private LocalDate dataImmatricolazione;
    private List<Esame> esami;
    private Integer cfu;
    private Double mediaPonderata;
    private Double mediaAritmetica;

    // Metodo costruttore parametrizzato
    public Studente(String cf,
                    String nome,
                    String cognome,
                    LocalDate dataNascita,
                    String dataIngressoUniversita,
                    String matricola,
                    String corsoDiLaurea,
                    LocalDate dataImmatricolazione,
                    List<Esame> esami){
        super(cf, nome, cognome, dataNascita, dataIngressoUniversita);
        this.matricola = matricola;
        this.corsoDiLaurea = corsoDiLaurea;
        this.dataImmatricolazione = dataImmatricolazione;
        this.esami = esami;
        this.cfu = this.calcolaCfu();
        this.mediaPonderata = calcolaMediaPonderata();
        this.mediaAritmetica = calcolaMediaAritmetica();
    }

    public Studente() {
        super();
    }

    // Metodi getter

    @Override
    public String getId() {
        return getMatricola();
    }

    public String getMatricola(){
        return this.matricola;
    }

    public String getCorsoDiLaurea(){
        return this.corsoDiLaurea;
    }

    public LocalDate getDataImmatricolazione(){
        return this.dataImmatricolazione;
    }

    public List<Esame> getEsami(){
        return this.esami;
    }

    public Integer getCfu(){
        return calcolaCfu();
    }

    public Double getMediaPonderata(){
        return calcolaMediaPonderata();
    }

    public Double getMediaAritmetica(){
        return calcolaMediaAritmetica();
    }


    // Metodi setter

    @Override
    public void setId(String id) {
        setMatricola(id);
    }

    public void setMatricola(String matricola){
        this.matricola=matricola;
    }

    public void setCorsoDiLaurea(String corsoDiLaurea){
        this.corsoDiLaurea=corsoDiLaurea;
    }

    public void setDataImmatricolazione(LocalDate dataImmatricolazione){
        this.dataImmatricolazione=dataImmatricolazione;
    }

    public void setEsami(List<Esame> esami){
        this.esami=esami;
    }

    public void setCfu(Integer cfu){
        this.cfu=cfu;
    }


    // ToString
    @Override
    public String toString(){
        return super.toString() + "Matricola: "+this.matricola+", "+
                "CorsoDiLaurea: "+ this.corsoDiLaurea+", "+
                "DataImmatricolazione: "+this.dataImmatricolazione+", "+
                "Cfu: "+this.cfu+", "+
                "MediaPonderata: "+this.mediaPonderata+", "+
                "MediaAritmetica: "+this.mediaAritmetica+". ";
    }


    // Metodo per calcolare la media ponderata
    public Double calcolaMediaPonderata(){
        if(this.esami == null || this.esami.isEmpty()){
            return 0.0;
        }
        Double somma=0.0;
        Integer sommaCfu=0;

        for(int i=0;i<this.esami.size();i++){
            Double voto=this.esami.get(i).getVoto();
            Integer cfu =this.esami.get(i).getCfu();

            somma=somma+(voto*cfu);
            sommaCfu +=cfu;
        }
        if (sommaCfu ==0){
            return null;
        }
        return somma/sommaCfu;
    }

    // Metodo per calcolare la media aritmetica
    public Double calcolaMediaAritmetica(){
        if(this.esami == null || this.esami.isEmpty()){
            return 0.0;
        }
        Double somma=0.0;
        for(int i=0;i<this.esami.size();i++){
            somma += this.esami.get(i).getVoto();
        }
        return somma/this.esami.size();
    }

    // Metodo per calcolare i CFU
    public int calcolaCfu(){
        if(this.esami==null||this.esami.isEmpty()){
            return 0;
        }
        int somma=0;
        for(int i=0;i<this.esami.size();i++){
            somma += this.esami.get(i).getCfu();
        }
        return somma;
    }

    // Metodo per generare la mail
    @Override
    protected String generaEmail(){
        if(this.getNome()!= null && this.getCognome()!= null){
            return this.getNome().toLowerCase()+"."+this.getCognome().toLowerCase()+ "@student.univaq.it";
        }
        return null;
    }

    // Metodo per aggiungere gli esami
    public void aggiungiEsame(Esame esame){
        this.esami.add(esame);
    }

    //Metodo per caricare tutti gli esami dinamicamente
    public void caricaEsamiDinamicamente(IscrizioneManager iscrizioneManager,
                                         EsameManager esameManager) {
        if (this.getCf() == null) return;

        // 1. Trova tutte le iscrizioni fatte da questo studente
        List<Integer> iscrizioniIds = iscrizioneManager.getAll().stream()
                .filter(i -> i.getRidStudenteCf().equalsIgnoreCase(this.getCf()))
                .map(i -> Integer.parseInt(i.getId()))
                .toList();

        // 2. Trova tutti gli esami legati a quelle iscrizioni
        List<Esame> esamiTrovati = esameManager.getAll().stream()
                .filter(e -> iscrizioniIds.contains(Integer.valueOf(e.getIscrizioneId())))
                .toList();

        // 3. Salva nella proprietà locale
        this.setEsami(esamiTrovati);
    }

}