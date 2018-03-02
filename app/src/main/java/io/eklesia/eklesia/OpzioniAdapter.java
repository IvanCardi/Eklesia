package io.eklesia.eklesia;

/**
 * Created by ivanc on 02/03/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.net.sip.SipSession;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OpzioniAdapter extends RecyclerView.Adapter<OpzioniAdapter.ViewHolder> {

    private ElementiRiga[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView icona;
        public TextView opzione;

        public ViewHolder(View v) {
            super(v);
            this.icona = v.findViewById(R.id.icona_riga);
            this.opzione = v.findViewById(R.id.testo_riga);
        }


    }

    public OpzioniAdapter(ElementiRiga[] myDataset) {
        mDataset = myDataset;
    }

    public OpzioniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        final LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.riga_opzione, parent, false);
        final ViewHolder vh = new ViewHolder(l);

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vh.opzione.getText().toString().equals("Logout")) {
                    final SharedPreferences sp_connection = l.getContext().getSharedPreferences(l.getContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    final CallbackFunction cbf_logout = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException {

                            SharedPreferences.Editor editor_connection = sp_connection.edit();
                            editor_connection.clear();
                            editor_connection.commit();
                            Intent i = new Intent(l.getContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addCategory(Intent.CATEGORY_HOME);
                            l.getContext().startActivity(i);
                        }

                        @Override
                        public void onError(JSONObject risposta) throws JSONException {
                            Toast.makeText(l.getContext(), "Logout non riuscito", Toast.LENGTH_LONG).show();
                        }
                    };
                    Map<String, String> map = new HashMap<>();
                    map.put("a_token", sp_connection.getString("a_token", ""));

                    RequestQueue requestQueue = Volley.newRequestQueue(l.getContext());
                    requestQueue.add(Connessione.sendPost(map, "api/utente/logout", null, cbf_logout));
                }
            }
        });
        return vh;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.opzione.setText(mDataset[position].getTesto());
        holder.icona.setImageResource(mDataset[position].getIcona());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}
