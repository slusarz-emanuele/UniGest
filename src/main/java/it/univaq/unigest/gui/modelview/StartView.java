package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.Reloader;
import it.univaq.unigest.gui.util.CrudView;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

// >>> se il tuo DocentiView ora accetta un DocenteService nel costruttore:
import it.univaq.unigest.service.DocenteService;

public class StartView {

    private boolean menuVisibile = true;
    private Button bottoneAttivo = null;
    private AbstractModelView vistaCorrente;

    public void start(Stage stagePrimario) {

        BorderPane root = new BorderPane();

        // --- SINISTRA (menu)
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #34495E;");
        menu.setPrefWidth(200);

        Button dashboardBtn = new Button("Home - Dashboard");
        Button studentiBtn  = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.studenti"));
        Button docentiBtn   = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.docenti"));
        Button corsoDiLaureaBtn = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.corsiDiLaurea"));
        Button insegnamentiBtn  = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.insegnamenti"));
        Button appelliBtn   = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.appelli"));
        Button iscrizioniBtn= new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.iscrizioni"));
        Button esamiBtn     = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.esami"));
        Button verbaliBtn   = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.verbali"));
        Button auleliBtn    = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.aule"));
        Button edificiBtn   = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.edifici"));

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
                studentiBtn, docentiBtn,
                creaSeparatoreMenu(),
                corsoDiLaureaBtn, insegnamentiBtn, appelliBtn, iscrizioniBtn, esamiBtn, verbaliBtn,
                creaSeparatoreMenu(),
                auleliBtn, edificiBtn
        );
        root.setLeft(menu);

        // --- TOP BAR (ridotta per brevità)
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(10);

        Button toggleMenu = new Button("☰");
        toggleMenu.getStyleClass().add("menu-button");
        toggleMenu.setOnAction(e -> {
            if (menuVisibile) {
                TranslateTransition hideMenu = new TranslateTransition(Duration.millis(300), menu);
                hideMenu.setToX(-menu.getWidth());
                hideMenu.setOnFinished(ev -> { menu.setVisible(false); menu.setManaged(false); });
                hideMenu.play();
            } else {
                menu.setVisible(true); menu.setManaged(true);
                TranslateTransition showMenu = new TranslateTransition(Duration.millis(300), menu);
                showMenu.setFromX(-menu.getWidth()); showMenu.setToX(0); showMenu.play();
            }
            menuVisibile = !menuVisibile;
        });

        Label titolo = new Label(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale"));
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnExport = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.backup"));
        Button btnSettings = new Button(Main.getParametrizzazioneHelper().getBundle().getString("button.impostazioni"));
        btnExport.getStyleClass().add("top-bar-button");
        btnSettings.getStyleClass().add("top-bar-button");

        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png")));
        ImageView logoView = new ImageView(logo); logoView.setFitHeight(32); logoView.setFitWidth(32); logoView.setPreserveRatio(true);

        topBar.getChildren().addAll(toggleMenu, spacer, btnExport, btnSettings, titolo, logoView);
        root.setTop(topBar);

        // --- BOTTONI MENU (solo Docenti modificato per usare il service)
        studentiBtn.setOnAction(e -> {
            handleButtonClick(studentiBtn);
            StudentiModelView v = new StudentiModelView();
            stagePrimario.setTitle(Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                    + Main.getParametrizzazioneHelper().getBundle().getString("button.studenti"));
            vistaCorrente = v;
            root.setCenter(v.getView());
        });

        docentiBtn.setOnAction(e -> {
            handleButtonClick(docentiBtn);
            var ds = Main.getDocenteService();
            // DocentiView deve esporre il pannello oppure ritornarlo
            DocentiView docentiView = new DocentiView(ds);
            // registra il pannello in Reloader
            Reloader.registerDocentiPannello(docentiView.getPannello()); // vedi nota sotto
            stagePrimario.setTitle(
                    Main.getParametrizzazioneHelper().getBundle().getString("etichetta.titolo.principale")
                            + Main.getParametrizzazioneHelper().getBundle().getString("button.docenti")
            );
            vistaCorrente = docentiView;
            root.setCenter(docentiView.getView());
        });


        // gli altri pulsanti restano invariati (useranno i Manager finché non li migri)

        Scene scena = new Scene(root, 1100, 900);
        scena.setOnKeyPressed(event -> {
            if (vistaCorrente instanceof CrudView crud) {
                switch (event.getCode()) {
                    case N -> { if (event.isControlDown()) crud.onAdd(); }
                    case DELETE -> crud.onDelete();
                    case E -> { if (event.isControlDown()) crud.onEdit(); }
                    case W -> System.exit(0);
                    default -> {}
                }
            }
        });

        scena.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/startview.css")).toExternalForm());
        stagePrimario.setScene(scena);
        stagePrimario.setTitle("UniGest — Dashboard");
        stagePrimario.show();
    }

    private Separator creaSeparatoreMenu() {
        Separator s = new Separator();
        s.setPrefWidth(180);
        s.setStyle("""
                -fx-background-color: transparent;
                -fx-border-style: solid;
                -fx-border-color: rgba(255,255,255,0.2);
                -fx-border-width: 0 0 1 0;
                -fx-padding: 8 0 8 0;
                """);
        return s;
    }

    private void handleButtonClick(Button clickedBtn) {
        if (bottoneAttivo != null) bottoneAttivo.getStyleClass().remove("menu-button-active");
        clickedBtn.getStyleClass().add("menu-button-active");
        bottoneAttivo = clickedBtn;
    }
}
