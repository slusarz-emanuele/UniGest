package it.univaq.unigest.service;

/**
 * Eccezione lanciata quando si tenta di effettuare 
 * un'operazione di eliminazione su un'entità
 * che non la supporta
 * <p>
 * Estende {@link RuntimeException}
 */
public class DeleteNotAllowedException extends RuntimeException {
    public DeleteNotAllowedException(String message) {
        super(message);
    }
}
