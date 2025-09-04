package it.univaq.unigest.gui.componenti;

import it.univaq.unigest.util.PdfHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class RelatedListStage<T> {

    private final String title;
    private final Supplier<List<T>> loader;
    private final LinkedHashMap<String, Function<T, String>> columns;
    private final String exportFileName;

    public RelatedListStage(String title,
                            Supplier<List<T>> loader,
                            LinkedHashMap<String, Function<T, String>> columns,
                            String exportFileName) {
        this.title = title;
        this.loader = loader;
        this.columns = columns;
        this.exportFileName = exportFileName;
    }

    public void show() {
        Stage stage = new Stage();

        // Top: titolo + filtro
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        TextField filtro = new TextField();
        filtro.setPromptText("Filtra...");

        HBox top = new HBox(10, lblTitle, filtro);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10));

        // Tabella
        TableView<T> table = new TableView<>();
        columns.forEach((header, extractor) -> {
            TableColumn<T, String> col = new TableColumn<>(header);
            col.setCellValueFactory(cd -> new SimpleStringProperty(extractor.apply(cd.getValue())));
            table.getColumns().add(col);
        });

        ObservableList<T> all = FXCollections.observableArrayList(loader.get());
        table.setItems(all);

        filtro.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || newV.isBlank()) {
                table.setItems(all);
            } else {
                String needle = newV.toLowerCase();
                table.setItems(all.filtered(item ->
                        columns.values().stream()
                                .map(f -> f.apply(item).toLowerCase())
                                .anyMatch(s -> s.contains(needle))
                ));
            }
        });

        // Bottom buttons
        Button btnRefresh = new Button("Aggiorna");
        Button btnExport  = new Button("Esporta in PDF");
        Button btnClose   = new Button("Chiudi");

        btnRefresh.setOnAction(e -> {
            List<T> fresh = loader.get();
            all.setAll(fresh);
            table.setItems(all);
        });

        btnExport.setOnAction(e ->
                PdfHelper.esportaTabellaInPdf(table, title, exportFileName)
        );

        btnClose.setOnAction(e -> stage.close());

        HBox bottom = new HBox(10, btnRefresh, btnExport, btnClose);
        bottom.setAlignment(Pos.CENTER_RIGHT);
        bottom.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(new VBox(table));
        root.setBottom(bottom);
        BorderPane.setMargin(root.getCenter(), new Insets(10));

        stage.setScene(new Scene(root, 900, 500));
        stage.setTitle(title);
        stage.show();
    }

    // comoda factory statica
    public static <T> void open(String title,
                                Supplier<List<T>> loader,
                                LinkedHashMap<String, Function<T, String>> columns,
                                String exportFileName) {
        new RelatedListStage<>(title, loader, columns, exportFileName).show();
    }
}