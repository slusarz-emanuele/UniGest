package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.model.Verbale;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.VerbaleService;

import java.util.List;
import java.util.Optional;

public class VerbaleServiceImpl implements VerbaleService {

    private final Repository<Verbale, String> repo;

    public VerbaleServiceImpl (Repository<Verbale, String> repo){
        this.repo = repo;
    }

    @Override
    public List<Verbale> findAll(){
        return repo.findAll();
    }

    @Override
    public Optional<Verbale> findById (String id){
        return repo.findById(id);
    }

    @Override
    public Verbale create (Verbale d){
        return repo.save(d);
    }

    @Override
    public Verbale update (Verbale d){
        return repo.save(d);
    }

    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    @Override
    public List<Verbale> filtra(Verbale filtro) {
        return repo.findAll().stream()
                .filter(v -> filtro.getId() == null || v.getId().equals(filtro.getId()))
                .filter(v -> filtro.getAppelloId() == null || v.getAppelloId().equalsIgnoreCase(filtro.getAppelloId()))
                .filter(v -> filtro.getDataChiusura() == null || v.getDataChiusura().equals(filtro.getDataChiusura()))
                .filter(v -> !filtro.getChiuso() || v.getChiuso()) // true → filtra solo chiusi
                .filter(v -> !filtro.getFirmato() || v.getFirmato()) // true → filtra solo firmati
                .filter(v -> filtro.getNote() == null || (v.getNote() != null && v.getNote().toLowerCase().contains(filtro.getNote().toLowerCase())))
                .toList();
    }

}
