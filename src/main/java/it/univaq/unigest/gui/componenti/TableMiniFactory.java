package it.univaq.unigest.gui.componenti;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class TableMiniFactory {

    private TableMiniFactory() {}

    // Base
    public static <T> TableView<T> creaTabella(
            Supplier<List<T>> loader,
            SelectionMode mode,
            int prefHeight,
            LinkedHashMap<String, Function<T, String>> colonne
    ) {
        TableView<T> table = new TableView<>();
        table.setPrefHeight(prefHeight > 0 ? prefHeight : 220);
        table.getSelectionModel().setSelectionMode(mode != null ? mode : SelectionMode.SINGLE);

        // Colonne
        colonne.forEach((title, extractor) -> {
            TableColumn<T, String> col = new TableColumn<>(title);
            col.setCellValueFactory(data -> new SimpleStringProperty(
                    extractor.apply(data.getValue())
            ));
            table.getColumns().add(col);
        });

        // Dati
        List<T> data = loader != null ? loader.get() : List.of();
        table.setItems(FXCollections.observableArrayList(data));
        return table;
    }

    // Con preselezione per id (String)
    public static <T> TableView<T> creaTabella(
            Supplier<List<T>> loader,
            SelectionMode mode,
            int prefHeight,
            LinkedHashMap<String, Function<T, String>> colonne,
            Function<T, String> idExtractor,
            String preselectId
    ) {
        TableView<T> table = creaTabella(loader, mode, prefHeight, colonne);
        if (idExtractor != null && preselectId != null) {
            table.getItems().stream()
                    .filter(item -> preselectId.equals(idExtractor.apply(item)))
                    .findFirst()
                    .ifPresent(item -> table.getSelectionModel().select(item));
        }
        return table;
    }

    // Variante con predicate di preselezione
    public static <T> TableView<T> creaTabella(
            Supplier<List<T>> loader,
            SelectionMode mode,
            int prefHeight,
            LinkedHashMap<String, Function<T, String>> colonne,
            Predicate<T> preselectPredicate
    ) {
        TableView<T> table = creaTabella(loader, mode, prefHeight, colonne);
        if (preselectPredicate != null) {
            table.getItems().stream()
                    .filter(preselectPredicate)
                    .findFirst()
                    .ifPresent(item -> table.getSelectionModel().select(item));
        }
        return table;
    }
}
