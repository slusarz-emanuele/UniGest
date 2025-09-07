package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Iscrizione;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.IscrizioneService;

import java.util.List;
import java.util.Optional;

public class IscrizioneServiceImpl implements IscrizioneService {

    private final Repository<Iscrizione, String> repo;

    /**
     * Crea il servizio delle Iscrizioni iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare le iscrizioni
     */
    public IscrizioneServiceImpl (Repository<Iscrizione, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Iscrizione> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Iscrizione> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Iscrizione create (Iscrizione d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Iscrizione update (Iscrizione d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Iscrizione> filtra(Iscrizione filtro) {
        return repo.findAll().stream()
                .filter(i -> filtro.getId() == null || i.getId().equals(filtro.getId()))
                .filter(i -> filtro.getRidStudenteCf() == null || i.getRidStudenteCf().equalsIgnoreCase(filtro.getRidStudenteCf()))
                .filter(i -> filtro.getRidAppello() == 0 || i.getRidAppello() == filtro.getRidAppello())
                .filter(i -> filtro.getDataIscrizione() == null || i.getDataIscrizione().equals(filtro.getDataIscrizione()))
                .filter(i -> !filtro.getRitirato() || i.getRitirato())
                .toList();
    }

}
