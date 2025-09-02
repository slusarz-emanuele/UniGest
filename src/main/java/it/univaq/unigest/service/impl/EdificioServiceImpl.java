package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Edificio;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.EdificioService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EdificioServiceImpl implements EdificioService {

    private final Repository<Edificio, String> repo;

    public EdificioServiceImpl (Repository<Edificio, String> repo){
        this.repo = repo;
    }

    @Override
    public List<Edificio> findAll(){
        return repo.findAll();
    }

    @Override
    public Optional<Edificio> findById (String id){
        return repo.findById(id);
    }

    @Override
    public Edificio create (Edificio d){
        return repo.save(d);
    }

    @Override
    public Edificio update (Edificio d){
        return repo.save(d);
    }

    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    @Override
    public List<Edificio> filtra(Edificio filtro) {
        return repo.findAll().stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getNome() == null || e.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .toList();
    }

}
