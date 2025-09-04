package it.univaq.unigest.gui.navigation;

import it.univaq.unigest.gui.modelview.AbstractModelView;
import it.univaq.unigest.gui.util.CrudPanel;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ViewDispatcher {

    private static ViewDispatcher INSTANCE;

    private final Stage stage;
    private final BorderPane root;
    private AbstractModelView<?> currentView;

    private ViewDispatcher(Stage stage, BorderPane root) {
        this.stage = stage;
        this.root = root;
    }

    public static void init(Stage stage, BorderPane root) {
        INSTANCE = new ViewDispatcher(stage, root);
    }

    public static ViewDispatcher get() {
        if (INSTANCE == null) {
            throw new IllegalStateException("ViewDispatcher non inizializzato: chiama ViewDispatcher.init(stage, root) prima dell'uso.");
        }
        return INSTANCE;
    }

    public <P extends CrudPanel, V extends AbstractModelView<P>> void show(String titolo,
                                                                           Supplier<V> viewFactory,
                                                                           Consumer<P> registrar) {
        V view = viewFactory.get();
        if (registrar != null) registrar.accept(view.getPannello());

        stage.setTitle("UniGest — " + titolo);
        root.setCenter(view.getView());
        currentView = view;
    }

    public AbstractModelView<?> getCurrentView() {
        return currentView;
    }
}