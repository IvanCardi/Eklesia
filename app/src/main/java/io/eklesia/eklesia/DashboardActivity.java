package io.eklesia.eklesia;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    boolean doubleBackToExitPressedOnce = false;
    private Toast toast = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 80;
    private TextView ricerca;
    private LinearLayout contenitoreRicerca;
    private int mMaxScrollSize;

    private boolean isFirst = true;


    private int initialWidth;
    private int finalWidth;
    private int diff;
    private int diffRelative;
    private double distanzaIniziale;
    double costante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.appbar);
        final TextView saluto = (TextView) findViewById(R.id.saluto_dashboard);
        ImageView profilo = (ImageView) findViewById(R.id.profilo_dashboard);
        ImageView chiesa = (ImageView) findViewById(R.id.chiesa_appartenenza_dashboard);
        ricerca = (TextView) findViewById(R.id.ricerca_dashboard);
        contenitoreRicerca = (LinearLayout) findViewById(R.id.ricerca);
        appbarLayout.addOnOffsetChangedListener(this);

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Chiese", R.drawable.ic_options_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Prediche", R.drawable.ic_help_black_24dp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Eventi", R.drawable.ic_account_circle_black_24dp);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setDefaultBackgroundColor(Color.WHITE);
        bottomNavigation.setAccentColor(fetchColor(R.color.colorPrimary));
        bottomNavigation.setInactiveColor(fetchColor(R.color.colorPrimaryLight));
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        /*Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        contenitoreRicerca.getLayoutParams().width = size.x - 148;
        finalWidth = size.x;
        initialWidth = size.x - 148;
        diff = finalWidth - initialWidth;
        diffRelative = diff;*/


        if (!Utente.isSet()) {
            CallbackFunction getUtenteInformazioni = new CallbackFunction() {
                @Override
                public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                    Utente.setAll(risposta.getJSONObject("utente"));
                    saluto.setText("Ciao, " + Utente.getNome() + "!");
                    saluto.setVisibility(View.VISIBLE);

                    saluto.setAlpha(0.0f);
                    saluto.animate()
                            .alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);

                                }
                            });
                    if (!risposta.isNull("chiesa")) {
                        MiaChiesa.setInfo(risposta.getJSONObject("chiesa"));
                    }
                }

                @Override
                public void onError(JSONObject risposta) throws JSONException {
                    Toast.makeText(DashboardActivity.this, risposta.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            };

            Map<String, String> map = new HashMap<>();
            map.put("a_token", sp_connection.getString("a_token", ""));

            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            requestQueue.add(Connessione.sendGet(map, "api/utente", getUtenteInformazioni));
        } else {
            saluto.setText("Ciao, " + Utente.getNome() + "!");
            saluto.setVisibility(View.VISIBLE);

            saluto.setAlpha(0.0f);
            saluto.animate()
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
        }


        /*ricerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, RicercaActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(DashboardActivity.this, contenitoreRicerca, "barra_ricerca");
                startActivity(i, options.toBundle());
            }
        });*/
        chiesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ChiesaActivity.class);
                startActivity(i);
            }
        });

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
                        makeSceneTransitionAnimation(DashboardActivity.this, pairs.toArray(new Pair[pairs.size()]));
                startActivity(i, options.toBundle());
            }
        });
    }
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }


    //Funzioni per gestire la chiusura dell'app dopo due back click
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Utente.clear();
            MiaChiesa.clear();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.esci_applicazione_secondo_click));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        /*if (isFirst == true) {
            distanzaIniziale = appBarLayout.getTotalScrollRange() * 0.2;
            costante = diff / distanzaIniziale;
            isFirst = false;

        }
        if (contenitoreRicerca.getWidth() != initialWidth) {
            ViewGroup.LayoutParams params = contenitoreRicerca.getLayoutParams();
            params.width = initialWidth;
            contenitoreRicerca.setLayoutParams(params);
            contenitoreRicerca.setElevation(4);
            appBarLayout.setBackgroundColor(Color.TRANSPARENT);
            appBarLayout.setElevation(0);
        }
        int total = appBarLayout.getTotalScrollRange();
        float percentuale = (float) -i / (float) total * 100;
        if (percentuale >= 80F) {
            int distanza = total + i;
            double offset = ((int) distanzaIniziale - distanza);
            double cost = initialWidth + offset * costante;
            double x = cost / initialWidth;

            contenitoreRicerca.setElevation(4);
            appBarLayout.setBackgroundColor(Color.TRANSPARENT);
            appBarLayout.setElevation(0);


            //float in =(float)finalWidth/(float)initialWidth;
            ViewGroup.LayoutParams params = contenitoreRicerca.getLayoutParams();
            params.width = (int) cost;
            if (percentuale >= 99.99F) {
                contenitoreRicerca.setElevation(0);
                appBarLayout.setBackgroundColor(Color.WHITE);
                appBarLayout.setElevation(4);
            }
            contenitoreRicerca.setLayoutParams(params);


        }*/

    }
}
