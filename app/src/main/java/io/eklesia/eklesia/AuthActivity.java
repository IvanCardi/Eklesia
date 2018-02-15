package io.eklesia.eklesia;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        //final String client_id = "1";
        //final String client_secret = "mlJHYMcFYLDTzjxio1SR7crta42sEAvzr21WXAxj";
        final String client_id = "3";
        final String client_secret = "PgAXIt0XZFe32G7BbJKOKWEUriZd720rj2AXJ19Q";

        final String a_token =sp.getString("a_token",null);
        final String r_token=sp.getString("r_token",null);
        final String primo_accesso = sp.getString("primo_accesso", null);

        if(a_token!=null&&r_token!=null)
        {
            CallbackFunction cbf = new CallbackFunction() {
                @Override
                public void onResponse(JSONObject risposta) {

                    if (primo_accesso!= null) {
                        Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(AuthActivity.this, ScegliChiesaPrimoAccessoActivity.class);
                        startActivity(i);
                    }
                }

                @Override
                public void onError(JSONObject risposta) throws JSONException {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("r_token", r_token);
                    jsonObject.put("grant_type", "refresh_token");
                    jsonObject.put("client_id", client_id);
                    jsonObject.put("client_secret", client_secret);
                    jsonObject.put("refresh_token", sp.getString("r_token", ""));

                    CallbackFunction cbf2 = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException {
                            editor.putString("a_token", risposta.getString("access_token"));
                            editor.putString("r_token",risposta.getString("refresh_token"));
                            editor.commit();

                            if (primo_accesso!= null) {
                                Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(AuthActivity.this, ScegliChiesaPrimoAccessoActivity.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onError(JSONObject risposta) {
                            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
                    requestQueue.add(Connessione.sendPost(null,"oauth/token", jsonObject, cbf2));
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
            Map<String,String> map= new HashMap<>();
            map.put("a_token",sp.getString("a_token",""));
            requestQueue.add(Connessione.sendGet(map,"api/utente", cbf));
        }
        else{
            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
            startActivity(i);

        }



    }
}
