package it.univaq.unigest.service;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia che gestisce le operazioni CRUD
 * relative alle entità di tipo {@code T}.
 *
 * @param <T>  il tipo dell'entità gestita dal servizio
 * @param <ID> il tipo dell'identificativo dell'entità
 */
public interface CrudService<T, ID> {

    /**
     * Restituisce ogni entità di tipo {@code T}.
     *
     * @return una {@link List} al cui interno sono presenti tutte le entità
     */
    List<T> findAll();

    /**
     * Restituisce un'entità dato il suo identificativo.
     *
     * @param id l'identificativo dell'entità
     * @return un {@link Optional} contenente l'entità se presente, altrimenti vuoto
     */
    Optional<T> findById(ID id);

    /**
     * Crea una nuova entità.
     *
     * @param entity l'entità da andare a creare
     * @return l'entità creata
     */
    T create(T entity);

     /**
     * Aggiorna un'entità pre-esistente.
     *
     * @param entity l'entità con i dati aggiornati
     * @return l'entità aggiornata
     */
    T update(T entity);

    /**
     * Elimina un'entità in base al suo identificativo.
     *
     * @param id l'identificativo dell'entità da eliminare
     * @throws DeleteNotAllowedException se l'eliminazione non è attuabile
     */
    void deleteById(ID id) throws DeleteNotAllowedException;
}
