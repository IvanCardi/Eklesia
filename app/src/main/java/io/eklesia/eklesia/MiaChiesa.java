package io.eklesia.eklesia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by ivanc on 20/02/2018.
 */

public class MiaChiesa {
    static private int id = 0;
    static private String nome;
    static private String indirizzo;
    static private String sito;
    static private String email;
    static private String telefono;
    static private String foto;
    static private Congregazione congregazione = null;
    static private Comune comune = null;
    static private ArrayList<Incontro> incontri = new ArrayList<>();
    static private ArrayList<Comunicazione> notizie = new ArrayList<>();

    public static void setInfo(JSONObject chiesa) throws JSONException {
        MiaChiesa.id = chiesa.getInt("id");
        MiaChiesa.nome = chiesa.getString("nome");
        MiaChiesa.indirizzo = chiesa.getString("indirizzo");
        MiaChiesa.sito = chiesa.getString("sito");
        MiaChiesa.email = chiesa.getString("email");
        MiaChiesa.telefono = chiesa.getString("telefono");
        MiaChiesa.foto = chiesa.getString("foto");
        if (!chiesa.isNull("congregazione"))
            MiaChiesa.congregazione = new Congregazione(chiesa.getJSONObject("congregazione"));
        if (!chiesa.isNull("comune"))
            MiaChiesa.comune = new Comune(chiesa.getJSONObject("comune"));
    }

    public static void clear() {
        MiaChiesa.id = 0;
        MiaChiesa.nome = "";
        MiaChiesa.indirizzo = "";
        MiaChiesa.sito = "";
        MiaChiesa.email = "";
        MiaChiesa.telefono = "";
        MiaChiesa.foto = "";
        MiaChiesa.congregazione = null;
        MiaChiesa.comune = null;
    }

    public static boolean isSet() {
        if (MiaChiesa.id == 0)
            return false;
        return true;
    }

    public static void setIncontri(JSONArray incontri) throws JSONException {
        for (int i = 0; i < incontri.length(); i++) {
            MiaChiesa.incontri.add(new Incontro(incontri.getJSONObject(i)));
        }
    }

    public static void setNotizie(JSONArray notizie) throws JSONException, ParseException {
        for (int i = 0; i < notizie.length(); i++) {
            MiaChiesa.notizie.add(new Comunicazione(notizie.getJSONObject(i)));
        }
    }


    public static ArrayList<Incontro> getIncontri() {
        return incontri;
    }

    public static ArrayList<Comunicazione> getNotizie() {
        return notizie;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        MiaChiesa.nome = nome;
    }

    public static String getIndirizzo() {
        return indirizzo;
    }

    public static void setIndirizzo(String indirizzo) {
        MiaChiesa.indirizzo = indirizzo;
    }

    public static String getSito() {
        return sito;
    }

    public static void setSito(String sito) {
        MiaChiesa.sito = sito;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        MiaChiesa.email = email;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static void setTelefono(String telefono) {
        MiaChiesa.telefono = telefono;
    }

    public static String getFoto() {
        return foto;
    }

    public static void setFoto(String foto) {
        MiaChiesa.foto = foto;
    }

    public static Congregazione getCongregazione() {
        return congregazione;
    }

    public static void setCongregazione(Congregazione congregazione) {
        MiaChiesa.congregazione = congregazione;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        MiaChiesa.id = id;
    }

    public static Comune getComune() {
        return comune;
    }

    public static void setComune(Comune comune) {
        MiaChiesa.comune = comune;
    }
}
