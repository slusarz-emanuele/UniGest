package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.Main;
import it.univaq.unigest.gui.Reloader;
import it.univaq.unigest.gui.util.CrudPanel;
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
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StartView {

    private boolean menuVisibile = true;
    private Button bottoneAttivo = null;
    private AbstractModelView vistaCorrente;
    private Stage stagePrimario;

    public void start(Stage stagePrimario) {

        this.stagePrimario = stagePrimario;

        BorderPane root = new BorderPane();

        // --- SINISTRA (menu)
        VBox menu = new VBox(10);
        menu.setPadding(new Insets(15));
        menu.setStyle("-fx-background-color: #34495E;");
        menu.setPrefWidth(200);

        Button dashboardBtn     = new Button("Home - Dashboard");
        Button studentiBtn      = new Button("Studenti");
        Button docentiBtn       = new Button("Docenti");
        Button corsoDiLaureaBtn = new Button("Corsi di Laurea");
        Button insegnamentiBtn  = new Button("Insegnamenti");
        Button appelliBtn       = new Button("Appelli");
        Button iscrizioniBtn    = new Button("Iscrizioni");
        Button esamiBtn         = new Button("Esami");
        Button verbaliBtn       = new Button("Verbali");
        Button auleliBtn        = new Button("Aule");
        Button edificiBtn       = new Button("Edifici");

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

        // --- TOP BAR
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

        Label titolo = new Label("UniGest");
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnExport   = new Button("Backup");
        Button btnSettings = new Button("Impostazioni");
        btnExport.getStyleClass().add("top-bar-button");
        btnSettings.getStyleClass().add("top-bar-button");

        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logo.png")));
        ImageView logoView = new ImageView(logo); logoView.setFitHeight(32); logoView.setFitWidth(32); logoView.setPreserveRatio(true);

        topBar.getChildren().addAll(toggleMenu, spacer, btnExport, btnSettings, titolo, logoView);
        root.setTop(topBar);

        // ---- Bind navigazione (titoli hardcoded)
        bindNav(
                docentiBtn,
                () -> new DocentiView(Main.getDocenteService()),
                "Docenti",
                Reloader::registerDocentiPannello,
                root
        );

        bindNav(
                studentiBtn,
                () -> new StudentiModelView(
                        Main.getStudenteService(),
                        () -> Main.getCorsoDiLaureaManager().getAll(),             // Supplier<List<CorsoDiLaurea>>
                        id -> {                                                    // Function<String,String> (nome by id)
                            //var c = Main.getCorsoDiLaureaManager().getById(id);
                            //return c != null ? c.getNome() : "";
                            return null;
                        }
                ),
                "Studenti",
                Reloader::registerStudentiPannello, // se lo usi; altrimenti passa null o usa l'overload senza registrar
                root
        );


        // TODO: quando migrerai gli altri, aggiungi i bindNav analoghi.
        // Per ora puoi lasciare i pulsanti senza azione o con la vecchia logica.

        // --- Scena
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
        this.stagePrimario.setScene(scena);
        this.stagePrimario.setTitle("UniGest — Dashboard");
        this.stagePrimario.show();
    }

    private <P extends CrudPanel, V extends AbstractModelView<P>> void bindNav(
            Button btn,
            Supplier<V> viewFactory,
            String titolo,                  // <-- ora stringa diretta
            Consumer<P> registrar,
            BorderPane root
    ) {
        btn.setOnAction(e -> {
            handleButtonClick(btn);

            V view = viewFactory.get();
            if (registrar != null) registrar.accept(view.getPannello());

            this.stagePrimario.setTitle("UniGest — " + titolo);

            vistaCorrente = view;
            root.setCenter(view.getView());
        });
    }

    // overload comodo se non devi registrare nulla
    private <P extends CrudPanel, V extends AbstractModelView<P>> void bindNav(
            Button btn,
            Supplier<V> viewFactory,
            String titolo,
            BorderPane root
    ) {
        bindNav(btn, viewFactory, titolo, null, root);
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
