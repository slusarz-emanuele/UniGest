package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Aula;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.AulaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AulaServiceImpl implements AulaService {

    private final Repository<Aula, String> repo;

    /**
     * Crea il servizio delle Aule iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare le Aule
     */
    public AulaServiceImpl (Repository<Aula, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Aula> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Aula> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Aula create (Aula d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Aula update (Aula d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Aula> filtra(Aula filtro) {
        return repo.findAll().stream()
                .filter(a -> filtro.getId() == null || a.getId().equalsIgnoreCase(filtro.getId()))
                .filter(a -> filtro.getCapienza() == 0 || a.getCapienza() == filtro.getCapienza())
                .filter(a -> filtro.getEdificio() == null || a.getEdificio().toLowerCase().contains(filtro.getEdificio().toLowerCase()))
                .toList();
    }

}
