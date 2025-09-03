package it.univaq.unigest.util;

import it.univaq.unigest.gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;

public class PdfHelper {

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
    
    // esporta un enità (con il toString, che fa il parsing)
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
