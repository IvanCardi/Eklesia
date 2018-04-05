package io.eklesia.eklesia.ricerca;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RicercaActivity extends AppCompatActivity {

    SharedPreferences sp_connection;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca);
        sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        Bundle data = getIntent().getExtras();
        ContenutoFiltro filtriRicerca = (ContenutoFiltro) data.getParcelable("filtriRicerca");

        CallbackFunction RispostaGetChiese = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                MiaChiesa.setInfo(risposta.getJSONObject("chiesa"));
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {

            }
        };

        Map<String, String> map = new HashMap<>();
        map.put("a_token", sp_connection.getString("a_token", ""));
        JSONObject parametri=new JSONObject();
        try {
            parametri.put("place_id",filtriRicerca.getPlaceId().equals("")?null:filtriRicerca.getPlaceId());
            parametri.put("distanza",filtriRicerca.getDistanza());
            parametri.put("denominazioni",filtriRicerca.getDenominazioni().size()==0?null:filtriRicerca.getDenominazioni());
            parametri.put("congregazioni",filtriRicerca.getCongregazioni().size()==0?null:filtriRicerca.getCongregazioni());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.add(Connessione.sendPost(map, "api/chiese",parametri, RispostaGetChiese));
    }
}
