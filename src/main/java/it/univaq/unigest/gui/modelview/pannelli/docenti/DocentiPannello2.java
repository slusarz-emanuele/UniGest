package it.univaq.unigest.gui.modelview.pannelli.docenti;

import it.univaq.unigest.gui.Dialogs;
import it.univaq.unigest.gui.componenti.DialogBuilder;
import it.univaq.unigest.gui.componenti.VistaConDettagliBuilder;
import it.univaq.unigest.gui.util.DialogsParser;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.service.DocenteService;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.function.Function;

public class DocentiPannello2 implements CrudPanel {

    private final DocenteService docenteService;
    private final VistaConDettagliBuilder<Docente> builder;

    public DocentiPannello2(DocenteService docenteService){
        this.docenteService = docenteService;
        this.builder = new VistaConDettagliBuilder<>(docenteService.findAll());
    }

    private void apriDialogAggiungi() {
        DialogBuilder<Docente> dialog = new DialogBuilder<>(
                "Nuovo Docente", "Inserisci i dati del docente",
                campi -> {
                    String cf = DialogsParser.validaCampo(campi, "CF");
                    String nome = DialogsParser.validaCampo(campi, "Nome");
                    String cognome = DialogsParser.validaCampo(campi, "Cognome");
                    LocalDate dataNascita = ((DatePicker) campi.get("Data di Nascita")).getValue();
                    String codiceDocente = DialogsParser.validaCampo(campi, "Codice Docente");
                    boolean isRuolo = ((CheckBox) campi.get("Ruolo")).isSelected();
                    LocalDate ingressoDocente = ((DatePicker) campi.get("Ingresso Univ. Docente")).getValue();
                    String dipartimento = DialogsParser.validaCampo(campi, "Dipartimento");
                    @SuppressWarnings("unchecked")
                    ComboBox<String> comboQualifica = (ComboBox<String>) campi.get("Qualifica");
                    String qualifica = comboQualifica.getValue();

                    Docente d = new Docente(cf, nome, cognome, dataNascita, null,
                            codiceDocente, isRuolo, ingressoDocente, dipartimento, qualifica);
                    return docenteService.create(d);
                },
                d -> { refresh(); Dialogs.showInfo("Successo", "Docente aggiunto correttamente!"); }
        );

        dialog.aggiungiCampo("CF", new TextField());
        dialog.aggiungiCampo("Nome", new TextField());
        dialog.aggiungiCampo("Cognome", new TextField());
        dialog.aggiungiCampo("Data di Nascita", new DatePicker());
        dialog.aggiungiCampo("Codice Docente", new TextField());
        dialog.aggiungiCampo("Ruolo", new CheckBox("Docente di ruolo"));
        dialog.aggiungiCampo("Ingresso Univ. Docente", new DatePicker());
        dialog.aggiungiCampo("Dipartimento", new TextField());
        ComboBox<String> qualifica = new ComboBox<>();
        qualifica.setItems(FXCollections.observableArrayList("Ordinario","Associato","Ricercatore","Contratto"));
        dialog.aggiungiCampo("Qualifica", qualifica);

        dialog.mostra();
    }

