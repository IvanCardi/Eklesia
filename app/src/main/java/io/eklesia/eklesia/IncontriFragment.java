package io.eklesia.eklesia;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncontriFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public IncontriFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_incontri, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_incontri);
        ElementiRiga [] elementi = new ElementiRiga[15];
        elementi[0] = new ElementiRiga(R.drawable.ic_account_circle_black_24dp, "Profilo");
        elementi[1] = new ElementiRiga(R.drawable.ic_saved_content, "Elementi salvati");
        elementi[2] = new ElementiRiga(R.drawable.ic_add_black_24dp, "Aggiungi la tua chiesa");
        elementi[3] = new ElementiRiga(R.drawable.ic_options_black_24dp, "Opzioni");
        elementi[4] = new ElementiRiga(R.drawable.ic_help_black_24dp, "Help");
        elementi[5] = new ElementiRiga(R.drawable.ic_account_circle_black_24dp, "Profilo");
        elementi[6] = new ElementiRiga(R.drawable.ic_saved_content, "Elementi salvati");
        elementi[7] = new ElementiRiga(R.drawable.ic_add_black_24dp, "Aggiungi la tua chiesa");
        elementi[8] = new ElementiRiga(R.drawable.ic_options_black_24dp, "Opzioni");
        elementi[9] = new ElementiRiga(R.drawable.ic_help_black_24dp, "Help");
        elementi[10] = new ElementiRiga(R.drawable.ic_account_circle_black_24dp, "Profilo");
        elementi[11] = new ElementiRiga(R.drawable.ic_saved_content, "Elementi salvati");
        elementi[12] = new ElementiRiga(R.drawable.ic_add_black_24dp, "Aggiungi la tua chiesa");
        elementi[13] = new ElementiRiga(R.drawable.ic_options_black_24dp, "Opzioni");
        elementi[14] = new ElementiRiga(R.drawable.ic_help_black_24dp, "Help");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new OpzioniAdapter(elementi);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
