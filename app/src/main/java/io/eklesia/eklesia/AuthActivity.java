package io.eklesia.eklesia;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        final String a_token =sp_connection.getString("a_token","");
        final String r_token = sp_connection.getString("r_token","");


        if(a_token.equals("") && r_token.equals(""))
        {
            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else{

            CallbackFunction RispostaConAccessToken= new CallbackFunction() {
                @Override
                public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                    Utente.setAll(risposta.getJSONArray("utente").getJSONObject(0));
                    Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                    startActivity(i);
                }

                @Override
                public void onError(JSONObject risposta) throws JSONException {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("grant_type", "refresh_token");
                    jsonObject.put("client_id", getString(R.string.client_id));
                    jsonObject.put("client_secret", getString(R.string.client_secret));
                    jsonObject.put("refresh_token", r_token);

                    CallbackFunction RispostaConRefreshToken = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                            SharedPreferences.Editor editor_connection = sp_connection.edit();
                            editor_connection.putString("a_token", risposta.getString("access_token"));
                            editor_connection.putString("r_token", risposta.getString("refresh_token"));
                            editor_connection.commit();

                            CallbackFunction RispostaGetInformazioni = new CallbackFunction() {
                                @Override
                                public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                                    Utente.setAll(risposta.getJSONArray("utente").getJSONObject(0));
                                    Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                                    startActivity(i);
                                }

                                @Override
                                public void onError(JSONObject risposta) throws JSONException {
                                    Toast.makeText(AuthActivity.this,"non andato",Toast.LENGTH_LONG).show();
                                }
                            };

                            Map<String,String> map= new HashMap<>();
                            map.put("a_token",sp_connection.getString("a_token",""));

                            RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
                            requestQueue.add(Connessione.sendGet(map, "api/utente", RispostaGetInformazioni));


                        }

                        @Override
                        public void onError(JSONObject risposta) {
                            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
                    requestQueue.add(Connessione.sendPost(null,"oauth/token", jsonObject, RispostaConRefreshToken));
                }
            };

            Map<String,String> map= new HashMap<>();
            map.put("a_token",sp_connection.getString("a_token",""));

            RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
            requestQueue.add(Connessione.sendGet(map,"api/utente", RispostaConAccessToken));
        }



    }
}
