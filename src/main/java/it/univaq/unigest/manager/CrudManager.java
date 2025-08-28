package it.univaq.unigest.manager;

import java.util.List;

public interface CrudManager<T> {

    /**
     * Carica tutti i dati dal file associato al manager.
     */
    void caricaDaFile();

    /**
     * Salva tutti i dati dal file associato al manager.
     */
    void salvaSuFile();

    /**
     * Aggiunge un nuovo elemento al manager.
     *
     * @param elemento elemento da aggiungere.
     */
    void aggiungi(T elemento);

    /**
     * Rimuove un elemento dal manager.
     *
     * @param elemento elemento da aggiungere.
     */
    void rimuovi(T elemento);


    List<T> getAll();

    /**
     * Aggiorna un elemento dal manager.
     *
     * @param nuovo elemento nuovo da sostituire.
     * @return Esito.
     */
    boolean aggiorna(T nuovo);

    /**
     * Filtra un elemento dal manager.
     *
     * @param filtro elemento nuovo da sostituire.
     * @return Lista di elementi corrispondenti all'oggetto del parametro.
     */
    List<T> filtra(T filtro);
}
