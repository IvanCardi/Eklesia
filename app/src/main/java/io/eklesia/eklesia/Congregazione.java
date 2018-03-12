package io.eklesia.eklesia;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Santa Caterina on 10/03/2018.
 */

public class Congregazione {

    public Congregazione(int id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }

    public Congregazione(JSONObject congregazione) throws JSONException {
        this.setId(congregazione.getInt("id"));
        this.setNome(congregazione.getString("nome"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    private String nome;
}
