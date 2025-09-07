package it.univaq.unigest.gui.componenti;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Builder generico per creare e mostrare pannelli JavaFX con campi personalizzati.
 * <p>
 * Alla conferma viene generato un oggetto di tipo {@code T} tramite
 * la funzione {@link #generatore}, che può essere poi elaborato tramite
 * il consumer {@link #onSuccess}.
 * </p>
 *
 * @param <T> il tipo di oggetto generato dai valori dei campi
 */
public class DialogBuilder<T> {
    
    private final LinkedHashMap<String, Control> campi = new LinkedHashMap<>();
    private final String titolo;
    private final String header;
    private final Function<Map<String, Control>, T> generatore;
    private final Consumer<T> onSuccess;

    /**
     * Costruisce un nuovo {@code DialogBuilder}.
     *
     * @param titolo titolo del dialogo
     * @param header intestazione del dialogo
     * @param generatore funzione che produce l’oggetto {@code T} dai campi
     * @param onSuccess consumer da eseguire con l’oggetto generato al click su OK
     */
    public DialogBuilder(String titolo, String header, Function<Map<String, Control>, T> generatore, Consumer<T> onSuccess){
        this.titolo = titolo;
        this.header = header;
        this.generatore = generatore;
        this.onSuccess = onSuccess;
    }

    /**
     * Aggiunge un campo al dialogo con la relativa etichetta.
     *
     * @param label etichetta da mostrare per il campo
     * @param campo controllo JavaFX da inserire (TextField, ComboBox, ecc.)
     */
    public void aggiungiCampo(String label, Control campo){
        campi.put(label, campo);
    }

    /**
     * Mostra il dialogo.
     * <p>
     * Una volta cliccato su OK:
     * <ul>
     *     <li>Viene invocata la funzione {@link #generatore} per creare l’oggetto {@code T}</li>
     *     <li>Se la generazione ha risocntro positivo, viene chiamato {@link #onSuccess}</li>
     *     <li>Se la generazione lancia un’eccezione, viene mostrato un alert di errore</li>
     * </ul>
     */
    public void mostra() {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(titolo);
        dialog.setHeaderText(header);

        // Layout principale con tutti i campi
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        for (Map.Entry<String, Control> entry : campi.entrySet()){
            content.getChildren().addAll(new Label(entry.getKey()), entry.getValue());
        }

        // ScrollPane per rendere scrollabile il contenuto
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefViewportHeight(400);  // Limite altezza dinamico

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                try {
                    return generatore.apply(campi);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setHeaderText("Dati non validi");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    System.out.println(e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(onSuccess);
    }

}
