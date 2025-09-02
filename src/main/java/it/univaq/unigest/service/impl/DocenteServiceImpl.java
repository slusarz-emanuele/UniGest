package it.univaq.unigest.service.impl;

import it.univaq.unigest.model.Docente;
import it.univaq.unigest.repository.Repository;
import it.univaq.unigest.service.DocenteService;

import java.util.List;
import java.util.Optional;

public class DocenteServiceImpl implements DocenteService {

    private final Repository<Docente, String> repo;

    public DocenteServiceImpl(Repository<Docente, String> repo){
        this.repo = repo;
    }

    @Override
    public List<Docente> findAll (){
        return repo.findAll();
    }

    @Override
    public Optional<Docente> findById (String id){
        return repo.findById(id);
    }

    @Override
    public Docente create (Docente d){
        return repo.save(d);
    }

    @Override
    public Docente update (Docente d){
        return repo.save(d);
    }

    @Override
    public void deleteById (String id){
        repo.deleteById(id);
    }

    @Override
    public List<Docente> filtra(Docente filtro) {
        return repo.findAll().stream()
                .filter(d -> filtro.getCodiceDocente()==null || d.getCodiceDocente().equalsIgnoreCase(filtro.getCodiceDocente()))
                .filter(d -> filtro.getNome()==null || d.getNome().toLowerCase().contains(filtro.getNome().toLowerCase()))
                .filter(d -> filtro.getCognome()==null || d.getCognome().toLowerCase().contains(filtro.getCognome().toLowerCase()))
                .filter(d -> filtro.getEmail()==null || d.getEmail().toLowerCase().contains(filtro.getEmail().toLowerCase()))
                .filter(d -> filtro.getDataNascita()==null || d.getDataNascita().equals(filtro.getDataNascita()))
                .filter(d -> filtro.getDataIngressoUniversitaDocente()==null || d.getDataIngressoUniversitaDocente().equals(filtro.getDataIngressoUniversitaDocente()))
                .filter(d -> filtro.getDipartimento()==null || d.getDipartimento().toLowerCase().contains(filtro.getDipartimento().toLowerCase()))
                .filter(d -> filtro.getQualifica()==null || d.getQualifica().toLowerCase().contains(filtro.getQualifica().toLowerCase()))
                .toList();
    }

    @Override
    public String getGeneralitaDaCf(String cf) {
        return repo.findAll().stream()
                .filter(d -> d.getCf().equals(cf))
                .map(d -> d.getNome() + " " + d.getCognome() + " (ID: " + d.getCodiceDocente() + ")")
                .findFirst().orElse("");
    }

}
