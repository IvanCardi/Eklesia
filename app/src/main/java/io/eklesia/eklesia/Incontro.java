package io.eklesia.eklesia;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Santa Caterina on 12/03/2018.
 */

public class Incontro {
    private int id;
    private String titolo;
    private Giorno giorno;
    private Orario orarioInizio;
    private Orario orarioFine;

    public Incontro(int id, String titolo, Giorno giorno, Orario orarioInizio, Orario orarioFine) {
        this.id = id;
        this.titolo = titolo;
        this.giorno = giorno;
        this.orarioInizio = orarioInizio;
        this.orarioFine = orarioFine;
    }

    public Incontro(JSONObject incontro) throws JSONException {
        this.id = incontro.getInt("id");
        this.titolo = incontro.getString("titolo");
        switch (incontro.getInt("giorno")){
            case 1:
                this.giorno=Giorno.Lunedì;
                break;
            case 2:
                this.giorno=Giorno.Martedì;
                break;
            case 3:
                this.giorno=Giorno.Mercoledì;
                break;
            case 4:
                this.giorno=Giorno.Giovedì;
                break;
            case 5:
                this.giorno=Giorno.Venerdì;
                break;
            case 6:
                this.giorno=Giorno.Sabato;
                break;
            case 7:
                this.giorno=Giorno.Domenica;
                break;
        }
        //this.orarioInizio = new Orario();
        this.orarioFine = orarioFine;
    }


    public Orario getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(Orario orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public Orario getOrarioFine() {
        return orarioFine;
    }

    public void setOrarioFine(Orario orarioFine) {
        this.orarioFine = orarioFine;
    }

    private enum Giorno {
        Lunedì,
        Martedì,
        Mercoledì,
        Giovedì,
        Venerdì,
        Sabato,
        Domenica
    }

    private class Orario {
        private int minuti;
        private int ore;

        public Orario(int ore, int minuti) {
            this.ore = ore;
            this.minuti = minuti;
        }

        public int getMinuti() {
            return minuti;
        }

        public void setMinuti(int minuti) {
            this.minuti = minuti;
        }

        public int getOre() {
            return ore;
        }

        public void setOre(int ore) {
            this.ore = ore;
        }

        public String toString(){
            String ore,minuti;
            if(this.ore<10)
                ore="0"+this.ore;
            else
                ore=this.ore+"";
            if(this.minuti<10)
                minuti="0"+this.minuti;
            else
                minuti=this.minuti+"";

            return ore+":"+minuti;
        }

        public Orario sottraiOrario(Orario orario){
            return new Orario(this.ore-orario.ore,this.minuti-orario.minuti);
        }
    }
}
