package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.navigation.ViewDispatcher;
import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.service.AppelloService;
import it.univaq.unigest.service.AulaService;
import it.univaq.unigest.service.CorsoDiLaureaService;
import it.univaq.unigest.service.DocenteService;
import it.univaq.unigest.service.InsegnamentoService;
import it.univaq.unigest.service.StudenteService;
import it.univaq.unigest.service.VerbaleService;
import it.univaq.unigest.service.SettingsService;
import it.univaq.unigest.service.MaintenanceService;
import it.univaq.unigest.service.query.DomainQueryService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Schermata principale dell'applicazione UniGest.
 * Mostra:
 * <ul>
 *     <li>Header con titolo, logo e orologio in tempo reale</li>
 *     <li>Riepilogo del numero di studenti, docenti, insegnamenti e appelli odierni</li>
 *     <li>Tabella dei prossimi appelli</li>
 *     <li>Pannello per navigare tra sezioni principali</li>
 * </ul>
 */
public class DashboardScreen {

    //Servizi
    private final StudenteService studenteService;
    private final DocenteService docenteService;
    private final InsegnamentoService insegnamentoService;
    private final AppelloService appelloService;
    private final AulaService aulaService;
    private final CorsoDiLaureaService corsoDiLaureaService;
    private final VerbaleService verbaleService;
    private final SettingsService settingsService;
    private final MaintenanceService maintenanceService;
    private final DomainQueryService domainQueryService;

    public DashboardScreen(StudenteService studenteService,
                           DocenteService docenteService,
                           InsegnamentoService insegnamentoService,
                           AppelloService appelloService,
                           AulaService aulaService,
                           CorsoDiLaureaService corsoDiLaureaService,
                           VerbaleService verbaleService,
                           SettingsService settingsService,
                           MaintenanceService maintenanceService,
                           DomainQueryService domainQueryService) {
        this.studenteService = studenteService;
        this.docenteService = docenteService;
        this.insegnamentoService = insegnamentoService;
        this.appelloService = appelloService;
        this.aulaService = aulaService;
        this.corsoDiLaureaService = corsoDiLaureaService;
        this.verbaleService = verbaleService;
        this.settingsService = settingsService;
        this.maintenanceService = maintenanceService;
        this.domainQueryService = domainQueryService;
    }

    private final DateTimeFormatter clockFmt = DateTimeFormatter.ofPattern("EEE d MMM yyyy • HH:mm:ss");

