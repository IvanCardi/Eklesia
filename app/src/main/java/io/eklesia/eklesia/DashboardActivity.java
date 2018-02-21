package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        final SharedPreferences sp_utente=getApplicationContext().getSharedPreferences("utente_" + Utente.getIdUtente() , Context.MODE_PRIVATE);
        int primo_accesso = Integer.parseInt(sp_utente.getString("primo_accesso", "0"));

        if (primo_accesso == 0){
            Intent i = new Intent(DashboardActivity.this, ScegliChiesaPrimoAccessoActivity.class);
            startActivity(i);
        }

        Button logout = (Button) findViewById(R.id.logout_dashboard);

        final SharedPreferences sp_connection=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor_connection = sp_connection.edit();



        final CallbackFunction cbf_logout = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException {
                editor_connection.putString("a_token", null);
                editor_connection.putString("r_token", null);
                editor_connection.commit();
                Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {
                Snackbar.make(findViewById(R.id.dashboard_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
            }
        };


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> map= new HashMap<>();
                map.put("a_token",sp_connection.getString("a_token",""));

                RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
                requestQueue.add(Connessione.sendPost(map,"api/utente/logout",null, cbf_logout));
            }
        });
    }
}
