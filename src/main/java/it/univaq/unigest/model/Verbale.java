package it.univaq.unigest.model;

import it.univaq.unigest.model.common.Identificabile;


import java.time.LocalDate;
import java.util.List;

/**
 * La classe {@code Verbale} rappresenta il verbale relativo ad un appello d'esame.
 *
 * <p>Contiene informazioni quali:
 * <ul>
 *   <li>ID del verbale</li>
 *   <li>ID dell'appello associato</li>
 *   <li>Data di chiusura del verbale</li>
 *   <li>Flag per indicare se il verbale è chiuso</li>
 *   <li>Flag per indicare se il verbale è firmato</li>
 *   <li>Eventuali note aggiuntive</li>
 *   <li>Lista degli esami collegati al verbale</li>
 * </ul>
 *
 * <p>Questa classe implementa l'interfaccia {@link Identificabile}.
 */
public class Verbale implements Identificabile<String> {

    private Integer id;
    private String appelloId; // FK
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

    // Getters
    @Override
    public String getId() {return String.valueOf(this.id);}

    @Override
    public void setId(String id){
        this.id = Integer.parseInt(id);
    }

    public String getAppelloId() { return appelloId; }

    public LocalDate getDataChiusura() { return dataChiusura; }

    public boolean getChiuso() { return chiuso; }

    public boolean getFirmato() { return firmato; }

    public String getNote() { return note; }

    public List<Esame> getEsami() { return esami; }

    // Setters
    public void setId(Integer id) { this.id = id; }

    public void setAppelloId(String appelloId) { this.appelloId = appelloId; }

    public void setDataChiusura(LocalDate dataChiusura) { this.dataChiusura = dataChiusura; }

    public void setChiuso(boolean chiuso) { this.chiuso = chiuso; }

    public void setFirmato(boolean firmato) { this.firmato = firmato; }

    public void setNote(String note) { this.note = note; }

    public void setEsami(List<Esame> esami) { this.esami = esami; }

    @Override
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