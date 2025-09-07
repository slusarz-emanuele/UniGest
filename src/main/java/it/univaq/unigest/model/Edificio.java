package it.univaq.unigest.model;

import it.univaq.unigest.model.common.Identificabile;

/**
 * La classe {@code Edificio} rappresenta
 * l'entità Edificio dell'università.
 *
 * <p>Contiene informazioni quali:
 * <ul>
 *   <li>ID dell'edificio</li>
 *   <li>Nome dell'edificio</li>
 * </ul> e i metodi di getter e setter relativi a tali informazioni.
 *
 * <p>Questa classe implementa l'interfaccia {@link Identificabile}.
 */
public class Edificio implements Identificabile<String> {

    private String id;
    private String nome;

    public Edificio(String id,String nome){
        this.id=id;
        this.nome=nome;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}