package it.univaq.unigest.gui.componenti;

import javafx.scene.layout.VBox;
import java.util.List;

public interface TabellaView<T>{
    VBox getView(List<T> elementi);
}