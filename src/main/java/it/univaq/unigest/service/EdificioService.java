package it.univaq.unigest.service;

import it.univaq.unigest.model.Edificio;

import java.util.List;

public interface EdificioService extends CrudService<Edificio, String> {

    List<Edificio> filtra (Edificio filtro);
    
}
