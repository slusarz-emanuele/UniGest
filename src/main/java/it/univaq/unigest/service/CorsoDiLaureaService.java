package it.univaq.unigest.service;

import it.univaq.unigest.model.CorsoDiLaurea;

import java.util.List;
import java.util.Optional;

public interface CorsoDiLaureaService extends CrudService<CorsoDiLaurea, String> {
    
    List<CorsoDiLaurea> filtra (CorsoDiLaurea filtro);

    default String labelById(String id) {
        return Optional.ofNullable(id)
                .flatMap(this::findById)
                .map(CorsoDiLaurea::getNome) // o come lo chiami tu
                .orElse("");
    }

}
