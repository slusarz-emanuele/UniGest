package it.univaq.unigest.service;

import it.univaq.unigest.model.Verbale;

import java.util.List;

public interface VerbaleService extends CrudService<Verbale, String> {

    List<Verbale> filtra (Verbale filtro);
    
}
