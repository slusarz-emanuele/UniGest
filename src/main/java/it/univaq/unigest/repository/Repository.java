package it.univaq.unigest.repository;

import java.util.List;
import java.util.Optional;

/**
 * Contratto generico per un repository di persistenza.
 * <p>
 * È pensato per astrarre l’accesso ai dati (file, DB, memoria, ecc.)
 * e fornire operazioni CRUD minime. L’implementazione concreta decide
 * come tradurre queste operazioni sullo storage sottostante.
 *
 * @param <T>  tipo dell’entità gestita
 * @param <ID> tipo dell’identificatore univoco dell’entità
 */
public interface Repository<T, ID> {

    /**
     * Restituisce tutte le entità presenti nello storage.
     *
     * @return lista di tutte le entità (mai {@code null}; può essere vuota)
     */
    List<T> findAll();

    /**
     * Cerca un’entità per identificatore.
     *
     * @param id identificatore da cercare (non deve essere {@code null})
     * @return {@link Optional} contenente l’entità se presente, altrimenti vuoto
     */
    Optional<T> findById(ID id);

    /**
     * Salva o aggiorna un’entità.
     * <p>
     * Il comportamento tipico è quello di un <em>upsert</em>:
     * se esiste già un’entità con lo stesso ID viene aggiornata,
     * altrimenti viene inserita.
     *
     * @param entity entità da salvare (non deve essere {@code null})
     * @return l’entità salvata, eventualmente con ID valorizzato dall’implementazione
     */
    T save(T entity);

    /**
     * Elimina un’entità dato il suo identificatore.
     * <p>
     * Se l’ID non esiste, l’operazione è tipicamente <em>idempotente</em>
     * (non solleva eccezioni).
     *
     * @param id identificatore dell’entità da eliminare
     */
    void deleteById(ID id);

    /**
     * Genera un nuovo identificatore valido per una futura creazione.
     * <p>
     * Il contratto non impone un formato specifico: può essere un contatore,
     * un UUID, un valore derivato, ecc. L’unico requisito è l’univocità logica
     * all’interno dello storage gestito.
     *
     * @return un nuovo ID pronto per essere assegnato ad una nuova entità
     */
    ID nextId();
}
