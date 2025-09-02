package it.univaq.unigest.service;

import it.univaq.unigest.model.Studente;

import java.util.List;

public class StudenteService extends CrudService<Studente, String> {

    List<Studente> filtra (Studente filtro);
    
}
