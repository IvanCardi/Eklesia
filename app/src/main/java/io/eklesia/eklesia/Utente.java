package io.eklesia.eklesia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    static private Integer id_chiesa;
    static private ArrayList <Integer> chiese_seguite=new ArrayList<>();
    static private ArrayList <Integer> eventi_seguiti=new ArrayList<>();

    public static void setAll(JSONObject utente) throws JSONException, ParseException {

        JSONArray chiese_seguite_j;
        JSONArray eventi_seguiti_j;

        Utente.id_utente = utente.getInt("id");
        Utente.nome = utente.getString("nome");
        Utente.cognome = utente.getString("cognome");
        Utente.email = utente.getString("email");
        Utente.setMaschio(utente.getInt("sesso"));

        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd");
        Utente.data_nascita = df.parse(utente.getString("data_nascita"));
        if(utente.isNull("id_chiesa"))
            Utente.setChiesaAppartenenza(null);
        else
            Utente.setChiesaAppartenenza(utente.getInt("id_chiesa"));

        eventi_seguiti_j = utente.getJSONArray("eventi_seguiti");
        if (eventi_seguiti_j.length()>0) {
            int len = eventi_seguiti_j.length();
            for (int i=0;i<len;i++){
                Utente.eventi_seguiti.add(eventi_seguiti_j.getJSONObject(i).getInt("id"));
            }
        }else{
            int b=1+2;
            int c;
            c=b+4;

        }

        chiese_seguite_j = utente.getJSONArray("chiese_seguite");
        if (chiese_seguite_j.length()>0) {
            int len = chiese_seguite_j.length();

            for (int i=0;i<len;i++){
                Utente.chiese_seguite.add(chiese_seguite_j.getJSONObject(i).getInt("id"));
            }
        }


    }

    public static int getIdUtente(){
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

    public static Date getDataNascita() {
        return data_nascita;
    }

    public static void setDataNascita(Date data_nascita) {
        Utente.data_nascita = data_nascita;
    }

    public static boolean isMaschio() {
        return maschio;
    }

    public static void setMaschio(int maschio) {
        if(maschio==1)
            Utente.maschio = true;
        else
            Utente.maschio=false;
    }

    public static int getChiesaAppartenenza() {
        return id_chiesa;
    }

    public static void setChiesaAppartenenza(Integer id_chiesa_appartenenza) {
            Utente.id_chiesa = id_chiesa_appartenenza;
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
