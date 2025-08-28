package it.univaq.unigest.model;

import it.univaq.unigest.manager.EsameManager;
import it.univaq.unigest.manager.IscrizioneManager;

import java.time.LocalDate;
import java.util.List;

public class Studente extends Persona{
     //attributi di instanza
     private String matricola;
     private String corsoDiLaurea;
     private LocalDate dataImmatricolazione;
     private List<Esame> esami;
     private Integer cfu;
     private Double mediaPonderata;
     private Double mediaAritmetica;
     //metodo costruttore parametrizzato
     public Studente( String cf,
     String nome,
     String cognome,
     LocalDate dataNascita,
     String dataIngressoUniversita,
     String matricola,
     String corsoDiLaurea,
     LocalDate dataImmatricolazione,
     List<Esame> esami){
        super(cf,nome,cognome,dataNascita,dataIngressoUniversita);
        this.matricola = matricola;
        this.corsoDiLaurea = corsoDiLaurea;
        this.dataImmatricolazione = dataImmatricolazione;
        this.esami = esami;
        this.cfu = this.calcolaCfu();
        this.mediaPonderata = calcolaMediaPonderata();
        this.mediaAritmetica = calcolaMediaAritmetica();
     }

     //metodi getter
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
     
     //metodi setter
     public void setMatricola(String matricola){
        this.matricola = matricola;
     }
     
     public void setCorsoDiLaurea(String corsoDiLaurea){
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

     //metodo toString
     public String toString(){
        return super.toString() + "Matricola: "+this.matricola+", "+
        "CorsoDiLaurea: "+ this.corsoDiLaurea+", "+
        "DataImmatricolazione: "+this.dataImmatricolazione+", "+
        "Cfu: "+this.cfu+", "+
        "MediaPonderata: "+this.mediaPonderata+", "+
        "MediaAritmetica: "+this.mediaAritmetica+". ";
     }

     //metodo per calcolare la media ponderata
     public Double calcolaMediaPonderata(){
        if(this.esami == null || this.esami.isEmpty()){
            return 0.0;
        }
        Double somma = 0.0;
        Integer sommaCfu = 0;
        
        for(int i=0; i < this.esami.size(); i++){
            Double voto = this.esami.get(i).getVoto();
            Integer cfu = this.esami.get(i).getCfu();

            somma = somma + (voto*cfu);
            sommaCfu += cfu;
        }
          
        if (sommaCfu == 0){
            return null;
        }
          
        return somma/sommaCfu;
     }

     //metodo che calcola la media aritmetica
     public Double calcolaMediaAritmetica(){
        if(this.esami == null || this.esami.isEmpty()){
            return 0.0;
        }
          
        Double somma = 0.0;
          
        for(int i=0; i < this.esami.size(); i++){
            somma += this.esami.get(i).getVoto();
        }
        return somma/this.esami.size();
     }

     //metodo per il calcolo dei cfu
     public int calcolaCfu(){
        if(this.esami == null||this.esami.isEmpty()){
            return 0;
        }
          
        int somma = 0;
          
        for(int i=0; i < this.esami.size();i++){
            somma += this.esami.get(i).getCfu();
        }
          
        return somma;
     }

     //metodo per generare email
     protected String generaEmail(){
        if(this.getNome() != null && this.getCognome() != null){
            return this.getNome().toLowerCase()+"."+this.getCognome().toLowerCase()+ "@student.univaq.it";
        }
        return null;
     }
     //metodo per caricare tutti gli esami dinamicamente
     public void caricaEsamiDinamicamente(IscrizioneManager iscrizioneManager,
                                         EsameManager esameManager){
          if(this.getCf() == null) return ;

          //1) trova tutte le iscrizioni fatte da questo studente
          List<Integer> iscrizioniIds = iscrizioneManager.getAll().stream()
               .filter(i -> i.getRidStudenteCf().equalsIgnoreCase(this.getCf()))
               .map(Iscrizione::getId)
               toList();

          //2) trova tutti gli esami legati a quelle iscrizioni
          List<Esame> esamiTrovati = esameManager.getAll().stream()
               .filter(e -> iscrizioniIds.contains(Integer.valueOf(e.getIscrizioneId())))
               .toList();

          //3) salva nella proprietà locale
          this.setEsami(esamiTrovati);
     }

     //metodo per aggiungere gli esami
     public void aggiungiEsame(Esame esame){
        this.esami.add(esame);
     }
     
}
