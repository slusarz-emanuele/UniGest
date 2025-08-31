package it.univaq.unigest.gui.componenti;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class TabelleHelper {

    /**
     * Genera la tabella dei {@link Docente} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti i docenti
     */
    public static TableView<Docente> generaTabellaFkDocenti(SelectionMode selectionMode){
        // Quantità di docenti
        var tuttiDocenti = Main.getDocenteManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Docente> tabellaDocenti = new TableView<>();
        tabellaDocenti.getSelectionModel().setSelectionMode(selectionMode);
        tabellaDocenti.setPrefHeight(250);
        tabellaDocenti.setPrefWidth(400);

        // Colonna ID
        TableColumn<Docente, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodiceDocente()));
        colId.setPrefWidth(100);

        // Colonna con Nome e Cognome
        TableColumn<Docente, String> colGeneralita = new TableColumn<>("Generalità");
        colGeneralita.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome() + " " + data.getValue().getCognome()));
        colGeneralita.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaDocenti.getColumns().addAll(colId, colGeneralita);
        tabellaDocenti.setItems(FXCollections.observableArrayList(tuttiDocenti));

        return tabellaDocenti;
    }

    /**
     * Genera la tabella degli {@link Studente} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti gli studenti
     */
    public static TableView<Studente> generaTabellaFkStudenti(SelectionMode selectionMode){
        // Quantità di studenti
        var tuttiStudenti = Main.getStudenteManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Studente> tabellaStudenti = new TableView<>();
        tabellaStudenti.getSelectionModel().setSelectionMode(selectionMode);
        tabellaStudenti.setPrefHeight(250);
        tabellaStudenti.setPrefWidth(400);

        // Colonna ID
        TableColumn<Studente, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMatricola()));
        colId.setPrefWidth(100);

        // Colonna con Nome e Cognome
        TableColumn<Studente, String> colGeneralita = new TableColumn<>("Generalità");
        colGeneralita.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome() + " " + data.getValue().getCognome()));
        colGeneralita.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaStudenti.getColumns().addAll(colId, colGeneralita);
        tabellaStudenti.setItems(FXCollections.observableArrayList(tuttiStudenti));

        return tabellaStudenti;
    }

    /**
     * Genera la tabella dei {@link CorsoDiLaurea} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti i Corsi Di Laurea
     */
    public static TableView<CorsoDiLaurea> generaTabellaFkCorsiDiLaurea(SelectionMode selectionMode){
        // Quantità di corsi di laurea
        var tuttiCorsi = Main.getCorsoDiLaureaManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<CorsoDiLaurea> tabellaCorsiDiLaurea = new TableView<>();
        tabellaCorsiDiLaurea.getSelectionModel().setSelectionMode(selectionMode);
        tabellaCorsiDiLaurea.setPrefHeight(250);
        tabellaCorsiDiLaurea.setPrefWidth(400);

        // Colonna ID
        TableColumn<CorsoDiLaurea, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colId.setPrefWidth(100);

        // Colonna con Nome
        TableColumn<CorsoDiLaurea, String> colGeneralita = new TableColumn<>("Nome");
        colGeneralita.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colGeneralita.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaCorsiDiLaurea.getColumns().addAll(colId, colGeneralita);
        tabellaCorsiDiLaurea.setItems(FXCollections.observableArrayList(tuttiCorsi));

        return tabellaCorsiDiLaurea;
    }

    /**
     * Genera la tabella degli {@link Insegnamento} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti gli insegnamenti
     */
    public static TableView<Insegnamento> generaTabellaFkInsegnamento(SelectionMode selectionMode){
        // Quantità di insegnamenti
        var tuttiInsegnamenti = Main.getInsegnamentoManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Insegnamento> tabellaInsegnamenti = new TableView<>();
        tabellaInsegnamenti.getSelectionModel().setSelectionMode(selectionMode);
        tabellaInsegnamenti.setPrefHeight(250);
        tabellaInsegnamenti.setPrefWidth(400);

        // Colonna ID
        TableColumn<Insegnamento, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colId.setPrefWidth(100);

        // Colonna con Nome e Cognome
        TableColumn<Insegnamento, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colNome.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaInsegnamenti.getColumns().addAll(colId, colNome);
        tabellaInsegnamenti.setItems(FXCollections.observableArrayList(tuttiInsegnamenti));

        return tabellaInsegnamenti;
    }

    /**
     * Genera la tabella delle {@link Aula} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutte le aule
     */
    public static TableView<Aula> generaTabellaFkAule(SelectionMode selectionMode){
        // Quantità di aule
        var tutteAule = Main.getAulaManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Aula> tabellaAule = new TableView<>();
        tabellaAule.getSelectionModel().setSelectionMode(selectionMode);
        tabellaAule.setPrefHeight(250);
        tabellaAule.setPrefWidth(400);

        // Colonna ID
        TableColumn<Aula, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colId.setPrefWidth(100);

        // Colonna con Nome
        TableColumn<Aula, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId() + " " + data.getValue().getEdificio()));
        colNome.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaAule.getColumns().addAll(colId, colNome);
        tabellaAule.setItems(FXCollections.observableArrayList(tutteAule));

        return tabellaAule;
    }

    /**
     * Genera la tabella degli {@link Edificio} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti gli edifici
     */
    public static TableView<Edificio> generaTabellaFkEdifici(SelectionMode selectionMode){
        // Quantità di edifici
        var tuttiEdifici = Main.getEdificioManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Edificio> tabellaEdifici = new TableView<>();
        tabellaEdifici.getSelectionModel().setSelectionMode(selectionMode);
        tabellaEdifici.setPrefHeight(250);
        tabellaEdifici.setPrefWidth(400);

        // Colonna ID
        TableColumn<Edificio, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colId.setPrefWidth(100);

        // Colonna con Nome
        TableColumn<Edificio, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        colNome.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaEdifici.getColumns().addAll(colId, colNome);
        tabellaEdifici.setItems(FXCollections.observableArrayList(tuttiEdifici));

        return tabellaEdifici;
    }

    /**
     * Genera la tabella delle {@link Iscrizione} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutte le iscrizioni
     */
    public static TableView<Iscrizione> generaTabellaFkIscrizioni(SelectionMode selectionMode){
        // Quantità di iscizioni
        var tutteIscrizioni = Main.getIscrizioneManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Iscrizione> tabellaIscizioni = new TableView<>();
        tabellaIscizioni.getSelectionModel().setSelectionMode(selectionMode);
        tabellaIscizioni.setPrefHeight(250);
        tabellaIscizioni.setPrefWidth(400);

        // Colonna ID
        TableColumn<Iscrizione, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colId.setPrefWidth(100);

        // Colonna con Nome
        TableColumn<Iscrizione, String> colNome = new TableColumn<>("Generalità");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGeneralita()));
        colNome.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaIscizioni.getColumns().addAll(colId, colNome);
        tabellaIscizioni.setItems(FXCollections.observableArrayList(tutteIscrizioni));

        return tabellaIscizioni;
    }

    /**
     * Genera la tabella degli {@link Appello} per la gestione delle chiavi esterne.
     * @param selectionMode modalità di selezione dei record (singoli o multipli).
     * @return Una {@link TabellaView} con tutti gli appelli
     */
    public static TableView<Appello> generaTabellaFkAppelli(SelectionMode selectionMode){
        // Quantità di appelli
        var tuttiAppelli = Main.getAppelloManager().getAll();

        // Creazione della tabella con dimensioni
        TableView<Appello> tabellaAppello = new TableView<>();
        tabellaAppello.getSelectionModel().setSelectionMode(selectionMode);
        tabellaAppello.setPrefHeight(250);
        tabellaAppello.setPrefWidth(400);

        // Colonna ID
        TableColumn<Appello, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colId.setPrefWidth(100);

        // Colonna con Nome
        TableColumn<Appello, String> colNome = new TableColumn<>("Generalità");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGeneralita()));
        colNome.setPrefWidth(300);

        // Conclusione della creazione della tabella
        tabellaAppello.getColumns().addAll(colId, colNome);
        tabellaAppello.setItems(FXCollections.observableArrayList(tuttiAppelli));

        return tabellaAppello;
    }

}

// TODO: Ridurre tutto a una funzione generica!! (Si, si può fare)