package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.email_login);
        final EditText pwd = (EditText) findViewById(R.id.pwd_login);
        final TextView registrati = (TextView) findViewById(R.id.registrati_login);
        Button accedi = (Button) findViewById(R.id.accedi_login);

        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor_connection = sp_connection.edit();

        final JSONObject jsonObject = new JSONObject();

        final Map<Integer, String> errori = new HashMap<>();

        final CallbackFunction RispostaLogin = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                editor_connection.putString("a_token", risposta.getString("access_token"));
                editor_connection.putString("r_token", risposta.getString("refresh_token"));
                editor_connection.commit();

                CallbackFunction RispostaGetInformazioni = new CallbackFunction() {
                    @Override
                    public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                        Utente.setAll(risposta.getJSONObject("utente"));
                    }

                    @Override
                    public void onError(JSONObject risposta) throws JSONException {

                    }
                };

                Map<String,String> map= new HashMap<>();
                map.put("a_token",sp_connection.getString("a_token",""));

                RequestQueue requestQueue = Volley.newRequestQueue( LoginActivity.this);
                requestQueue.add(Connessione.sendGet(map, "api/utente", RispostaGetInformazioni));

                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {
                Snackbar.make(findViewById(R.id.login_layout), risposta.getString("message"), Toast.LENGTH_LONG).show();
            }
        };

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean risposta = validator(errori, email, pwd);

                if (risposta) {
                    try {
                        jsonObject.put("grant_type", "password");
                        jsonObject.put("client_id", getString(R.string.client_id));
                        jsonObject.put("client_secret",getString(R.string.client_secret));
                        jsonObject.put("username", email.getText());
                        jsonObject.put("password", pwd.getText());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    requestQueue.add(Connessione.sendPost(null, "oauth/token", jsonObject, RispostaLogin));

                } else {
                    String e = errori.get(R.id.email_login)!= null? errori.get(R.id.email_login):"";
                    String p = errori.get(R.id.pwd_login) != null?errori.get(R.id.pwd_login):"";

                    if (e.length()> 0){
                        email.setText("");
                        email.setHint(e);
                    }

                    if (p.length()> 0){
                        pwd.setText("");
                        pwd.setHint(p);
                    }

                    Snackbar.make(findViewById(R.id.login_layout), "Formato credenziali errato", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    protected boolean validator(Map<Integer, String> map, EditText email, EditText pwd) {
        map.clear();
        boolean risposta = true;

        if (email.getText().length() == 0) {
            map.put(email.getId(), "Inserisci la mail");
            risposta = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                map.put(email.getId(), "Formato mail non corretto");
                risposta = false;
            }
        }

        if (pwd.length() < 6) {
            map.put(pwd.getId(),"Inserisci una password corretta");
            risposta = false;
        }

        return risposta;
    }
}
