package it.univaq.unigest.gui.componenti;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.util.ParametrizzazioneHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class VistaConDettagliBuilder<T> {

    private final ObservableList<T> items;
    private final TableView<T> tabella = new TableView<>();

    private Runnable azioneAggiungi;
    private Consumer<T> azioneModifica;
    private Consumer<T> azioneElimina;

    // Mappa etichetta --> azione da eseguire al click
    private final Map<String, Consumer<T>> linkActions = new LinkedHashMap<>();

    public VistaConDettagliBuilder(List<T> lista) {
        this.items = FXCollections.observableArrayList(lista);
        tabella.setItems(items);
    }

    /**
     * Permette di rendere un campo "cliccabile" con una azione.
     * @param etichetta nome del campo (come appare in campiDettagli)
     * @param action azione da eseguire sul record selezionato
     */
    public void setLinkAction(String etichetta, Consumer<T> action) {
        linkActions.put(etichetta, action);
    }


    public VBox build(String titolo,
                      LinkedHashMap<String, Function<T, String>> colonneTabella,
                      LinkedHashMap<String, Function<T, String>> campiDettagli,
                      Runnable onAggiungi,
                      Consumer<T> onModifica,
                      Consumer<T> onElimina) {

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Titolo + Filtro
        HBox topBar = new HBox(10);
        Label labelTitolo = new Label(titolo);
        labelTitolo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField filtroField = new TextField();
        filtroField.setPromptText(Main.getParametrizzazioneHelper().getBundle().getString("field.filtra"));
        filtroField.setPrefWidth(200);

        topBar.getChildren().addAll(labelTitolo, filtroField);
        topBar.setAlignment(Pos.CENTER_LEFT);
        layout.getChildren().add(topBar);

        HBox contenitore = new HBox(10);
        contenitore.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // === SINISTRA ===
        VBox sinistra = new VBox(10);
        sinistra.setPadding(new Insets(10));
        sinistra.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        HBox.setHgrow(sinistra, Priority.ALWAYS);
        VBox.setVgrow(sinistra, Priority.ALWAYS);

        tabella.getColumns().clear();
        colonneTabella.forEach((titoloColonna, extractor) -> {
            TableColumn<T, String> col = new TableColumn<>(titoloColonna);
            col.setCellValueFactory(cell -> new SimpleStringProperty(extractor.apply(cell.getValue())));
            tabella.getColumns().add(col);
        });

        // Pulsanti CRUD
        HBox pulsanti = new HBox(10);
        pulsanti.setAlignment(Pos.CENTER_LEFT);
        Button btnAggiungi = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.aggiungi"));
        Button btnModifica = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.modifica"));
        Button btnElimina = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.elimina"));
        Button btnDeseleziona = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.deseleziona"));

        btnAggiungi.setOnAction(e -> onAggiungi.run());
        btnModifica.setOnAction(e -> {
            T selected = tabella.getSelectionModel().getSelectedItem();
            if (selected != null) onModifica.accept(selected);
        });
        btnElimina.setOnAction(e -> {
            T selected = tabella.getSelectionModel().getSelectedItem();
            if (selected != null) onElimina.accept(selected);
        });
        btnDeseleziona.setOnAction(e -> tabella.getSelectionModel().clearSelection());

        pulsanti.getChildren().addAll(btnAggiungi, btnModifica, btnElimina, btnDeseleziona);

        sinistra.getChildren().addAll(tabella, pulsanti);
        VBox.setVgrow(tabella, Priority.ALWAYS);

        // === DESTRA ===
        VBox destra = new VBox(10);
        destra.setPadding(new Insets(10));
        destra.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #F9F9F9;");
        HBox.setHgrow(destra, Priority.ALWAYS);
        VBox.setVgrow(destra, Priority.ALWAYS);
        Label lblDettagli = new Label(Main.getParametrizzazioneHelper().getBundle().getString("label.dettagli"));
        lblDettagli.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        destra.getChildren().add(lblDettagli);

        Map<String, Node> campiDettaglioNode = new LinkedHashMap<>();
        campiDettagli.forEach((etichetta, extractor) -> {
            if (linkActions.containsKey(etichetta)) {
                Hyperlink link = new Hyperlink("-");
                link.setOnAction(e -> {
                    T selected = tabella.getSelectionModel().getSelectedItem();
                    if (selected != null) linkActions.get(etichetta).accept(selected);
                });
                destra.getChildren().add(new HBox(new Label(etichetta + ": "), link));
                campiDettaglioNode.put(etichetta, link);
            } else {
                Label lbl = new Label(etichetta + ": -");
                destra.getChildren().add(lbl);
                campiDettaglioNode.put(etichetta, lbl);
            }
        });

        tabella.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nuovoSel) -> {
            if (nuovoSel != null) {
                campiDettagli.forEach((etichetta, extractor) -> {
                    Node node = campiDettaglioNode.get(etichetta);
                    String value = extractor.apply(nuovoSel);
                    if (node instanceof Label lbl) {
                        lbl.setText(etichetta + ": " + value);
                    } else if (node instanceof Hyperlink link) {
                        link.setText(value);
                    }
                });
            } else {
                campiDettaglioNode.forEach((k, node) -> {
                    if (node instanceof Label lbl) lbl.setText(k + ": -");
                    else if (node instanceof Hyperlink link) link.setText("-");
                });
            }
        });

        contenitore.getChildren().addAll(sinistra, destra);
        VBox.setVgrow(contenitore, Priority.ALWAYS);
        layout.getChildren().add(contenitore);

        // === Filtro ===
        filtroField.textProperty().addListener((obs, old, nuovo) -> {
            if (nuovo == null || nuovo.isBlank()) {
                tabella.setItems(items);
            } else {
                ObservableList<T> filtrati = items.filtered(item ->
                        colonneTabella.values().stream()
                                .map(func -> func.apply(item).toLowerCase())
                                .anyMatch(val -> val.contains(nuovo.toLowerCase()))
                );
                tabella.setItems(filtrati);
            }
        });

        this.azioneAggiungi = onAggiungi;
        this.azioneModifica = onModifica;
        this.azioneElimina = onElimina;

        return layout;
    }

    public void refresh(List<T> nuovaLista) {
        items.setAll(nuovaLista);
    }

    public TableView<T> getTabella() {
        return tabella;
    }

    public Runnable getAggiungiAction() {
        return azioneAggiungi;
    }

    public Consumer<T> getModificaAction() {
        return azioneModifica;
    }

    public Consumer<T> getEliminaAction() {
        return azioneElimina;
    }


}
