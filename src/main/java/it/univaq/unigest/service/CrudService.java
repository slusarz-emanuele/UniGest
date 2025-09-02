package it.univaq.unigest.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T create(T entity);
    T update(T entity);
    void deleteById(ID id) throws DeleteNotAllowedException;
}