    private void mostraDialogModificaDocente(Docente docente) {
        DialogBuilder<Docente> dialog = new DialogBuilder<>(
                "Modifica Docente","Modifica i dati del docente",
                campi -> {
                    String cf = ((TextField) campi.get("CF")).getText();
                    String nome = ((TextField) campi.get("Nome")).getText();
                    String cognome = ((TextField) campi.get("Cognome")).getText();
                    LocalDate dataNascita = ((DatePicker) campi.get("Data di Nascita")).getValue();
                    String codiceDocente = ((TextField) campi.get("Codice Docente")).getText();
                    boolean isRuolo = ((CheckBox) campi.get("Ruolo")).isSelected();
                    LocalDate ingressoDocente = ((DatePicker) campi.get("Ingresso Univ. Docente")).getValue();
                    String dipartimento = ((TextField) campi.get("Dipartimento")).getText();
                    @SuppressWarnings("unchecked")
                    ComboBox<String> comboQualifica = (ComboBox<String>) campi.get("Qualifica");
                    String qualifica = comboQualifica.getValue();

                    docente.setCf(cf);
                    docente.setNome(nome);
                    docente.setCognome(cognome);
                    docente.setDataNascita(dataNascita.toString());
                    docente.setCodiceDocente(codiceDocente);
                    docente.setRuolo(isRuolo);
                    docente.setDataIngressoUniversitaDocente(ingressoDocente);
                    docente.setDipartimento(dipartimento);
                    docente.setQualifica(qualifica);

                    return docenteService.update(docente);
                },
                d -> { refresh(); Dialogs.showInfo("Successo","Docente modificato con successo!"); }
        );

        dialog.aggiungiCampo("CF", new TextField(docente.getCf()));
        dialog.aggiungiCampo("Nome", new TextField(docente.getNome()));
        dialog.aggiungiCampo("Cognome", new TextField(docente.getCognome()));
        dialog.aggiungiCampo("Data di Nascita", new DatePicker(LocalDate.parse(docente.getDataNascita())));
        dialog.aggiungiCampo("Codice Docente", new TextField(docente.getCodiceDocente()));
        CheckBox ruolo = new CheckBox("Docente di ruolo"); ruolo.setSelected(docente.isRuolo());
        dialog.aggiungiCampo("Ruolo", ruolo);
        dialog.aggiungiCampo("Ingresso Univ. Docente", new DatePicker(docente.getDataIngressoUniversitaDocente()));
        dialog.aggiungiCampo("Dipartimento", new TextField(docente.getDipartimento()));
        ComboBox<String> q = new ComboBox<>();
        q.setItems(FXCollections.observableArrayList("Ordinario","Associato","Ricercatore","Contratto"));
        q.setValue(docente.getQualifica());
        dialog.aggiungiCampo("Qualifica", q);

        dialog.mostra();
    }

    private void elimina(Docente d) {
        try {
            docenteService.deleteById(d.getId());
            refresh();
        } catch (Exception e) {
            // TODO: FIXARE
            //Dialogs.showWarn(e.getMessage());
            Dialogs.showError("Errpre", e.getMessage());
        }
    }

    public void refresh() {
        builder.refresh(docenteService.findAll());
    }

    // helpers per DocentiView:
    public void apriDialogAggiungiPubblico() { apriDialogAggiungi(); }
    public void modificaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione","Seleziona un docente"); return; }
        mostraDialogModificaDocente(sel);
    }
    public void eliminaSelezionato() {
        var sel = builder.getTabella().getSelectionModel().getSelectedItem();
        if (sel == null) { Dialogs.showError("Nessuna selezione","Seleziona un docente"); return; }
        elimina(sel);
    }

    public VBox getView (){
        LinkedHashMap<String, Function<Docente, String>> columns = new LinkedHashMap<>();
        columns.put("CF", Docente::getCf);
        columns.put("Nome", Docente::getNome);
        columns.put("Cognome", Docente::getCognome);
        columns.put("Email", Docente::getEmail);
        columns.put("Ruolo", d -> d.isRuolo() ? "Di ruolo" : "Esterno");
        columns.put("Dipartimento", Docente::getDipartimento);
        columns.put("Qualifica", Docente::getQualifica);

        LinkedHashMap<String, Function<Docente, String>> details = new LinkedHashMap<>(columns);
        details.put("Data di Nascita", d -> String.valueOf(d.getDataNascita()));
        details.put("Ingresso Università", d -> String.valueOf(d.getDataIngressoUniversitaDocente()));
        details.put("Codice Docente", Docente::getCodiceDocente);

        return builder.build(
                "Gestione Docenti",
                columns,
                details,
                this::apriDialogAggiungi,
                this::mostraDialogModificaDocente,
                this::elimina
        );
    }

    // in DocentiPannello2
    public VistaConDettagliBuilder<Docente> getBuilder() {
        return builder;
    }


}