package it.univaq.unigest.service;

import it.univaq.unigest.model.Iscrizione;

import java.util.List;

public class IscrizioneService extends CrudService<Iscrizione, String> {

    List<Iscrizione> filtra (Iscrizione filtro);
    
}
