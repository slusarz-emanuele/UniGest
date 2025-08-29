package it.univaq.unigest.model;

import java.util.List;

public class Insegnamento{

    private String id;
    private String nome;
    private Integer cfu;
    private String corsodiLaureaId;
    private List<String> docenti;
    private Integer anno;
    private Integer semestre;

    public Insegnamento(String id,
    String nome,
    Integer cfu,
    String corsodiLaureaId,
    List<String> docenti,
    Integer anno,
    Integer semestre){
        this.id=id;
        this.nome=nome;
        this.cfu=cfu;
        this.corsodiLaureaId=corsodiLaureaId;
        this.docenti=docenti;
        this.anno=anno;
        this.semestre=semestre;
    }
    public String getId() { return id; }

    public String getNome() { return nome; }

    public Integer getCfu() { return cfu; }

    public String getCorsoDiLaureaId() { return corsodiLaureaId; }

    public List<String> getDocenti() { return docenti; }

    public Integer getAnno() { return anno; }

    public Integer getSemestre() { return semestre; }

    
    public void setId(String id) { this.id = id; }

    public void setNome(String nome) { this.nome = nome; }

    public void setCfu(Integer cfu) { this.cfu = cfu; }

    public void setCorsoDiLaureaId(String corsoDiLaureaId) { this.corsodiLaureaId = corsoDiLaureaId; }

    public void setDocenti(List<String> docenti) { this.docenti = docenti; }

    public void setAnno(Integer anno) { this.anno = anno; }

    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public void stampaDocenti(){
        System.out.println("Docenti assegnati all'insegnamento \""+this.nome+"\":");
        if(docenti==null || docenti.isEmpty()){
            System.out.println("Nessun docente assegnato");
             }else{ 
                for (String docenteId : docenti) {
                System.out.println("• " + docenteId);
            }
             }
    }
    public void aggiungiDocente(String docenteId){
        if(!this.docenti.contains(docenteId)){
            this.docenti.add(docenteId);

        }
    }
    public void rimuoviDocente(String docenteId){
        this.docenti.remove(docenteId);
    }
    public void caricaDocentiDinamicamente(){
        if(this.getId()==null) return ;
    }
        public String toString() {
        return "ID: " + id +
                ", Nome: " + nome +
                ", CFU: " + cfu +
                ", CorsoDiLaureaID: " + corsodiLaureaId +
                ", Docenti: " + docenti +
                ", Anno: " + anno +
                ", Semestre: " + semestre;
    }

}