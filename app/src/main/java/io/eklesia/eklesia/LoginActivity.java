package io.eklesia.eklesia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextInputEditText emailEditText = (TextInputEditText) findViewById(R.id.email_edit_text_login);
        final TextInputEditText pwdEditText = (TextInputEditText) findViewById(R.id.password_edit_text_login);
        final TextInputLayout emailTextInput = (TextInputLayout) findViewById(R.id.email_text_input_login);
        final TextInputLayout passwordTextInput = (TextInputLayout) findViewById(R.id.password_text_input_login);
        final ImageButton registrati = (ImageButton) findViewById(R.id.registrati_login);
        Button accedi = (Button) findViewById(R.id.accedi_login);

        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        final JSONObject jsonObject = new JSONObject();

        final Map<Integer, String> errori = new HashMap<>();

        final CallbackFunction RispostaLogin = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                SharedPreferences.Editor editor_connection = sp_connection.edit();
                editor_connection.putString("a_token", risposta.getString("access_token"));
                editor_connection.putString("r_token", risposta.getString("refresh_token"));
                editor_connection.commit();

                CallbackFunction RispostaGetInformazioni = new CallbackFunction() {
                    @Override
                    public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                        Utente.setAll(risposta.getJSONArray("utente").getJSONObject(0));
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onError(JSONObject risposta) throws JSONException {

                    }
                };

                Map<String, String> map = new HashMap<>();
                map.put("a_token", sp_connection.getString("a_token", ""));

                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(Connessione.sendGet(map, "api/utente", RispostaGetInformazioni));


            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {
                Snackbar.make(findViewById(R.id.login_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
            }
        };

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Validator.validation(errori, null, null, null, emailEditText,pwdEditText,null,null)) {
                    try {
                        jsonObject.put("grant_type", "password");
                        jsonObject.put("client_id", getString(R.string.client_id));
                        jsonObject.put("client_secret", getString(R.string.client_secret));
                        jsonObject.put("username", emailEditText.getText());
                        jsonObject.put("password", pwdEditText.getText());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    requestQueue.add(Connessione.sendPost(null, "oauth/token", jsonObject, RispostaLogin));

                } else {
                    String e = errori.get(R.id.email_edit_text_login) != null ? errori.get(R.id.email_edit_text_login) : "";
                    String p = errori.get(R.id.password_edit_text_login) != null ? errori.get(R.id.password_edit_text_login) : "";

                    if (e.length() > 0) {
                        emailTextInput.setError(e);
                    }

                    if (p.length() > 0) {
                        passwordTextInput.setError(p);
                    }

                }
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (emailTextInput.getError() != null)
                    emailTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pwdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (passwordTextInput.getError() != null)
                    passwordTextInput.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Validator.validation(errori, null, null, null, emailEditText, pwdEditText,null,null)) {
                    String e = errori.get(R.id.email_edit_text_login) != null ? errori.get(R.id.email_edit_text_login) : "";
                    String p = errori.get(R.id.password_edit_text_login) != null ? errori.get(R.id.password_edit_text_login) : "";
                    if (e.length() > 0) {
                        emailTextInput.setError(e);
                    }

                    if (p.length() > 0) {
                        passwordTextInput.setError(p);
                    }

                } else {
                    CallbackFunction rispostaControlloEmail = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                            i.putExtra("email", emailEditText.getText().toString());
                            i.putExtra("password", pwdEditText.getText().toString());
                            startActivity(i);
                        }

                        @Override
                        public void onError(JSONObject risposta) throws JSONException {
                            Snackbar.make(findViewById(R.id.login_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    requestQueue.add(Connessione.sendGet(null, "api/mail/verification/" + emailEditText.getText().toString(), rispostaControlloEmail));
                }
            }
        });
    }
}
