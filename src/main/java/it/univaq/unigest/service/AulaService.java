package it.univaq.unigest.service;

import it.univaq.unigest.model.Aula;

import java.util.List;

public interface AulaService extends CrudService<Aula, String> {

    List<Aula> filtra (Aula filtro);
    
}
