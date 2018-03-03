package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private Toast toast = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        final SharedPreferences sp_connection=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        TextView saluto = (TextView) findViewById(R.id.saluto_dashboard);
        ImageView profilo = (ImageView) findViewById(R.id.profilo_dashboard);
        ImageView chiesa = (ImageView) findViewById(R.id.chiesa_appartenenza_dashboard);

        chiesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ChiesaActivity.class);
                startActivity(i);
            }
        });
        saluto.setText("Ciao, " + Utente.getNome() + "!");

        profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ProfiloActivity.class);
                View statusBar = findViewById(android.R.id.statusBarBackground);
                View navigationBar = findViewById(android.R.id.navigationBarBackground);

                List<Pair<View, String>> pairs = new ArrayList<>();
                if (statusBar != null) {
                    pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                }
                if (navigationBar != null) {
                    pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
                }

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(DashboardActivity.this,pairs.toArray(new Pair[pairs.size()]));
                startActivity(i, options.toBundle());
            }
        });
    }


    //Funzioni per gestire la chiusura dell'app dopo due back click
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast( getString(R.string.esci_applicazione_secondo_click));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1500);
    }

    @Override
    protected void onPause() {
        killToast();
        super.onPause();
    }

    private void showToast(String message) {
        if (this.toast == null) {
            // Create toast if found null, it would he the case of first call only
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else if (this.toast.getView() == null) {
            // Toast not showing, so create new one
            this.toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        } else {
            // Updating toast message is showing
            this.toast.setText(message);
        }

        // Showing toast finally
        this.toast.show();
    }
    private void killToast() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }
}
