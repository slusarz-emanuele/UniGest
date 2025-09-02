package it.univaq.unigest.gui.modelview.pannelli.studenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.service.StudenteService;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class StudentiPannello2 implements CrudPanel {

    // Etichette
    private static final String L_CF = "CF";
    private static final String L_NOME = "Nome";
    private static final String L_COGNOME = "Cognome";
    private static final String L_DATA_NASCITA = "Data di Nascita";
    private static final String L_EMAIL = "Email";
    private static final String L_MATRICOLA = "Matricola";
    private static final String L_CORSO_DI_LAUREA = "Corso Di Laurea";
    private static final String L_CFU = "Cfu";
    private static final String L_MEDIA_PONDERATA = "Media Ponderata";
    private static final String L_MEDIA_ARITMETICA = "Media Aritmetica";
    private static final String L_DATA_IMMATRICOLAZIONE = "Data Immatricolazione";

    // Utility
    private final StudenteService studenteService;
    private final VistaConDettagliBuilder<Studente> builder;

    // Costruttore
    public StudentiPannello2(StudenteService studenteService){
        this.studenteService = studenteService;
        this.builder = new VistaConDettagliBuilder<>(studenteService.findAll());
    }

    // Api crud panel
    @Override
    public VBox getView (){
        return builder.build(
          "Gestione Studenti",
          colonne();
          dettagli();
          this::apriDialogAggiungi,
          this::mostraDialogModifica,
          this::elimina
        );
    }

    @Override
    public void apriDialogAggiungiPubblico() {
        apriDialogAggiungi();
    }

    @Override
    public void modificaSelezionato(){
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) {
            Dialogs.showError("Nessuna Selezione", "Seleziona uno Studente");
            return;
        }
        elimina(sel);
    }

    @Override
    public void refresh (){
        builder.refresh(studenteService.findAll());
    }

    public VistaConDettagliBuilder<Studente> getBuilder(){
        return builder;
    }

    // Colonne
    private LinkedHashMap<String, Function<Studente, String>> colonne() {
        LinkedHashMap<String, Function<Studente, String>> columns = new LinkedHashMap<>();
        columns.put(L_CF, Studente::getCf);
        columns.put(L_NOME, Studente::getNome);
        columns.put(L_COGNOME, Studente::getCognome);
        columns.put(L_EMAIL, Studente::getEmail);
        columns.put(L_MATRICOLA, Studente::getMatricola);
        columns.put(L_CORSO_DI_LAUREA, Studente::getCorsoDiLaurea);
        columns.put(L_CFU, Studente::getCfu);
        columns.put(L_MEDIA_ARITMETICA)
        columns.put(L_MEDIA_PONDERATA)
        return columns;
    }

}