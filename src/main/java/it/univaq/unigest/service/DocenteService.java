package it.univaq.unigest.service;

import it.univaq.unigest.model.Docente;

import java.util.List;

public interface DocenteService extends CrudService<Docente, String> {

    List<Docente> filtra (Docente filtro);
    String getGeneralitaDaCf(String cf);

}
