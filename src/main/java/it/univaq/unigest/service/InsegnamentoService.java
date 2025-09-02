package it.univaq.unigest.service;

import it.univaq.unigest.model.Insegnamento;

import java.util.List;

public class InsegnamentoService extends CrudService<Insegnamento, String> {

    List<Insegnamento> filtra (Insegnamento filtro);
    
}
