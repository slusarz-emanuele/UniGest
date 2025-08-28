package it.univaq.unigest.model;

import it.univaq.unigest.manager.AppelloManager;
import it.univaq.unigest.manager.EsameManager;
import it.univaq.unigest.manager.IscrizioneManager;

import java.time.LocalDate;
import java.util.List;

public class Verbale{
    
    //attributi
    private Integer id;
    private String appelloId;
    private LocalDate dataChiusura;
    private boolean chiuso;
    private boolean firmato;
    private String note;
    private List<Esame> esami;

public Verbale(Integer id,
String appelloId,
LocalDate dataChiusura,
boolean chiuso,
boolean firmato,
String note,
List<Esame> esami){
    this.id = id;
    this.appelloId = appelloId;
    this.dataChiusura = dataChiusura;
    this.chiuso = chiuso;
    this.firmato = firmato;
    this.note = note;
    this.esami = esami;
}

    //metodi getter
public Integer getId() { return id; }

    public String getAppelloId() { return appelloId; }

    public LocalDate getDataChiusura() { return dataChiusura; }

    public boolean getChiuso() { return chiuso; }

    public boolean getFirmato() { return firmato; }

    public String getNote() { return note; }

    public List<Esame> getEsami() { return esami; }

    //metodi setter
    public void setId(Integer id) { this.id = id; }

    public void setAppelloId(String appelloId) { this.appelloId = appelloId; }

    public void setDataChiusura(LocalDate dataChiusura) { this.dataChiusura = dataChiusura; }

    public void setChiuso(boolean chiuso) { this.chiuso = chiuso; }

    public void setFirmato(boolean firmato) { this.firmato = firmato; }

    public void setNote(String note) { this.note = note; }

    public void setEsami(List<Esame> esami) { this.esami = esami; } 

     //metodo per caricare tutti gli esami dinamicamente
    public void caricaEsamiDinamicamente(IscrizioneManager iscrizioneManager,
                                        EsameManager esameManager){
        if(this.getId() == null) return ;

        //1) dagli appelli troviamo le iscrizioni 
        List<Integer> iscrizioniIds = iscrizioneManager.getAll().stream()
            .filter(i -> i.getRidAppello() == Integer.parseInt(this.getAppelloId()))
            .map(Iscrizione::getId())
            .toList();

        //2) dalle iscrizioni troviamo gli esami
        List<Esame> esamiTrovvati = esameManager.getAll().stream()
            .filter(e -> iscrizioniIds.contains(Integer.valueOf(e.getIscrizioneId())))
            .toList();

        //3) salviamo tutto 
        this.setEsami(esamiTrovvati);
        }

    //override toString
     public String toString() {
        return "id: " + id +
                ", appelloId: " + appelloId +
                ", dataChiusura: " + dataChiusura +
                ", chiuso: " + chiuso +
                ", firmato: " + firmato +
                ", note: " + note +
                ", esami: " + (esami != null ? esami.size() + " esami" : "nessuno");
    }
}
