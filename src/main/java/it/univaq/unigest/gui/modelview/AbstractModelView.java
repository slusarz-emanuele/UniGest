package it.univaq.unigest.gui.modelview;

import it.univaq.unigest.gui.util.CrudPanel;
import it.univaq.unigest.gui.util.CrudView;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Base astratta per le schermate “model view” con supporto CRUD standard.
 * <p>
 * Fornisce:
 * <ul>
 *   <li>un contenitore comune con {@link TabPane} e una tab “Gestione”;</li>
 *   <li>il wiring delle azioni CRUD (add/edit/delete) verso un {@link CrudPanel} associato;</li>
 *   <li>un punto di estensione {@link #creaGestioneContenuto()} per costruire l’UI della tab.</li>
 * </ul>
 * <note>
 *     è possibile inserire qui altri pannelli per le entità.
 * </note>
 * @param <T> tipo del pannello CRUD associato alla vista
 */
public abstract class AbstractModelView<T extends CrudPanel> implements CrudView {

    /**
     * Pannello CRUD “core” a cui delegare le azioni standard
     * (dialog di inserimento, modifica, eliminazione, refresh, ecc.).
     * Deve essere valorizzato dalla sottoclasse in {@link #creaGestioneContenuto()}.
     */
    protected T panel;

    /**
     * Costruisce il contenitore della vista con una singola tab “Gestione”
     * e inserisce al suo interno il layout specifico fornito da {@link #creaGestioneContenuto()}.
     *
     * @return un {@link VBox} pronto per essere aggiunto alla scena
     */
    public VBox getView(){
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TabPane tabPane = new TabPane();
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Tab 1
        Tab tabGestione = new Tab("Gestione");
        tabGestione.setClosable(false);

        VBox layoutGestione = creaGestioneContenuto();
        tabGestione.setContent(layoutGestione);

        VBox.setVgrow(layoutGestione, Priority.ALWAYS);

        // TabPane
        tabPane.getTabs().addAll(tabGestione);
        tabPane.getSelectionModel().select(tabGestione);
        tabPane.setStyle("-fx-background-color: #ffffff;");

        // Root
        root.getChildren().add(tabPane);
        return root;
    }

    /**
     * Restituisce il pannello CRUD associato alla vista.
     * Utile per registrazioni esterne (es. Reloader) o per test.
     *
     * @return il pannello CRUD corrente
     */
    public T getPannello (){
        return this.panel;
    }

    /**
     * Inoltra l’azione di aggiunta al {@link CrudPanel}.
     * Equivalente a “Nuovo…” / “Aggiungi”.
     */
    @Override public void onAdd()    {
        panel.apriDialogAggiungiPubblico();
    }

    /**
     * Inoltra l’azione di modifica al {@link CrudPanel}.
     * Opera sull’elemento attualmente selezionato nella tabella/pannello.
     */
    @Override public void onEdit()   {
        panel.modificaSelezionato();
    }

    /**
     * Inoltra l’azione di eliminazione al {@link CrudPanel}.
     * Opera sull’elemento attualmente selezionato nella tabella/pannello.
     */
    @Override public void onDelete() {
        panel.eliminaSelezionato();
    }

    /**
     * Punto di estensione principale: la sottoclasse costruisce qui il layout
     * specifico della tab “Gestione”, istanzia e assegna {@link #panel},
     * e ritorna il contenitore da montare nella tab.
     *
     * @return il layout (VBox) da visualizzare nella tab “Gestione”
     */
    protected abstract VBox creaGestioneContenuto();

}
