package it.univaq.unigest.gui.navigation;

import it.univaq.unigest.gui.modelview.AbstractModelView;
import it.univaq.unigest.gui.util.CrudPanel;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Router centralizzato dell'interfaccia.
 *
 * <p>Responsabilità principali:
 * <ul>
 *   <li>Inizializzazione con lo {@link Stage} primario e il {@link BorderPane} root dell’app;</li>
 *   <li>Caricamento di viste strutturate ({@link AbstractModelView}) tramite {@link #show(String, Supplier, Consumer)};</li>
 *   <li>Caricamento di pannelli “puri” JavaFX (qualunque {@link Node}) tramite {@link #showNode(String, Node)};</li>
 *   <li>Mantenimento della vista corrente per scorciatoie/azioni contestuali ({@link #getCurrentView()}).</li>
 * </ul>
 *
 * <p>Pattern d’uso tipico (in {@code StartView}):
 * <pre>{@code
 * // bootstrap
 * ViewDispatcher.init(primaryStage, rootBorderPane);
 *
 * // routing verso una AbstractModelView + registrazione pannello per il Reloader
 * ViewDispatcher.get().show(
 *     "Docenti",
 *     () -> new DocentiView(docenteService, domainQueryService),
 *     Reloader::registerDocentiPannello
 * );
 *
 * // routing verso un pannello "puro" (es. Dashboard costruita a mano)
 * ViewDispatcher.get().showNode("Dashboard", new DashboardScreen(...).build());
 * }</pre>
 */
public final class ViewDispatcher {

    private static ViewDispatcher INSTANCE;

    private final Stage stage;
    private final BorderPane root;
    private AbstractModelView<?> currentView;

    /**
     * Costruttore privato: usare {@link #init(Stage, BorderPane)}.
     */
    private ViewDispatcher(Stage stage, BorderPane root) {
        this.stage = stage;
        this.root = root;
    }

    /**
     * Inizializza il dispatcher con lo stage primario e il contenitore root.
     *
     * @param stage lo {@link Stage} principale dell’applicazione
     * @param root  il {@link BorderPane} principale in cui montare i contenuti
     */
    public static void init(Stage stage, BorderPane root) {
        INSTANCE = new ViewDispatcher(stage, root);
    }

    /**
     * Restituisce l’istanza inizializzata del dispatcher.
     *
     * @return l’istanza singleton
     * @throws IllegalStateException se {@link #init(Stage, BorderPane)} non è stata chiamata
     */
    public static ViewDispatcher get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ViewDispatcher non inizializzato: chiama ViewDispatcher.init(stage, root) prima dell'uso.");
        }
        return INSTANCE;
    }

    /**
     * Mostra una vista strutturata (derivata da {@link AbstractModelView}) nel centro del layout,
     * impostando il titolo della finestra e registrando opzionalmente il pannello CRUD per il {@code Reloader}.
     *
     * @param titolo       testo mostrato nel titolo della finestra (verrà prefissato con “UniGest — ”)
     * @param viewFactory  factory che crea la vista da mostrare
     * @param registrar    (opzionale) consumer che riceve il pannello CRUD interno per registrazioni globali
     * @param <P>          tipo del pannello CRUD interno (implementa {@link CrudPanel})
     * @param <V>          tipo della vista ({@link AbstractModelView})
     */
    public <P extends CrudPanel, V extends AbstractModelView<P>> void show(String titolo,
                                                                           Supplier<V> viewFactory,
                                                                           Consumer<P> registrar) {
        V view = viewFactory.get();
        if (registrar != null) registrar.accept(view.getPannello());

        stage.setTitle("UniGest — " + titolo);
        root.setCenter(view.getView());
        currentView = view;
    }

    /**
     * Mostra un {@link Node} arbitrario nel centro del layout (es. Dashboard),
     * impostando il titolo della finestra. In questo caso {@code currentView} viene azzerato,
     * quindi eventuali scorciatoie CRUD globali risultano disabilitate.
     *
     * @param titolo  testo mostrato nel titolo della finestra (verrà prefissato con “UniGest — ”)
     * @param content nodo da visualizzare
     */
    public void showNode(String titolo, Node content) {
        stage.setTitle("UniGest — " + titolo);
        root.setCenter(content);
        currentView = null; // non è un AbstractModelView ma è SEMPLICEMENTE un pannello PURO, in fatti mi servve per Dashboardviw!!!!
    }

    /**
     * Restituisce la vista corrente se è una {@link AbstractModelView},
     * altrimenti {@code null} (es. quando è stato caricato un pannello "puro" con {@link #showNode}).
     *
     * @return la vista corrente o {@code null}
     */
    public AbstractModelView<?> getCurrentView() {
        return currentView;
    }
}
