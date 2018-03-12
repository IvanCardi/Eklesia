package io.eklesia.eklesia;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by ivanc on 12/03/2018.
 */

public class Notizia {
    private int id;
    //private String tipo;
    private String titolo;
    private String corpo;
    private Date data;
    private boolean visibile;


    public Notizia(int id, String tipo, String titolo, String corpo, Date data, boolean visibile) {
        this.id = id;
        //this.tipo = tipo;
        this.titolo = titolo;
        this.corpo = corpo;
        this.data = data;
        this.visibile = visibile;
    }

    public Notizia(JSONObject notizia) throws JSONException, ParseException {
        this.id = notizia.getInt("id");
        //this.tipo = notizia.getString("tipo");
        this.titolo = notizia.getString("titolo");
        this.corpo = notizia.getString("corpo");

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
        this.data = df.parse(notizia.getString("data_nascita"));

        this.visibile = notizia.getBoolean("visibile");
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

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }
}
