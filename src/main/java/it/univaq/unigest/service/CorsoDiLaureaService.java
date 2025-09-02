package it.univaq.unigest.service;

import it.univaq.unigest.model.CorsoDiLaurea;

import java.util.List;

public class CorsoDiLaureaService extends CrudService<CorsoDiLaurea, String> {
    
    List<CorsoDiLaurea> filtra (CorsoDiLaurea filtro);
    
}
