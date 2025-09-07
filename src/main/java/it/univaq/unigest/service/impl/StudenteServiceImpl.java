package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Studente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.StudenteService;

import java.util.List;
import java.util.Optional;

public class StudenteServiceImpl implements StudenteService {

    private final Repository<Studente, String> repo;

    /**
     * Crea il servizio dei Studenti iniettando il repository sottostante.
     *
     * @param repo repository per persistere/recuperare gli studenti
     */
    public StudenteServiceImpl (Repository<Studente, String> repo){
        this.repo = repo;
    }

    /** {@inheritDoc} */
    @Override
    public List<Studente> findAll(){
        return repo.findAll();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Studente> findById (String id){
        return repo.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Studente create (Studente d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public Studente update (Studente d){
        return repo.save(d);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Studente> filtra(Studente filtro) {
        return repo.findAll().stream()
                .filter(s -> filtro.getCf() == null || s.getCf().equalsIgnoreCase(filtro.getCf()))
                .filter(s -> filtro.getNome() == null || s.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(s -> filtro.getCognome() == null || s.getCognome().toLowerCase().contains(filtro.getCognome().toLowerCase()))
                .filter(s -> filtro.getEmail() == null || s.getEmail().toLowerCase().contains(filtro.getEmail().toLowerCase()))
                .filter(s -> filtro.getDataNascita() == null || s.getDataNascita().equals(filtro.getDataNascita()))
                .filter(s -> filtro.getDataIngressoUniversita() == null || s.getDataIngressoUniversita().equals(filtro.getDataIngressoUniversita()))
                .filter(s -> filtro.getMatricola() == null || s.getMatricola().toLowerCase().contains(filtro.getMatricola().toLowerCase()))
                .filter(s -> filtro.getCorsoDiLaurea() == null ||
                        s.getCorsoDiLaurea() != null &&
                                s.getCorsoDiLaurea().trim().equalsIgnoreCase(filtro.getCorsoDiLaurea().trim()))
                .filter(s -> filtro.getDataImmatricolazione() == null || s.getDataImmatricolazione().equals(filtro.getDataImmatricolazione()))
                .filter(s -> filtro.getEsami() == null || s.getEsami().equals(filtro.getEsami()))
                .filter(s -> filtro.getCfu() == null || s.getCfu().equals(filtro.getCfu()))
                .filter(s -> filtro.getMediaPonderata() == null || filtro.getMediaPonderata().equals(s.getMediaPonderata()))
                .filter(s -> filtro.getMediaAritmetica() == null || filtro.getMediaAritmetica().equals(s.getMediaAritmetica()))
                .toList();
    }

}
