package it.univaq.unigest.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {

    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    void deleteById(ID id);
    ID nextId();
}
