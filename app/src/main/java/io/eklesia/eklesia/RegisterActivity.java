package io.eklesia.eklesia;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent rispostaIntent=getIntent();
        email = rispostaIntent.getStringExtra("email");
        password = rispostaIntent.getStringExtra("password");

        final TextInputLayout nomeTextInput = (TextInputLayout) findViewById(R.id.nome_text_input_register);
        final TextInputLayout cognomeTextInput = (TextInputLayout) findViewById(R.id.cognome_text_input_register);
        final TextInputLayout confermaPasswordTextInput = (TextInputLayout) findViewById(R.id.conferma_password_text_input_register);
        final TextInputLayout dataNascitaTextInput = (TextInputLayout) findViewById(R.id.data_nascita_text_input_register);
        final TextInputEditText nome = (TextInputEditText) findViewById(R.id.nome_edit_text_register);
        final TextInputEditText cognome = (TextInputEditText) findViewById(R.id.cognome_edit_text_register);
        final TextInputEditText conferma_pwd = (TextInputEditText) findViewById(R.id.conferma_password_edit_text_register);
        final TextInputEditText data_nascita = (TextInputEditText) findViewById(R.id.data_nascita_edit_text_register);
        final TextView sesso_label = (TextView) findViewById(R.id.sesso_label_register);
        final TextView sesso_message = (TextView) findViewById(R.id.sesso_message_register);
        final RadioGroup sesso = (RadioGroup) findViewById(R.id.sesso_register);
        final RadioButton femmina = (RadioButton) findViewById(R.id.f_register);

        Button conferma = (Button) findViewById(R.id.conferma_register);

        final Map<Integer, String> errori = new HashMap<>();

        final JSONObject jsonObject = new JSONObject();

        final CallbackFunction cbf = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {

               /* JSONObject jObj= risposta.getJSONObject("errors");
                  for(int i = 0; i< jsonArray.length(); i++){
                    JSONObject jsObj = jsonArray.getJSONObject(i);
                    switch (jsObj.keys().next()){
                        case "nome":
                            nome.setText("");
                            nome.setHint(jsObj.getString("nome"));
                            break;
                        case "cognome":
                            nome.setText("");
                            nome.setHint(jsObj.getString("cognome"));
                            break;
                        case "data_nascita":
                            nome.setText("");
                            nome.setHint(jsObj.getString("data_nascita"));
                            break;
                        case "email":
                            nome.setText("");
                            nome.setHint(jsObj.getString("email"));
                            break;
                        case "password":
                            nome.setText("");
                            nome.setHint(jsObj.getString("password"));
                            break;
                        default:
                            break;
                    }
                }*/
                JSONObject jObj= risposta.getJSONObject("errors");
                JSONArray jsonArray= jObj.getJSONArray("email");
                Snackbar.make(findViewById(R.id.register_layout), jsonArray.getString(0), Snackbar.LENGTH_LONG).show();
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

                if (validator(errori, nome, cognome,conferma_pwd,data_nascita)) {

                    try {
                        jsonObject.put("nome", nome.getText());
                        jsonObject.put("cognome", cognome.getText());
                        jsonObject.put("email", email);
                        jsonObject.put("password", password);
                        jsonObject.put("password_confirmation", conferma_pwd.getText());
                        jsonObject.put("data_nascita", data_nascita.getText().toString());
                        jsonObject.put("sesso", sesso_scelto.getText().equals("Maschio") ? "1" : "0");
                        //jsonObject.put("foto", encodeFileToBase64Binary(new File("C:\\Users\\ivanc\\AndroidStudioProjects\\Eklesia\\app\\src\\main\\res\\drawable")));

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        requestQueue.add(Connessione.sendPost(null, "api/utente", jsonObject, cbf));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    String n = errori.get(R.id.nome_edit_text_register) != null ? errori.get(R.id.nome_edit_text_register) : "";
                    String c = errori.get(R.id.cognome_edit_text_register) != null ? errori.get(R.id.cognome_edit_text_register) : "";
                    String cp = errori.get(R.id.conferma_password_edit_text_register) != null ? errori.get(R.id.conferma_password_edit_text_register) : "";
                    String d = errori.get(R.id.data_nascita_edit_text_register) != null ? errori.get(R.id.data_nascita_edit_text_register) : "";

                    if (n.length() > 0) {
                        nomeTextInput.setError(n);
                    }

                    if (c.length() > 0) {
                        cognomeTextInput.setError(c);

                    }

                    if (cp.length() > 0) {
                        confermaPasswordTextInput.setError(cp);
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

        conferma_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (confermaPasswordTextInput.getError() != null)
                    confermaPasswordTextInput.setError("");
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

    protected boolean validator(Map<Integer, String> map, EditText nome, EditText cognome, EditText conferma_pwd, EditText data) {
        map.clear();
        boolean risposta = true;

        String name_regex = "^[\\p{L}\\s.'\\-,]+$";
        Pattern pattern = Pattern.compile(name_regex, Pattern.CASE_INSENSITIVE);

        if (nome.getText().length() == 0) {
            map.put(nome.getId(), "Inserisci il nome");
            risposta = false;
        } else {
            if (!pattern.matcher(nome.getText()).find()) {
                map.put(nome.getId(), "Formato nome non corretto");
                risposta = false;
            }
        }

        if (cognome.getText().length() == 0) {
            map.put(cognome.getId(), "Inserisci il cognome");
            risposta = false;
        } else {
            if (!pattern.matcher(cognome.getText()).find()) {
                map.put(cognome.getId(), "Formato cognome non corretto");
                risposta = false;
            }
        }
        if (conferma_pwd.getText().length() == 0) {
            map.put(conferma_pwd.getId(), "Inserisci la password");
            risposta = false;
        } else {
            if (conferma_pwd.length() < 6) {
                map.put(conferma_pwd.getId(), "La password deve avere almeno sei caratteri");
                risposta = false;
            } else {
                if (!password.equals(conferma_pwd.getText().toString())) {
                    map.put(conferma_pwd.getId(), "Le due password non coincidono");
                    risposta = false;
                }
            }
        }
        if (data.getText().length()==0){
            map.put(data.getId(), "Inserisci la data");
            risposta = false;
        }

        return risposta;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextInputEditText) findViewById(R.id.data_nascita_edit_text_register));
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
