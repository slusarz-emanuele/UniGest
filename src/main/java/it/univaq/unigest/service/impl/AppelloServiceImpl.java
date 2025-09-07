package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.AppelloService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppelloServiceImpl implements AppelloService {

    private final Repository<Appello, String> repo;

    /**
     * Crea il servizio degli Appelli iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare gli Appelli
     */
    public AppelloServiceImpl (Repository<Appello, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Appello> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Appello> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Appello create (Appello d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Appello update (Appello d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Appello> filtra(Appello filtro) {
        return repo.findAll().stream()
                .filter(a -> Integer.parseInt(filtro.getId()) == 0 || a.getId() == filtro.getId())
                .filter(a -> filtro.getRidInsegnamento() == null || a.getRidInsegnamento().equalsIgnoreCase(filtro.getRidInsegnamento()))
                .filter(a -> filtro.getData() == null || a.getData().equals(filtro.getData()))
                .filter(a -> filtro.getOra() == null || a.getOra().equals(filtro.getOra()))
                .filter(a -> filtro.getRidAula() == null || a.getRidAula().equalsIgnoreCase(filtro.getRidAula()))
                .filter(a -> filtro.getRidDocente() == null || a.getRidDocente().equalsIgnoreCase(filtro.getRidDocente()))
                .filter(a -> filtro.getRidVerbale() == null || a.getRidVerbale().equalsIgnoreCase(filtro.getRidVerbale()))
                .toList();
    }

}
