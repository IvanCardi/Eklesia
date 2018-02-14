package io.eklesia.eklesia;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanc on 13/02/2018.
 */

public class Connessione {
    //static String pref = "http://192.168.1.5/portale/public/";
    static String pref = "http://192.168.1.4/";
    boolean risposta;

    public Connessione(){
        super();
    }

    public static JsonObjectRequest sendGet(final SharedPreferences sp, String uri, final CallbackFunction cbf){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, pref + uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cbf.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    JSONObject jo=new JSONObject(body);
                    try {
                        cbf.onError(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    // exception
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + sp.getString("a_token", ""));
                return headers;
            }
        };

        return jsonObjectRequest;
    }

    public static JsonObjectRequest sendPost(SharedPreferences sp, String uri, final JSONObject jsonObject, final CallbackFunction cbf){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, pref + uri, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    cbf.onResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    JSONObject jo=new JSONObject(body);
                    try {
                        cbf.onError(jo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    // exception
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        return jsonObjectRequest;
    }



}
