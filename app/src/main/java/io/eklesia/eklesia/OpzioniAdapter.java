package io.eklesia.eklesia;

/**
 * Created by ivanc on 02/03/2018.
 */

import android.graphics.Path;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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

    public OpzioniAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.riga_opzione, parent, false);
        ViewHolder vh = new ViewHolder(l);
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
