package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.InsegnamentoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InsegnamentoServiceImpl implements InsegnamentoService {

    private final Repository<Insegnamento, String> repo;

    public InsegnamentoServiceImpl (Repository<Insegnamento, String> repo){
        this.repo = repo;
    }

    @Override
    public List<Insegnamento> findAll(){
        return repo.findAll();
    }

    @Override
    public Optional<Insegnamento> findById (String id){
        return repo.findById(id);
    }

    @Override
    public Insegnamento create (Insegnamento d){
        return repo.save(d);
    }

    @Override
    public Insegnamento update (Insegnamento d){
        return repo.save(d);
    }

    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    @Override
    public List<Insegnamento> filtra(Insegnamento filtro) {
        return repo.findAll().stream()
                .filter(i -> filtro.getId() == null || i.getId().equalsIgnoreCase(filtro.getId()))
                .filter(i -> filtro.getNome() == null || i.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(i -> filtro.getCfu() == null || i.getCfu().equals(filtro.getCfu()))
                .filter(i -> filtro.getCorsoDiLaureaId() == null || i.getCorsoDiLaureaId().equalsIgnoreCase(filtro.getCorsoDiLaureaId()))
                .filter(i -> filtro.getDocenti() == null || filtro.getDocenti().isEmpty() ||
                        i.getDocenti().stream().anyMatch(doc -> filtro.getDocenti().contains(doc)))
                .filter(i -> filtro.getAnno() == null || i.getAnno().equals(filtro.getAnno()))
                .filter(i -> filtro.getSemestre() == null || i.getSemestre().equals(filtro.getSemestre()))
                .toList();
    }

}
