package it.univaq.unigest.service;

public class DeleteNotAllowedException extends RuntimeException {
    public DeleteNotAllowedException(String message) {
        super(message);
    }
}
