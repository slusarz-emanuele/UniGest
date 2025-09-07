package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.EdificioService;

import java.util.List;
import java.util.Optional;

public class EdificioServiceImpl implements EdificioService {

    private final Repository<Edificio, String> repo;

    /**
     * Crea il servizio degli Edifici iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare gli edifici
     */
    public EdificioServiceImpl (Repository<Edificio, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Edificio> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Edificio> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Edificio create (Edificio d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Edificio update (Edificio d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Edificio> filtra(Edificio filtro) {
        return repo.findAll().stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getNome() == null || e.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .toList();
    }

}