    /**
     * Costruisce la vista della dashboard,
     * comprendente di tutti i campi necessari.
     *
     * @return {@link Node} contenente l'intera dashboard
     */
    public Node build() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(12));

        // ===== Header (titolo + logo a destra) =====
        VBox headerBox = new VBox(6);

        HBox headerLine = new HBox(10);
        Label title = new Label("Benvenuto in UniGest");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView logoView = null;
        try {
            Image img = new Image(getClass().getResourceAsStream("/icons/univaq.png"));
            logoView = new ImageView(img);
            logoView.setFitHeight(80);
            logoView.setPreserveRatio(true);
        } catch (Exception ignored) {}

        headerLine.getChildren().addAll(title, spacer);
        if (logoView != null) headerLine.getChildren().add(logoView);

        Label clockLabel = new Label();
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockLabel.setText(LocalDateTime.now().format(clockFmt))),
                new KeyFrame(Duration.seconds(1))
        );
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        headerBox.getChildren().addAll(headerLine, clockLabel);

        // ===== Riepilogo semplice =====
        HBox summary = new HBox(16);
        summary.setAlignment(Pos.CENTER_LEFT);

        summary.getChildren().addAll(
                smallStat("Studenti", String.valueOf(studenteService.findAll().size())),
                smallStat("Docenti", String.valueOf(docenteService.findAll().size())),
                smallStat("Insegnamenti", String.valueOf(insegnamentoService.findAll().size())),
                smallStat("Appelli oggi", String.valueOf(
                        appelloService.findAll().stream()
                                .filter(a -> LocalDate.now().equals(a.getData()))
                                .count()
                ))
        );

        // ===== Contenuto in due colonne =====
        HBox content = new HBox(12);

        // sinistra: Prossimi Appelli (tabella)
        VBox left = new VBox(8);
        Label l1 = new Label("Prossimi Appelli");
        l1.setStyle("-fx-font-weight: bold;");
        Node tableAppelli = buildUpcomingAppelliTable();
        left.getChildren().addAll(l1, tableAppelli);
        VBox.setVgrow(tableAppelli, Priority.ALWAYS);

        // destra: Azioni rapide
        VBox right = new VBox(8);
        Label l2 = new Label("Azioni rapide");
        l2.setStyle("-fx-font-weight: bold;");
        right.getChildren().addAll(l2, quickActions());

        HBox.setHgrow(left, Priority.ALWAYS);

        content.getChildren().addAll(left, right);

        root.getChildren().addAll(headerBox, new Separator(), summary, new Separator(), content);
        VBox.setVgrow(content, Priority.ALWAYS);
        return root;
    }

    // --- Helpers UI semplici ---

     /**
     * Costruisce un campo(label + valore) per il riepilogo.
     *
     * @param label etichetta del campo
     * @param value valore del compo
     * @return {@link Node} contenente il campo
     */
    private Node smallStat(String label, String value) {
        VBox box = new VBox(2);
        Label l = new Label(label + ":");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        box.getChildren().addAll(l, v);
        return box;
    }

    /**
     * Costruisce la tabella dei prossimi appelli.
     *
     * @return {@link Node} contenente la tabella dei prossimi appelli
     */
    private Node buildUpcomingAppelliTable() {
        TableView<Appello> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Appello, String> cData = new TableColumn<>("Data");
        cData.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getData() != null ? d.getValue().getData().toString() : ""
        ));

        TableColumn<Appello, String> cOra = new TableColumn<>("Ora");
        cOra.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getOra() != null ? d.getValue().getOra().toString() : ""
        ));

        TableColumn<Appello, String> cIns = new TableColumn<>("Insegnamento");
        cIns.setCellValueFactory(d -> new SimpleStringProperty(resolveInsegnamentoNome(d.getValue().getRidInsegnamento())));

        TableColumn<Appello, String> cAula = new TableColumn<>("Aula");
        cAula.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getRidAula() != null ? d.getValue().getRidAula() : ""
        ));

        table.getColumns().addAll(cData, cOra, cIns, cAula);
        
        var upcoming = appelloService.findAll().stream()
                .filter(a -> a.getData() != null && !a.getData().isBefore(LocalDate.now()))
                .sorted(Comparator
                        .comparing(Appello::getData, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Appello::getOra, Comparator.nullsLast(Comparator.naturalOrder())))
                .limit(8)
                .collect(Collectors.toList());

        table.getItems().setAll(upcoming);
        return table;
    }

    /**
     * Si occupa di risolvere la corrispondenza tra l'ID di un 
     * insegnamento ed il suo nome
     *
     * @param id ID dell'insegnamento
     * @return nome dell'insegnamento se presente, altrimenti l'ID
     */
    private String resolveInsegnamentoNome(String id) {
        if (id == null) return "";
        Optional<Insegnamento> opt = insegnamentoService.findById(id);
        return opt.map(Insegnamento::getNome).orElse(id);
    }

    /**
     * Costruisce il pannello con i pulsanti per le azioni rapide.
     *
     * @return {@link Node} contenente le azioni rapide
     */
    private Node quickActions() {
        VBox box = new VBox(6);

        Button bStudenti = new Button("Apri Studenti");
        bStudenti.setOnAction(e ->
                ViewDispatcher.get().show("Studenti",
                        () -> new StudentiModelView(
                                studenteService,
                                () -> corsoDiLaureaService.findAll(),
                                domainQueryService
                        ),
                        null
                )
        );

        Button bAppelli = new Button("Apri Appelli");
        bAppelli.setOnAction(e ->
                ViewDispatcher.get().show("Appelli",
                        () -> new AppelliModelView(
                                appelloService,
                                () -> insegnamentoService.findAll(),
                                () -> aulaService.findAll(),
                                () -> docenteService.findAll(),
                                domainQueryService
                        ),
                        null
                )
        );

        Button bVerbali = new Button("Vai a Verbali");
        bVerbali.setOnAction(e ->
                ViewDispatcher.get().show("Verbali",
                        () -> new VerbaliView(
                                verbaleService,
                                () -> appelloService.findAll(),
                                domainQueryService
                        ),
                        null
                )
        );

        Button bImpostazioni = new Button("Impostazioni");
        bImpostazioni.setOnAction(e ->
                it.univaq.unigest.gui.impostazioni.SettingsWindow.open(
                        settingsService,
                        maintenanceService
                )
        );

        box.getChildren().addAll(bStudenti, bAppelli, bVerbali, bImpostazioni);
        return box;
    }
}