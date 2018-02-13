package io.eklesia.eklesia;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String a_token, r_token;
        a_token=sp.getString("a_token",null);
        r_token=sp.getString("r_token",null);
        if(a_token!=null&&r_token!=null)
        {
            CallbackFunction cbf = new CallbackFunction() {
                @Override
                public void onResponse() {
                    Toast.makeText(AuthActivity.this, "OK", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String risposta) {
                    Toast.makeText(AuthActivity.this, risposta, Toast.LENGTH_LONG).show();
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
            requestQueue.add(Connessione.sendGet("api/utente", cbf));
        }
        else{
            Toast.makeText(AuthActivity.this, "Non ci sono preferences", Toast.LENGTH_LONG).show();
        }



    }
}
