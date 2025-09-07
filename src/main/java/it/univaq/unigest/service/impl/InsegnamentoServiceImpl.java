package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Insegnamento;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.InsegnamentoService;

import java.util.List;
import java.util.Optional;

public class InsegnamentoServiceImpl implements InsegnamentoService {

    private final Repository<Insegnamento, String> repo;

    /**
     * Crea il servizio degli Insegnamenti iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare gli insegnamenti
     */
    public InsegnamentoServiceImpl (Repository<Insegnamento, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Insegnamento> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Insegnamento> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Insegnamento create (Insegnamento d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Insegnamento update (Insegnamento d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
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
