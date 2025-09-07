package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Appello;
import it.univaq.unigest.model.Esame;
import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.EsameService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EsameServiceImpl implements EsameService {

    private final Repository<Esame, String> repo;

    /**
     * Crea il servizio degli Esami iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare gli Esami
     */
    public EsameServiceImpl (Repository<Esame, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Esame> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Esame> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Esame create (Esame d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Esame update (Esame d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Esame> filtra(Esame filtro) {
        return repo.findAll().stream()
                .filter(e -> filtro.getId() == null || e.getId().equalsIgnoreCase(filtro.getId()))
                .filter(e -> filtro.getIscrizioneId() == null || e.getIscrizioneId().equalsIgnoreCase(filtro.getIscrizioneId()))
                .filter(e -> filtro.getVoto() == null || e.getVoto().equals(filtro.getVoto()))
                .filter(e -> filtro.getCfu() == null || e.getCfu().equals(filtro.getCfu()))
                .filter(e -> !filtro.isLode() || e.isLode())
                .filter(e -> !filtro.isRifiutato() || e.isRifiutato())
                .filter(e -> !filtro.isVerbalizzato() || e.isVerbalizzato())
                .toList();
    }

}
