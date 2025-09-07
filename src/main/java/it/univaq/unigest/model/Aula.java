package it.univaq.unigest.model;

import it.univaq.unigest.model.common.Identificabile;

/**
 * La classe {@code Aula} rappresenta
 * l'entità Aula.
 *
 * <p>Contiene informazioni quali:
 * <ul>
 *   <li>ID dell'aula</li>
 *   <li>Capienza dell'aula</li>
 *   <li>Edificio in cui si trova l'aula</li>
 * </ul> e i metodi di getter e setter relativi a tali informazioni.
 *
 * <p>Questa classe implementa l'interfaccia {@link Identificabile}
 */
public class Aula implements Identificabile<String> {
    private String id;
    private int capienza;
    private String edificio;

    public Aula(String id,
                int capienza,
                String edificio){
        this.id=id;
        this.capienza=capienza;
        this.edificio=edificio;

    }

    @Override
    public String getId(){
        return this.id;
    }

    public int getCapienza(){
        return this.capienza;
    }

    public String getEdificio(){return this.edificio;}


    @Override
    public void setId(String id){
        this.id = id;
    }

    public void setCapienza(int capienza){
        this.capienza = capienza;
    }

    public void setEdificio(String edificio){
        this.edificio = edificio;
    }

    @Override
    public String toString(){
        return "id: " + this.id + " " +
                "capienza: " + this.capienza + " " +
                "edificio: " + this.edificio;
    }

    public String getEdificioNome() {
        return "";
    }
}