package it.univaq.unigest.util;

public class Impostazioni {

    private String regolaIntegrita; // Da deprecare
    private boolean backupAutomatico;
    private int numeroGiorniBackupAutomatico;
    private String lingua;

    public Impostazioni() {
        this.regolaIntegrita = "NO_ACTION"; // Credo che sia da rimuovere
        this.backupAutomatico = false;
        this.numeroGiorniBackupAutomatico = 7;
        this.lingua = LinguaApplicazione.ITALIANO.name();
    }

    public String getRegolaIntegrita() {
        return regolaIntegrita;
    }

    public void setRegolaIntegrita(String regolaIntegrita) {
        this.regolaIntegrita = regolaIntegrita;
    }

    public boolean isBackupAutomatico() {
        return backupAutomatico;
    }

    public void setBackupAutomatico(boolean backupAutomatico) {
        this.backupAutomatico = backupAutomatico;
    }
    public int getNumeroGiorniBackupAutomatico() {
        return numeroGiorniBackupAutomatico;
    }

    public void setNumeroGiorniBackupAutomatico(int numeroGiorniBackupAutomatico) {
        this.numeroGiorniBackupAutomatico = numeroGiorniBackupAutomatico;
    }

    public String getLingua() {
        return lingua;
    }

    public void setLingua(String lingua) {
        this.lingua = lingua;
    }

}
