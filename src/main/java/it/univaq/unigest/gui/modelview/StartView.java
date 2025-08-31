package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.util.CrudView;
import it.univaq.unigest.util.ParametrizzazioneHelper;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class StartView {

    private boolean menuVisibile = true;
    private Button bottoneAttivo = null;
    private AbstractModelView vistaCorrente;

    public void start(Stage stagePrimario) {

        // Layout main
        BorderPane root = new BorderPane();

        // SINISTRA
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #34495E;");
        menu.setPrefWidth(200);

        Button dashboardBtn = new Button("Home - Dashboard");
        Button studentiBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.studenti"));
        Button docentiBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.docenti"));
        Button corsoDiLaureaBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.corsiDiLaurea"));
        Button insegnamentiBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.insegnamenti"));
        Button appelliBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.appelli"));
        Button iscrizioniBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.iscrizioni"));
        Button esamiBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.esami"));
        Button verbaliBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.verbali"));
        Button auleliBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.aule"));
        Button edificiBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.edifici"));

        // Aggiungi classi CSS ai pulsanti menu
        dashboardBtn.getStyleClass().add("menu-button");
        studentiBtn.getStyleClass().add("menu-button");
        docentiBtn.getStyleClass().add("menu-button");
        corsoDiLaureaBtn.getStyleClass().add("menu-button");
        insegnamentiBtn.getStyleClass().add("menu-button");
        appelliBtn.getStyleClass().add("menu-button");
        iscrizioniBtn.getStyleClass().add("menu-button");
        esamiBtn.getStyleClass().add("menu-button");
        verbaliBtn.getStyleClass().add("menu-button");
        auleliBtn.getStyleClass().add("menu-button");
        edificiBtn.getStyleClass().add("menu-button");

        menu.getStyleClass().add("menu-container");

        menu.getChildren().addAll(
                dashboardBtn,
                creaSeparatoreMenu(),
                studentiBtn,
                docentiBtn,
                creaSeparatoreMenu(),
                corsoDiLaureaBtn,
                insegnamentiBtn,
                appelliBtn,
                iscrizioniBtn,
                esamiBtn,
                verbaliBtn,
                creaSeparatoreMenu(),
                auleliBtn,
                edificiBtn
        );

        root.setLeft(menu);

        // SOPRA
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(10);

        Button toggleMenu = new Button("☰");
        toggleMenu.getStyleClass().add("menu-button");

        toggleMenu.setOnAction(e -> {
            if (menuVisibile) {
                // Nasconde la sidebar con animazione verso sinistra
                TranslateTransition hideMenu = new TranslateTransition(Duration.millis(300), menu);
                hideMenu.setToX(-menu.getWidth());
                hideMenu.setOnFinished(event -> {
                    menu.setVisible(false);
                    menu.setManaged(false);
                });
                hideMenu.play();
            } else {
                // Mostra la sidebar con animazione verso destra
                menu.setVisible(true);
                menu.setManaged(true);
                TranslateTransition showMenu = new TranslateTransition(Duration.millis(300), menu);
                showMenu.setFromX(-menu.getWidth());
                showMenu.setToX(0);
                showMenu.play();
            }
            menuVisibile = !menuVisibile;
        });

        Label titolo = new Label(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale"));
        titolo.getStyleClass().add("top-bar-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnExport = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.backup"));
        btnExport.setTooltip(new Tooltip(Main.getParametrizzazioneHelper().getBundle().getString("tooltip.backup")));
        btnExport.setOnAction(e -> {
            //TODO: fare il pulante di backup
        });

        Button btnSettings = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.impostazioni"));
        btnSettings.setTooltip(new Tooltip(Main.getParametrizzazioneHelper().getBundle().getString("tooltip.impostazioni")));
        btnSettings.setOnAction(e -> {
            //TODO: fare il pulante delle impostazioni
        });

        btnExport.getStyleClass().add("top-bar-button");
        btnSettings.getStyleClass().add("top-bar-button");

        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(32);
        logoView.setFitWidth(32);
        logoView.setPreserveRatio(true);

        // Aggiunta in ordine
        topBar.getChildren().addAll(toggleMenu, spacer, btnExport, btnSettings, titolo, logoView);
        root.setTop(topBar);

        //TODO: lo sta per fare benedetta
//        // Parte principle
//        DashBoardView dashBoardView = new DashBoardView();
//        root.setCenter(dashBoardView.getView());
//
//        // Pulsante Dashboard
//        dashboardBtn.setOnAction(e -> {
//            handleButtonClick(dashboardBtn);
//            root.setCenter(dashBoardView.getView());
//        });

        // Pulsante Studenti
        studentiBtn.setOnAction(e -> {
            handleButtonClick(studentiBtn);
            StudentiModelView studentiView = new StudentiModelView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.studenti")
            );
            vistaCorrente = studentiView;
            root.setCenter(studentiView.getView());
        });

        // Pulsante Docenti
        docentiBtn.setOnAction(e -> {
            handleButtonClick(docentiBtn);
            DocentiView docentiView = new DocentiView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.docenti")
            );
            vistaCorrente = docentiView;
            root.setCenter(docentiView.getView());
        });

        // Pulsante CDL
        corsoDiLaureaBtn.setOnAction(e -> {
            handleButtonClick(corsoDiLaureaBtn);
            CorsoDiLaureaView corsoDiLaureaView = new CorsoDiLaureaView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.corsiDiLaurea")
            );
            vistaCorrente = corsoDiLaureaView;
            root.setCenter(corsoDiLaureaView.getView());
        });

        // Pulsante Insegnamenti
        insegnamentiBtn.setOnAction(e -> {
            handleButtonClick(insegnamentiBtn);
            InsegnamentiView insegnamentiView = new InsegnamentiView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.insegnamenti")
            );
            vistaCorrente = insegnamentiView;
            root.setCenter(insegnamentiView.getView());
        });

        // Pulsante Appelli
        appelliBtn.setOnAction(e -> {
            handleButtonClick(appelliBtn);
            AppelliModelView appelliView = new AppelliModelView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.appelli")
            );
            vistaCorrente = appelliView;
            root.setCenter(appelliView.getView());
        });

        // Pulsante Iscrizioni
        iscrizioniBtn.setOnAction(e -> {
            handleButtonClick(iscrizioniBtn);
            IscrizioniView iscrizioniView = new IscrizioniView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.iscrizioni")
            );
            vistaCorrente = iscrizioniView;
            root.setCenter(iscrizioniView.getView());
        });

        // Pulsante Esami
        esamiBtn.setOnAction(e -> {
            handleButtonClick(esamiBtn);
            EsamiView esamiView = new EsamiView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.esami")
            );
            vistaCorrente = esamiView;
            root.setCenter(esamiView.getView());
        });

        // Pulsante Verbali
        verbaliBtn.setOnAction(e -> {
            handleButtonClick(verbaliBtn);
            VerbaliView verbaliView = new VerbaliView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.verbali")
            );
            vistaCorrente = verbaliView;
            root.setCenter(verbaliView.getView());
        });

        // Pulsante Aule
        auleliBtn.setOnAction(e -> {
            handleButtonClick(auleliBtn);
            AuleModelView auleView = new AuleModelView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.aule")
            );
            vistaCorrente = auleView;
            root.setCenter(auleView.getView());
        });

        // Pulsante Edifici
        edificiBtn.setOnAction(e -> {
            handleButtonClick(edificiBtn);
            EdificioView edificioView = new EdificioView();
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.edifici")
            );
            vistaCorrente = edificioView;
            root.setCenter(edificioView.getView());
        });

        // Scena
        Scene scena = new Scene(root, 1100, 900);
        scena.setOnKeyPressed(event -> {
            if (vistaCorrente instanceof CrudView crud) {
                switch (event.getCode()) {
                    case N -> {
                        // CTRL+N che aggiunge
                        if (event.isControlDown()) crud.onAdd();
                    }
                    case S -> {
                        // CTRL+S per salvare
                        if (event.isControlDown()) crud.onSave();
                    }
                    case DELETE -> crud.onDelete();
                    case E -> {
                        // CTRL+E per modificare
                        if (event.isControlDown()) crud.onEdit();
                    }
                    case W -> {
                        System.exit(0);
                    }
                }
            }
        });

        scena.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/startview.css")).toExternalForm());
        stagePrimario.setScene(scena);
        stagePrimario.setTitle("UniGest — Dashboard"); // TODO: farlo per le altre finestre
        stagePrimario.show();
    }

    // TODO: rimuoverlo per tutti i restanti
    private Separator creaSeparatoreMenu() {
        Separator separatore = new Separator();
        separatore.setPrefWidth(180); // leggermente meno della VBox
        separatore.setStyle("""
                -fx-background-color: transparent;
                -fx-border-style: solid;
                -fx-border-color: rgba(255,255,255,0.2);
                -fx-border-insets: 0;
                -fx-border-width: 0 0 1 0; /* solo bordo inferiore */
                -fx-padding: 8 0 8 0;
                """);
        return separatore;
    }

    private void handleButtonClick(Button clickedBtn) {
        if (bottoneAttivo != null) {
            bottoneAttivo.getStyleClass().remove("menu-button-active");
        }
        clickedBtn.getStyleClass().add("menu-button-active");
        bottoneAttivo = clickedBtn;
    }
}
