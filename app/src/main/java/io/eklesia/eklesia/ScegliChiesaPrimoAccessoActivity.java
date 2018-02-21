package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ScegliChiesaPrimoAccessoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scegli_chiesa_primo_accesso);

        Button skip = (Button) findViewById(R.id.skip_scegli_chiesa_primo_accesso);

        final SharedPreferences sp_utente = getApplicationContext().getSharedPreferences("utente_" + Utente.getId_utente(), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor_utente = sp_utente.edit();

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor_utente.putString("primo_accesso", "1");
                editor_utente.commit();
                Intent i = new Intent(ScegliChiesaPrimoAccessoActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });
    }
}
