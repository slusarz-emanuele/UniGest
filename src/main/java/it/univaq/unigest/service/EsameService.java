package it.univaq.unigest.service;

import it.univaq.unigest.model.Esame;

import java.util.List;

public class EsameService extends CrudService<Esame, String> {

    List<Esame> filtra (Esame filtro);
    
}
