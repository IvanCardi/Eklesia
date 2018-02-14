package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText pwd = (EditText) findViewById(R.id.password);
        final TextView reg = (TextView) findViewById(R.id.registrati);
        Button accedi = (Button) findViewById(R.id.accedi);

        final SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        final JSONObject jsonObject = new JSONObject();
        final String client_id = "3";
        final String client_secret = "PgAXIt0XZFe32G7BbJKOKWEUriZd720rj2AXJ19Q";

        final CallbackFunction cbf = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException {
                editor.putString("a_token", risposta.getString("access_token"));
                editor.putString("r_token", risposta.getString("refresh_token"));
                editor.commit();

                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(String risposta) throws JSONException {
                Toast.makeText(LoginActivity.this, risposta, Toast.LENGTH_LONG).show();
            }
        };

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonObject.put("grant_type", "password");
                    jsonObject.put("client_id", client_id);
                    jsonObject.put("client_secret", client_secret);
                    jsonObject.put("username", email.getText());
                    jsonObject.put("password", pwd.getText());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(Connessione.sendPost(sp, "oauth/token", jsonObject, cbf));
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(i);
            }
        });


    }
}
