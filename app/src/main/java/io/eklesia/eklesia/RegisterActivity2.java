package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity2 extends AppCompatActivity {
    String email;
    String password;
    String conferma_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        final TextInputLayout nomeTextInput = (TextInputLayout) findViewById(R.id.nome_text_input_register2);
        final TextInputLayout cognomeTextInput = (TextInputLayout) findViewById(R.id.cognome_text_input_register2);
        final TextInputLayout dataNascitaTextInput = (TextInputLayout) findViewById(R.id.data_text_input_register2);
        final EditText nome = (EditText) findViewById(R.id.nome_edit_text_register2);
        final EditText cognome = (EditText) findViewById(R.id.cognome_edit_text_register2);
        final EditText data_nascita = (EditText) findViewById(R.id.data_edit_text_register2);
        final RadioGroup sesso = (RadioGroup) findViewById(R.id.sesso_register2);
        Button conferma = (Button) findViewById(R.id.conferma_register);
        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

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
            /*getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setAllowReturnTransitionOverlap(false);*/
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
        }
        final Map<Integer, String> errori = new HashMap<>();

        final JSONObject jsonObjectReg = new JSONObject();
        final JSONObject jsonObjectLog = new JSONObject();


        Intent risposta = getIntent();
        email = risposta.getStringExtra("email");
        password = risposta.getStringExtra("password");
        conferma_password = risposta.getStringExtra("conferma_password");

        final CallbackFunction rispostaRegistrazione = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {

                CallbackFunction rispostaLogin = new CallbackFunction() {
                    @Override
                    public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                        SharedPreferences.Editor editor_connection = sp_connection.edit();
                        editor_connection.putString("a_token", risposta.getString("access_token"));
                        editor_connection.putString("r_token", risposta.getString("refresh_token"));
                        editor_connection.commit();

                        CallbackFunction rispostaGetInformazioni = new CallbackFunction() {
                            @Override
                            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                                Utente.setAll(risposta.getJSONObject("utente"));
                                Intent i = new Intent(RegisterActivity2.this, DashboardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addCategory(Intent.CATEGORY_HOME);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onError(JSONObject risposta) throws JSONException {

                            }
                        };

                        Map<String, String> map = new HashMap<>();
                        map.put("a_token", sp_connection.getString("a_token", ""));

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity2.this);
                        requestQueue.add(Connessione.sendGet(map, "api/utente", rispostaGetInformazioni));
                    }

                    @Override
                    public void onError(JSONObject risposta) throws JSONException {
                        Snackbar.make(findViewById(R.id.register2_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
                    }


                };

                try {
                    jsonObjectLog.put("grant_type", "password");
                    jsonObjectLog.put("client_id", getString(R.string.client_id));
                    jsonObjectLog.put("client_secret", getString(R.string.client_secret));
                    jsonObjectLog.put("username", email);
                    jsonObjectLog.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity2.this);
                requestQueue.add(Connessione.sendPost(null, "oauth/token", jsonObjectLog, rispostaLogin));

            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {
                Snackbar.make(findViewById(R.id.register2_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();

            }
        };

        data_nascita.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int scelta = sesso.getCheckedRadioButtonId();
                RadioButton sesso_scelto = (RadioButton) findViewById(scelta);

                if (Validator.validation(errori, nome, cognome, null, null, null, data_nascita)) {
                    try {
                        jsonObjectReg.put("nome", nome.getText());
                        jsonObjectReg.put("cognome", cognome.getText());
                        jsonObjectReg.put("email", email);
                        jsonObjectReg.put("password", password);
                        jsonObjectReg.put("password_confirmation", conferma_password);
                        jsonObjectReg.put("data_nascita", data_nascita.getText().toString());
                        jsonObjectReg.put("sesso", sesso_scelto.getText().equals("Maschio") ? "1" : "0");

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity2.this);
                        requestQueue.add(Connessione.sendPost(null, "api/utente", jsonObjectReg, rispostaRegistrazione));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String n = errori.get(R.id.nome_edit_text_register2) != null ? errori.get(R.id.nome_edit_text_register2) : "";
                    String c = errori.get(R.id.cognome_edit_text_register2) != null ? errori.get(R.id.cognome_edit_text_register2) : "";
                    String d = errori.get(R.id.data_edit_text_register2) != null ? errori.get(R.id.data_edit_text_register2) : "";

                    if (n.length() > 0) {
                        nomeTextInput.setError(n);
                    }

                    if (c.length() > 0) {
                        cognomeTextInput.setError(c);

                    }

                    if (d.length() > 0) {
                        dataNascitaTextInput.setError(d);
                    }
                }
            }
        });

        nome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nomeTextInput.getError() != null)
                    nomeTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cognome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cognomeTextInput.getError() != null)
                    cognomeTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        data_nascita.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (dataNascitaTextInput.getError() != null)
                    dataNascitaTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        //overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((EditText) findViewById(R.id.data_edit_text_register2));
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
