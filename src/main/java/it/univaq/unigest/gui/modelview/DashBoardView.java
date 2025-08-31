package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.util.ParametrizzazioneHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashBoardView {

    private final Label dateTimeLabel = new Label();

    public VBox getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Top bar
        HBox topBar = createTopBar();
        Label welcomeLabel = createWelcomeLabel();

        // Colonna sinistra: riepilogo + eventi
        VBox leftColumn = new VBox(20, createSummaryBox(), createEventiBox());
        leftColumn.setPrefWidth(300);

        // Colonna destra: pulsante + grafico
        VBox rightColumn = new VBox(20, createCountButton(), createChartTitle(), createPieChart());
        rightColumn.setAlignment(Pos.TOP_CENTER);

        // Layout principale
        HBox mainContent = new HBox(50, leftColumn, rightColumn);
        mainContent.setAlignment(Pos.TOP_CENTER);

        root.getChildren().addAll(topBar, welcomeLabel, mainContent);
        return root;
    }

    // --- Top bar con data e ora ---
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(0, 10, 20, 10));

        dateTimeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        dateTimeLabel.setTextFill(Color.web("#2C3E50"));
        updateDateTime();
        startDateTimeUpdater();

        topBar.getChildren().add(dateTimeLabel);
        return topBar;
    }

    private Label createWelcomeLabel() {
        Label welcomeLabel = new Label(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.benvenuto"));
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        welcomeLabel.setTextFill(Color.web("#2C3E50"));
        return welcomeLabel;
    }

    private VBox createSummaryBox() {
        VBox summaryBox = new VBox(8);
        Label title = new Label("Riepilogo");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2C3E50"));

        Label studentiLabel = new Label("📚 Studenti registrati: " + Main.getStudenteManager().getAll().size());
        Label docentiLabel = new Label("👨‍🏫 Docenti attivi: " + Main.getDocenteManager().getAll().size());
        Label corsiLabel = new Label("🎓 Corsi di laurea: " + Main.getCorsoDiLaureaManager().getAll().size());
        Label appelliLabel = new Label("📅 Appelli imminenti: " + Main.getAppelloManager().getAll().size());
        Label esamiLabel = new Label("📝 Esami totali: " + Main.getEsameManager().getAll().size());

        studentiLabel.setFont(Font.font(16));
        docentiLabel.setFont(Font.font(16));
        corsiLabel.setFont(Font.font(16));
        appelliLabel.setFont(Font.font(16));
        esamiLabel.setFont(Font.font(16));

        summaryBox.getChildren().addAll(title, studentiLabel, docentiLabel, corsiLabel, appelliLabel, esamiLabel);
        return summaryBox;
    }

    private VBox createEventiBox() {
        VBox eventiBox = new VBox(5);
        Label title = new Label("Ultimi eventi");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2C3E50"));

        ListView<String> eventiList = new ListView<>();
        eventiList.setPrefHeight(150);
        eventiList.setItems(FXCollections.observableArrayList(
                "Studente Rossi Marco iscritto a Informatica",
                "Appello Algoritmi (03/08/2025) pubblicato",
                "Studente Verdi Giulia aggiunto al CDL Economia",
                "Verbale Esame Reti approvato",
                "Backup completato con successo"
        ));

        eventiBox.getChildren().addAll(title, eventiList);
        return eventiBox;
    }

    private Button createCountButton() {
        Button btn = new Button("Conta studenti Informatica");
        btn.setOnAction(e -> {
            int n = Main.getCorsoDiLaureaManager()
                    .getNumeroStudentiAppartenentiAlCorsoDalNome("Informatica");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Risultato");
            alert.setHeaderText(null);
            alert.setContentText("Studenti iscritti a Informatica: " + n);
            alert.showAndWait();
        });
        return btn;
    }

    private Label createChartTitle() {
        Label chartTitle = new Label("Distribuzione Studenti per Corso di Laurea");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        chartTitle.setTextFill(Color.web("#2C3E50"));
        return chartTitle;
    }

    private PieChart createPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Main.getCorsoDiLaureaManager().getAll().forEach(corso -> {
            int count = Main.getCorsoDiLaureaManager()
                    .getNumeroStudentiAppartenentiAlCorsoDalNome(corso.getNome());
            pieChartData.add(new PieChart.Data(corso.getNome(), count));
        });

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(400, 300);
        return pieChart;
    }

    private void updateDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dateTimeLabel.setText(LocalDateTime.now().format(formatter));
    }

    private void startDateTimeUpdater() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
