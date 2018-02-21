package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigurazionePrimoAccessoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_chiesa_primo_accesso);

        Button skip = (Button) findViewById(R.id.skip_scegli_chiesa_primo_accesso);



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp_utente = getApplicationContext().getSharedPreferences("utente_" + Utente.getIdUtente(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_utente = sp_utente.edit();
                editor_utente.putInt("primo_accesso", 1);
                editor_utente.commit();
                Intent i = new Intent(ConfigurazionePrimoAccessoActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });
    }
}
