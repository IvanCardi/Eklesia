package io.eklesia.eklesia.ricerca;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.eklesia.eklesia.CallbackFunction;
import io.eklesia.eklesia.Congregazione;
import io.eklesia.eklesia.Connessione;
import io.eklesia.eklesia.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Santa Caterina on 21/03/2018.
 */

public class CercaChieseFragment extends AAH_FabulousFragment implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    ContenutoFiltro mContenutoFiltro;
    ArrayMap<String, List<String>> filtriApplicati = new ArrayMap<>();
    TabLayout tabs_types;
    ImageButton applicaButton;
    ImageButton refreshButton;
    DiscreteSeekBar seekBar;
    SezioniPageAdapter mAdapter;
    AutoCompleteTextView posizioneET;
    List<TextView> textviews = new ArrayList<>();
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceArrayAdapter.PlaceAutocomplete postoSelezionato;

    public static CercaChieseFragment newInstance() {
        CercaChieseFragment f = new CercaChieseFragment();
        return f;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.search_filter_view, null);

        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);
        ViewPager vp_types = (ViewPager) contentView.findViewById(R.id.vp_types);
        tabs_types = (TabLayout) contentView.findViewById(R.id.tabs_types);
        refreshButton = (ImageButton) contentView.findViewById(R.id.imgbtn_refresh);
        applicaButton = (ImageButton) contentView.findViewById(R.id.imgbtn_apply);
        applicaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter(mContenutoFiltro);
            }
        });
        mContenutoFiltro=new ContenutoFiltro();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv : textviews) {
                    tv.setTag("unselected");
                    tv.setBackgroundResource(R.drawable.chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                }

                filtriApplicati.clear();
                posizioneET.setText("");
                seekBar.setProgress(2);
                mContenutoFiltro.clear();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();
        mAdapter = new SezioniPageAdapter();
        vp_types.setOffscreenPageLimit(3);
        vp_types.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        tabs_types.setupWithViewPager(vp_types);


        //params to set
        setAnimationDuration(300); //optional; default 500ms
        setPeekHeight(300);//optional; to get back result
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        setViewPager(vp_types); //optional; if you use viewpager that has scrollview
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    public class SezioniPageAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = null;
            FlexboxLayout fbl;

            switch (position) {
                case 0:
                    layout = (ViewGroup) inflater.inflate(R.layout.ricerca_posizione_layout, collection, false);
                    Button posizione = (Button) layout.findViewById(R.id.ricerca_posizione_posizione_button);
                    seekBar = (DiscreteSeekBar) layout.findViewById(R.id.ricerca_posizione_seekbar);
                    final TextView kmTextView = (TextView) layout.findViewById(R.id.ricerca_posizione_distanza_scelta_textView);
                    seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                        @Override
                        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                            kmTextView.setText("0-" + value + "km");
                            mContenutoFiltro.setDistanza(value);
                        }

                        @Override
                        public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

                        }
                    });
                    posizioneET = (AutoCompleteTextView) layout.findViewById(R.id.ricerca_posizione_posizione_editText);

                    posizioneET.setOnItemClickListener(mAutocompleteClickListener);
                    mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, null, null);
                    posizioneET.setAdapter(mPlaceArrayAdapter);


                    posizione.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                            Criteria criteria = new Criteria();
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                return;
                            }
                            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (addresses.size() > 0) {
                                    String cityname = addresses.get(0).getLocality().toString();
                                    posizioneET.setText(addresses.get(0).getThoroughfare() + ", " + addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //inflateLayoutWithFilters("genre", fbl);
                    break;
                case 1:

                    layout = (ViewGroup) inflater.inflate(R.layout.ricerca_denominazioni_layout, collection, false);
                    fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
                    inflateLayoutWithFilters("denominazioni", fbl);
                    break;
                case 2:
                    layout = (ViewGroup) inflater.inflate(R.layout.ricerca_denominazioni_layout, collection, false);
                    fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
                    inflateLayoutWithFilters("congregazioni", fbl);
                    break;
            }
            collection.addView(layout);
            return layout;

        }

        private AdapterView.OnItemClickListener mAutocompleteClickListener
                = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                postoSelezionato = item;
                mContenutoFiltro.setPlaceId(item.placeId.toString());
            }
        };

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "POSIZIONE";
                case 1:
                    return "DENOMINAZIONE";
                case 2:
                    return "CONGREGAZIONE";

            }
            return "";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private void inflateLayoutWithFilters(final String filter_category, FlexboxLayout fbl) {
        final List<Congregazione> congregazioni = new ArrayList<>();
        String uri = "";
        switch (filter_category) {
            case "denominazioni":
                uri = "api/denominazioni";
                break;
            case "congregazioni":
                uri = "api/congregazioni";
                break;
        }
        Map<String, String> map = new HashMap<>();
        SharedPreferences sp_connection = getContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        final FlexboxLayout finalFbl = fbl;
        CallbackFunction getCongregazioni = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                JSONArray jsonArray = risposta.getJSONArray(filter_category);
                for (int l = 0; l < jsonArray.length(); l++) {
                    congregazioni.add(new Congregazione(jsonArray.getJSONObject(l)));
                }

                for (int i = 0; i < congregazioni.size(); i++) {
                    View subchild = getActivity().getLayoutInflater().inflate(R.layout.single_chip, null);
                    final TextView tv = ((TextView) subchild.findViewById(R.id.txt_title));
                    tv.setText(congregazioni.get(i).getNome());
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tv.getTag() != null && tv.getTag().equals("selected")) {
                                tv.setTag("unselected");
                                tv.setBackgroundResource(R.drawable.chip_unselected);
                                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                                removeFromSelectedMap(filter_category, congregazioni.get(finalI).getId());
                            } else {
                                tv.setTag("selected");
                                tv.setBackgroundResource(R.drawable.chip_selected);
                                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_header));
                                addToSelectedMap(filter_category, congregazioni.get(finalI).getId());
                            }
                        }
                    });


                    if (filtriApplicati != null && filtriApplicati.get(filter_category) != null && filtriApplicati.get(filter_category).contains(congregazioni.get(finalI).getNome())) {
                        tv.setTag("selected");
                        tv.setBackgroundResource(R.drawable.chip_selected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_header));
                    } else {
                        tv.setBackgroundResource(R.drawable.chip_unselected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filters_chips));
                    }
                    textviews.add(tv);

                    finalFbl.addView(subchild);
                }
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {
                Toast.makeText(getActivity(), risposta.get("message").toString(), Toast.LENGTH_SHORT).show();
            }
        };
        map.put("a_token", sp_connection.getString("a_token", ""));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(Connessione.sendGet(map, uri, getCongregazioni, null));


    }

    private void addToSelectedMap(String key, int value) {
        if(key.equals("congregazioni")){
            mContenutoFiltro.addCongregazione(value);
        }else{
            mContenutoFiltro.addDenominazione(value);
        }
    }

    private void removeFromSelectedMap(String key, int value) {
        if(key.equals("congregazioni")){
            mContenutoFiltro.removeCongregazione(value);
        }else{
            mContenutoFiltro.removeDenominazione(value);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        /*Toast.makeText(getActivity(),
                "Google Places API connected.",
                Toast.LENGTH_LONG).show();*/

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        /*Toast.makeText(getActivity(),
                "Google Places API connection suspended.",
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        /*Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/

    }

}
