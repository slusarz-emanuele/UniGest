package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.CorsoDiLaurea;
import it.univaq.unigest.model.Docente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.CorsoDiLaureaService;

import java.util.List;
import java.util.Optional;

public class CorsoDiLaureaServiceImpl implements CorsoDiLaureaService {

    private final Repository<CorsoDiLaurea, String> repo;

    /**
     * Crea il servizio dei Corsi Di Laurea iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare i Corsi Di Laurea
     */
    public CorsoDiLaureaServiceImpl(Repository<CorsoDiLaurea, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<CorsoDiLaurea> findAll (){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<CorsoDiLaurea> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public CorsoDiLaurea create (CorsoDiLaurea d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public CorsoDiLaurea update (CorsoDiLaurea d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<CorsoDiLaurea> filtra(CorsoDiLaurea filtro){
        return repo.findAll().stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                //.filter()
                .toList();
    }
}
