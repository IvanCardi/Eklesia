package io.eklesia.eklesia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by ivanc on 20/02/2018.
 */

public class Utente {

    static private int id_utente;
    static private String nome;
    static private String cognome;
    static private String email;
    static private Date data_nascita;
    static private boolean maschio;
    static private int id_chiesa_appartenenza;
    static private JSONArray chiese_seguite_j;
    static private JSONArray eventi_seguiti_j;
    static private ArrayList <Integer> chiese_seguite;
    static private ArrayList <Integer> eventi_seguiti;

    public static void setAll(JSONObject utente) throws JSONException, ParseException {

        Utente.id_utente = utente.getInt("id_utente");
        Utente.nome = utente.getString("nome");
        Utente.cognome = utente.getString("cognome");
        Utente.email = utente.getString("email");
        Utente.maschio = utente.getBoolean("sesso");

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
        Utente.data_nascita = df.parse(utente.getString("data_nascita"));

        Utente.id_chiesa_appartenenza = utente.getInt("id_chiesa_appartenenza");
        Chiesa.setId_chiesa(id_chiesa_appartenenza);

        chiese_seguite_j = utente.getJSONArray("chiese_seguite");
        if (chiese_seguite_j != null) {
            int len = chiese_seguite_j.length();
            for (int i=0;i<len;i++){
                chiese_seguite.add(Integer.parseInt(chiese_seguite_j.get(i).toString()));
            }
        }

        eventi_seguiti_j = utente.getJSONArray("utenti_seguiti");
        if (eventi_seguiti_j != null) {
            int len = eventi_seguiti_j.length();
            for (int i=0;i<len;i++){
                eventi_seguiti.add(Integer.parseInt(eventi_seguiti.get(i).toString()));
            }
        }
    }
    public static int getId_utente(){
        return id_utente;
    }
    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        Utente.nome = nome;
    }

    public static String getCognome() {
        return cognome;
    }

    public static void setCognome(String cognome) {
        Utente.cognome = cognome;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Utente.email = email;
    }

    public static Date getData_nascita() {
        return data_nascita;
    }

    public static void setData_nascita(Date data_nascita) {
        Utente.data_nascita = data_nascita;
    }

    public static boolean isMaschio() {
        return maschio;
    }

    public static void setMaschio(boolean maschio) {
        Utente.maschio = maschio;
    }

    public static int getChiesa_appartenenza() {
        return id_chiesa_appartenenza;
    }

    public static void setChiesa_appartenenza(int id_chiesa_appartenenza) {
        Utente.id_chiesa_appartenenza = id_chiesa_appartenenza;
    }

    public static void addChiesa (Integer chiesa){
        Utente.chiese_seguite.add(chiesa);
    }

    public static void removeChiesa (Integer chiesa){
        Utente.chiese_seguite.remove(chiesa);
    }

    public static void addEvento (Integer evento){
        Utente.eventi_seguiti.add(evento);
    }

    public static void removeEvento (Integer evento){
        Utente.eventi_seguiti.remove(evento);
    }

    public static boolean checkChiesa(Integer chiesa){
        return chiese_seguite.contains(chiesa);
    }

    public static boolean checkEvento (Integer evento){
        return eventi_seguiti.contains(evento);
    }

}
