package io.eklesia.eklesia;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanc on 13/02/2018.
 */

public class Connessione {
    static String pref = "http://192.168.1.75/";
    boolean risposta;

    public Connessione(){
        super();
    }

    public static JsonObjectRequest sendGet (String uri, final CallbackFunction cbf){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, pref + uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cbf.onResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cbf.onError(error.toString());
            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijc0MDFlNDFlNWQ5MzI3NGQ1NDJmMTJlOTkzZTBhZGYxMDdhZDZmMmEwZjE4YjNhMjI2NzEzOTg0ZTA4MzdiMDZjNWRkOTJkOWM0NjExNTVkIn0.eyJhdWQiOiIzIiwianRpIjoiNzQwMWU0MWU1ZDkzMjc0ZDU0MmYxMmU5OTNlMGFkZjEwN2FkNmYyYTBmMThiM2EyMjY3MTM5ODRlMDgzN2IwNmM1ZGQ5MmQ5YzQ2MTE1NWQiLCJpYXQiOjE1MTg1Mjk0NjEsIm5iZiI6MTUxODUyOTQ2MSwiZXhwIjoxNTE4NTMwMDYwLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.mhAASf8n-Ne23XtxMcO9A4WihwQGnyf-CiaU7E24NQgR49evSiv54TbG0ZgdpqgJbeB_zd58r2LKR33NipfWhVxeT3Cx1E6SaFj4bEkJdYXSk_YjY771PpTJp7P9nPambjKiSMS_DG5izYdIVa9tio4elj9v9rHAZk-6Kq8razVXzQpvMvR2b6khKDv2W1CGSTa9Vi7iCQdBu1dE4MPdajDrmBHSCwDipJgSkIPW79mQo0yFjRUJQ6j7jOHGFqvAGvgs4k1-Bt09S24fLNSdwsXbMA1jPtXnp6SX2ZA6opON1q6vxdow2G9NKGN5B33usgpErZ8PJolJCQ0q11tRyCAEES3D1bd3NQNJBZ7fkWRa628Icf5-9z6NUquL6LcnwmYFNfJBSc7G29kgpbWD2twC1IqFghsIb95lwQWep7Q3YPuAx8g_BjLUD_KvVlqkkKu_AC12LnBcUcJfKvEvSqJmm_l6LggaKOXxbENh-msQSPW9pzqbJrBLjz2xuGQa7zVMIuzEs39nPwSFBte1r53W_-4PYIDmrmbMgRbKWQO4nJ-O4S8g-oBB7-8YZ_5CMDKxR4HrsVpBkY4E1UENkI7zfrb_pml7WGrIHN9dKMZJIaPQaMJqrbOzZZ1OJH1DBYkRZ1WCjmn-RD6BFnxwLRGK5fDSv5xGIZoNmPxMCAA");
                return headers;
            }
        };

        return jsonObjectRequest;
    }

}
