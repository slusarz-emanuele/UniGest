package it.univaq.unigest.service;

import it.univaq.unigest.model.Appello;

import java.util.List;

public interface AppelloService extends CrudService<Appello, String> {

    List<Appello> filtra (Appello filtro);
    
}
