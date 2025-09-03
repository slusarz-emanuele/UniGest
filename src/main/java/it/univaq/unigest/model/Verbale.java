package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;
import it.univaq.unigest.manager.AppelloManager;
import it.univaq.unigest.manager.EsameManager;
import it.univaq.unigest.manager.IscrizioneManager;


import java.time.LocalDate;
import java.util.List;

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

    // Metodo per caricare tutti gli esami dinamicamente
        public void caricaEsamiDinamicamente(IscrizioneManager iscrizioneManager,
                                            EsameManager esameManager){
            if (this.getId() == null) return;

            // 1. Dagli appelli troviamo le iscrizioni
            int appelloId = Integer.parseInt(this.getAppelloId());

            List<Integer> iscrizioniIds = iscrizioneManager.getAll().stream()
                    .filter(i -> i.getRidAppello() == appelloId)
                    .map(i -> Integer.parseInt(i.getId()))  // <-- parse da String a int
                    .toList();


            // 2. Dalle iscrizioni troviamo gli esami
            List<Esame> esamiTrovvati = esameManager.getAll().stream()
                    .filter(e -> iscrizioniIds.contains(Integer.valueOf(e.getIscrizioneId())))
                    .toList();

            // 3. Salviamo tutto
            this.setEsami(esamiTrovvati);
        }
    
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