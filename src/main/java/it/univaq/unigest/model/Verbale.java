package it.univaq.unigest.model;

import java.time.LocalDate;
import java.util.List;

public class Verbale{
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
    this.id=id;
    this.appelloId=appelloId;
    this.dataChiusura=dataChiusura;
    this.chiuso=chiuso;
    this.firmato=firmato;
    this.note=note;
    this.esami=esami;
}
public Integer getId() { return id; }

    public String getAppelloId() { return appelloId; }

    public LocalDate getDataChiusura() { return dataChiusura; }

    public boolean getChiuso() { return chiuso; }

    public boolean getFirmato() { return firmato; }

    public String getNote() { return note; }

    public List<Esame> getEsami() { return esami; }


    public void setId(Integer id) { this.id = id; }

    public void setAppelloId(String appelloId) { this.appelloId = appelloId; }

    public void setDataChiusura(LocalDate dataChiusura) { this.dataChiusura = dataChiusura; }

    public void setChiuso(boolean chiuso) { this.chiuso = chiuso; }

    public void setFirmato(boolean firmato) { this.firmato = firmato; }

    public void setNote(String note) { this.note = note; }

    public void setEsami(List<Esame> esami) { this.esami = esami; }
    
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