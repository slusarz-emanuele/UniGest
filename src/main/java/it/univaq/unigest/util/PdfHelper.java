package it.univaq.unigest.util;

import it.univaq.unigest.gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Utility per l’esportazione in PDF.
 *
 * <p>Fornisce due funzionalità:
 * <ul>
 *   <li>{@link #esportaTabellaInPdf(TableView, String, String)}: esporta i dati visibili in una {@link TableView}
 *       (intestazioni e celle) in una tabella PDF;</li>
 *   <li>{@link #esportaEntita(Object, String, String)}: esporta una singola entità serializzata (via {@code toString()})
 *       in un PDF testuale.</li>
 * </ul>
 *
 * <h3>Dipendenze</h3>
 * <p>Usa le classi {@code com.lowagie.text.*} (iText 2/OpenPDF). Assicurati di avere una dipendenza compatibile, ad es. OpenPDF:
 * <pre>{@code
 * <dependency>
 *   <groupId>com.github.librepdf</groupId>
 *   <artifactId>openpdf</artifactId>
 *   <version>1.3.39</version>
 * </dependency>
 * }</pre>
 *
 * <h3>Threading e UI</h3>
 * <ul>
 *   <li>I metodi mostrano un {@link FileChooser} e {@link Alert}: devono essere invocati sul JavaFX Application Thread.</li>
 *   <li>Per tabelle molto grandi, valuta di generare il PDF in un {@code Task} in background e mostrare gli Alert con {@code Platform.runLater}.</li>
 * </ul>
 *
 * <h3>Limitazioni</h3>
 * <ul>
 *   <li>L’estrazione delle celle usa {@code TableColumn#getCellObservableValue(item)}; se una colonna usa solo renderer custom
 *       senza value factory, il valore potrebbe risultare vuoto. In tal caso, fornisci una cellValueFactory o usa {@code getCellData}.</li>
 *   <li>Il layout PDF è essenziale: una tabella con tante colonne può “stringersi”. Per layout avanzati, valuta larghezze colonne, font embedded, pagine orizzontali, ecc.</li>
 * </ul>
 *
 * <h3>Esempi</h3>
 * <pre>{@code
 * // 1) Esporta una TableView
 * PdfHelper.esportaTabellaInPdf(tabStudenti, "Elenco Studenti", "studenti");
 *
 * // 2) Esporta una singola entità
 * PdfHelper.esportaEntita(studente, "Dettaglio Studente", "studente_"+studente.getCf());
 * }</pre>
 */
public class PdfHelper {

    /**
     * Esporta i contenuti di una {@link TableView} in un file PDF.
     *
     * <p>Flusso:
     * <ol>
     *   <li>Apre un {@link FileChooser} per chiedere dove salvare il PDF;</li>
     *   <li>Scrive un titolo e una tabella con le intestazioni delle colonne e i valori visibili per ogni riga;</li>
     *   <li>Mostra un {@link Alert} di successo o errore.</li>
     * </ol>
     *
     * @param table    tabella da esportare (le colonne devono avere una {@code cellValueFactory} che restituisca un valore)
     * @param titolo   titolo da inserire in cima al documento PDF
     * @param nomeFile nome file suggerito (senza estensione), viene preimpostato come {@code nomeFile.pdf}
     * @param <T>      tipo degli elementi contenuti nella TableView
     *
     */
    public static <T> void esportaTabellaInPdf(TableView<T> table, String titolo, String nomeFile) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Salva report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")); // solo pdf
        fileChooser.setInitialFileName(nomeFile + ".pdf");

        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
        if (file == null) return;
        
        // try e catch with resources che chiude in autmatico tutto 
        try (com.lowagie.text.Document document = new com.lowagie.text.Document()) {
            com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Titolo del docimento
            com.lowagie.text.Font titoloFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD);
            document.add(new com.lowagie.text.Paragraph(titolo, titoloFont));
            document.add(new com.lowagie.text.Paragraph("\n"));

            // Tabella PDF 
            com.lowagie.text.pdf.PdfPTable pdfTable = new com.lowagie.text.pdf.PdfPTable(table.getColumns().size());

            //  colonne
            for (TableColumn<T, ?> col : table.getColumns()) {
                pdfTable.addCell(col.getText());
            }

            // Dati righe
            for (T item : table.getItems()) {
                for (TableColumn<T, ?> col : table.getColumns()) {
                    Object cellData = col.getCellObservableValue(item).getValue();
                    pdfTable.addCell(cellData == null ? "" : cellData.toString());
                }
            }

            document.add(pdfTable);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Esportazione completata");
            alert.setHeaderText(null);
            alert.setContentText("PDF salvato con successo:\n" + file.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore durante l'esportazione");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Esporta una singola entità in PDF, stampando il suo {@code toString()} (eventualmente formattato dal tuo {@code Parser}).
     *
     * <p>Flusso:
     * <ol>
     *   <li>Apre un {@link FileChooser} per scegliere il percorso di salvataggio;</li>
     *   <li>Scrive il titolo e poi una {@code Paragraph} con il risultato di {@code Parser.getToStringParsed(entita.toString())};</li>
     *   <li>Mostra un {@link Alert} di successo o errore.</li>
     * </ol>
     *
     * @param entita   oggetto da stampare (il suo {@code toString()} verrà formattato/“parsato” dal tuo {@code Parser})
     * @param titolo   titolo da inserire in cima al documento
     * @param nomeFile nome file suggerito (senza estensione); verrà proposto come {@code nomeFile.pdf}
     * @param <T>      tipo dell’entità
     *
     * @apiNote Questa modalità è ideale se il {@code toString()} della tua entità è “strutturato” e il tuo {@code Parser}
     *          lo rende leggibile su più righe/campi.
     */
    public static <T> void esportaEntita(T entita, String titolo, String nomeFile){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva report PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName(nomeFile + ".pdf");

        File file = fileChooser.showSaveDialog(Main.getPrimaryStage());
        if (file == null) return;

        try (com.lowagie.text.Document document = new com.lowagie.text.Document()) {
            com.lowagie.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            
            com.lowagie.text.Font titoloFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD);
            document.add(new com.lowagie.text.Paragraph(titolo, titoloFont));
            document.add(new com.lowagie.text.Paragraph("\n"));



            document.add(new com.lowagie.text.Paragraph(Parser.getToStringParsed(entita.toString()))); // il to string mi viene parsato dal parser dei to string fatto ad hoc per i to string per questa progettazione
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Esportazione completata");
            alert.setHeaderText(null);
            alert.setContentText("PDF salvato con successo:\n" + file.getAbsolutePath());
            alert.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Errore durante l'esportazione");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }


}
