package io.eklesia.eklesia.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;

import io.eklesia.eklesia.AuthActivity;
import io.eklesia.eklesia.CallbackFunction;
import io.eklesia.eklesia.Connessione;
import io.eklesia.eklesia.LoginActivity;
import io.eklesia.eklesia.MiaChiesa;
import io.eklesia.eklesia.R;
import io.eklesia.eklesia.ricerca.CercaChieseFragment;
import io.eklesia.eklesia.ricerca.ContenutoFiltro;
import io.eklesia.eklesia.ricerca.RicercaActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChieseFragment extends Fragment implements AAH_FabulousFragment.Callbacks  {

    SharedPreferences sp_connection;
    RequestQueue requestQueue;
    CercaChieseFragment mChieseFragment;
    public ChieseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sp_connection = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(getActivity());
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_chiese,container,false);
        ImageView imageView= (ImageView) getActivity().findViewById(R.id.dashboard_search_icon);

        if(imageView.getVisibility()!=View.VISIBLE){
            imageView.animate()
                    .alpha(1.0f)
                    .translationX(0)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
            imageView.setVisibility(View.VISIBLE);
        }

        final FloatingActionButton actionButton=(FloatingActionButton)rootView.findViewById(R.id.fab);
        mChieseFragment=CercaChieseFragment.newInstance();
        mChieseFragment.setCallbacks(this);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mChieseFragment.setParentFab(actionButton);
                mChieseFragment.show(getActivity().getSupportFragmentManager(),mChieseFragment.getTag());

            }
        });


        return rootView;
    }

    @Override
    public void onResult(Object result) {
        Log.d("k9res", "onResult: " + result.toString());
        if (result.toString().equalsIgnoreCase("swiped_down")) {
            //do something or nothing
        } else {
            ContenutoFiltro risposta=(ContenutoFiltro) result;
            Toast.makeText(getActivity(),Integer.toString(risposta.getDistanza()),Toast.LENGTH_LONG).show();
            Intent i = new Intent(getActivity(), RicercaActivity.class);
            i.putExtra("filtriRicerca",risposta);
            startActivity(i);


        }
    }

}
