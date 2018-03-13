package io.eklesia.eklesia;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by ivanc on 12/03/2018.
 */

public class Comunicazione {
    private int id;
    private String titolo;
    private String descrizione;
    private Date data;
    private boolean privato;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isPrioritario() {
        return prioritario;
    }

    public void setPrioritario(boolean prioritario) {
        this.prioritario = prioritario;
    }

    private boolean prioritario;


    public Comunicazione(int id, String titolo, String descrizione, Date data, boolean privato, boolean prioritario) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.privato = privato;
        this.prioritario=prioritario;
    }

    public Comunicazione(JSONObject notizia) throws JSONException, ParseException {
        this.id = notizia.getInt("id");
        this.titolo = notizia.getString("titolo");
        this.descrizione = notizia.getString("descrizione");

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
        this.data = df.parse(notizia.getString("data_nascita"));

        this.privato = notizia.getBoolean("privato");
        this.prioritario=notizia.getBoolean("prioritario");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isPrivato() {
        return privato;
    }

    public void setPrivato(boolean privato) {
        this.privato = privato;
    }
}
