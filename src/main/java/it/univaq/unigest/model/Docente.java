package it.univaq.unigest.model;

import it.univaq.unigest.common.Identificabile;

import java.time.LocalDate;

public class Docente extends Persona implements Identificabile<String> {

    private String codiceDocente;
    private boolean isRuolo;
    private LocalDate dataIngressoUniversitaDocente;
    private String dipartimento;
    private String qualifica;

    public Docente(String cf,
    String nome,
    String cognome,
    LocalDate dataNascita,
    String dataIngressoUniversita,
    String codiceDocente,
    boolean isRuolo,
    LocalDate dataIngressoUniversitaDocente,
    String dipartimento,
    String qualifica){
        super(cf,nome,cognome,dataNascita,dataIngressoUniversita);
        this.codiceDocente=codiceDocente;
        this.isRuolo=isRuolo;
        this.dataIngressoUniversitaDocente=dataIngressoUniversitaDocente;
        this.dipartimento=dipartimento;
        this.qualifica=qualifica;
    }

    public String getCodiceDocente(){
        return this.codiceDocente;
    }
    public boolean isRuolo(){
        return this.isRuolo;
    }
    public LocalDate getDataIngressoUniversitaDocente(){
        return this.dataIngressoUniversitaDocente;
    }
    public String getDipartimento(){
        return this.dipartimento;
    }
     public String getQualifica() {
        return this.qualifica;
    }

    
    public void setCodiceDocente(String codiceDocente) {
        this.codiceDocente = codiceDocente;
    }

    public void setRuolo(boolean ruolo) {
        this.isRuolo = ruolo;
    }

    public void setDataIngressoUniversitaDocente(LocalDate dataIngressoUniversitaDocente) {this.dataIngressoUniversitaDocente = dataIngressoUniversitaDocente;}

    public void setDipartimento(String dipartimento) {
        this.dipartimento = dipartimento;
    }

    public void setQualifica(String qualifica) {
        this.qualifica = qualifica;
    }
    protected String generaEmail(){
        return this.getNome().toLowerCase()+"."+this.getCognome().toLowerCase()+ "@univaq.it";
    }
    public String toString() {
        return super.toString() +
                "CodiceDocente: " + codiceDocente + ", " +
                "Ruolo: " + (isRuolo ? "Di ruolo" : "Esterno") + ", " +
                "Ingresso Università: " + dataIngressoUniversitaDocente + ", " +
                "Dipartimento: " + dipartimento + ", " +
                "Qualifica: " + qualifica;
    }

    @Override public String getId() { return getCodiceDocente(); }
    @Override public void setId(String id) { setCodiceDocente(id); }
}