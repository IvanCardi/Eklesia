package io.eklesia.eklesia.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import io.eklesia.eklesia.CallbackFunction;
import io.eklesia.eklesia.Connessione;
import io.eklesia.eklesia.MiaChiesa;
import io.eklesia.eklesia.R;
import io.eklesia.eklesia.Utente;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        final ImageView imageView = (ImageView) getActivity().findViewById(R.id.dashboard_search_icon);
        final TextView saluto = (TextView) rootView.findViewById(R.id.saluto_dashboard);
        imageView.animate()
                .alpha(0.0f)
                .translationX(imageView.getWidth())
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        imageView.setVisibility(View.INVISIBLE);
                        imageView.setTranslationX(imageView.getWidth());
                    }
                });
        final SharedPreferences sp_connection = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

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
                    Toast.makeText(getActivity(), risposta.get("message").toString(), Toast.LENGTH_SHORT).show();
                }
            };

            Map<String, String> map = new HashMap<>();
            map.put("a_token", sp_connection.getString("a_token", ""));

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(Connessione.sendGet(map, "api/utente", getUtenteInformazioni, null));
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

        return rootView;
    }

}
