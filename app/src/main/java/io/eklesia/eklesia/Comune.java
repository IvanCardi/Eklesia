package io.eklesia.eklesia;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Santa Caterina on 10/03/2018.
 */

public class Comune {
    private int id;
    private String nome;
    private String latitudine;
    private String longitudine;
    private String regione;
    private String provincia;

    public Comune(int id, String nome, String latitudine, String longitudine, String regione, String provincia) {
        this.id = id;
        this.nome = nome;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.regione = regione;
        this.provincia = provincia;
    }

    public Comune(JSONObject comune) throws JSONException {
        this.id = comune.getInt("id");
        this.nome = comune.getString("nome");
        this.latitudine = comune.getString("latitudine");
        this.longitudine = comune.getString("longitudine");
        this.regione = comune.getString("regione");
        this.provincia = comune.getString("provincia");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }


}
