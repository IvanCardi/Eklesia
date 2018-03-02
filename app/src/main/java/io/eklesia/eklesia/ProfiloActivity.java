package io.eklesia.eklesia;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfiloActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_profilo);
        setSupportActionBar(myToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();

            final View decor = getWindow().getDecorView();
            decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    decor.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    startPostponedEnterTransition();
                    return true;
                }
            });
            Transition slide = new Slide(Gravity.BOTTOM);
            getWindow().setEnterTransition(new Slide());
        }
        ImageView pulsanteChiudi=(ImageView) findViewById(R.id.chiudi_activity_profilo_image_view);
        pulsanteChiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
        ElementiRiga [] elementi = new ElementiRiga[6];
        elementi[0] = new ElementiRiga(R.drawable.ic_account_circle_black_24dp, "Profilo");
        elementi[1] = new ElementiRiga(R.drawable.ic_saved_content, "Elementi salvati");
        elementi[2] = new ElementiRiga(R.drawable.ic_add_black_24dp, "Aggiungi la tua chiesa");
        elementi[3] = new ElementiRiga(R.drawable.ic_options_black_24dp, "Opzioni");
        elementi[4] = new ElementiRiga(R.drawable.ic_help_black_24dp, "Help");
        elementi[5] = new ElementiRiga(R.drawable.ic_logout_black_24dp, "Logout");

        recyclerView = (RecyclerView) findViewById(R.id.lista_opzioni);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ProfiloActivity.this);
        adapter = new OpzioniAdapter(elementi);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        final SharedPreferences sp_connection=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //supportFinishAfterTransition();
    }
}
