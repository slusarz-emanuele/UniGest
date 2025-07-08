package it.univaq.unigest.model;

public class Insegnamento {

    // Attributi
    private String id;                // Codice insegnamento (es. INF123)
    private String nome;             // Nome esteso
    private Integer cfu;                 // Crediti formativi
    private String corsoDiLaureaId;  // FK verso corso di laurea
    private String docenteId;        // FK verso Persona con ruolo DOCENTE
    private Integer anno;                // Anno del piano di studi (1, 2, 3)
    private Integer semestre;           // Semestre (1 o 2)

    // Costruttore
    public Insegnamento(){

    }

    // Costruttore parametrizzato
    public Insegnamento(String id,
                        String nome,
                        Integer cfu,
                        String corsoDiLaureaId,
                        String docenteId,
                        Integer anno,
                        Integer semestre){
        this.id = id;
        this.nome = nome;
        this.cfu = cfu;
        this.corsoDiLaureaId = corsoDiLaureaId;
        this.docenteId = docenteId;
        this.anno = anno;
        this.semestre = semestre;
    }

    // Metodi getter
    public String getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public Integer getCfu(){
        return this.cfu;
    }

    public String getCorsoDiLaureaId(){
        return this.corsoDiLaureaId;
    }

    public String getDocenteId(){
        return this.docenteId;
    }

    public Integer getAnno(){
        return this.anno;
    }

    public Integer getSemestre(){
        return this.semestre;
    }

    // Metodi setter
    public void setId(String id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCfu(Integer cfu){
        this.cfu = cfu;
    }

    public void setCorsoDiLaureaId(String corsoDiLaureaId){
        this.corsoDiLaureaId = corsoDiLaureaId;
    }

    public void setDocenteId(String docenteId){
        this.docenteId = docenteId;
    }

    public void setAnno(Integer anno){
        this.anno = anno;
    }

    public void setSemestre(Integer semestre){
        this.semestre = semestre;
    }

    // ToString
    public String toString(){
        return "id: " + this.id + " " +
                "nome: " + this.nome + " " +
                "cfu: " + this.cfu + " " +
                "corsoDiLaureaId: " + this.corsoDiLaureaId + " " +
                "docenteId: " + this.docenteId + " " +
                "anno: " + this.anno + " " +
                "semestre: " + this.semestre;
    }
}
