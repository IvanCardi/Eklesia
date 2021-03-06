package io.eklesia.eklesia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        final SharedPreferences sp_connection = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        final CallbackFunction RispostaGetChiesa = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                MiaChiesa.setInfo(risposta.getJSONObject("chiesa"));
            }

            @Override
            public void onError(JSONObject risposta) throws JSONException {

            }
        };


        final JSONObject jsonObject = new JSONObject();

        final Map<Integer, String> errori = new HashMap<>();

        accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Validator.validation(errori, null, null, emailEditText, pwdEditText, null, null)) {
                    try {
                        jsonObject.put("grant_type", "password");
                        jsonObject.put("client_id", getString(R.string.client_id));
                        jsonObject.put("client_secret", getString(R.string.client_secret));
                        jsonObject.put("username", emailEditText.getText());
                        jsonObject.put("password", pwdEditText.getText());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CallbackFunction RispostaLogin = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                            SharedPreferences.Editor editor_connection = sp_connection.edit();
                            editor_connection.putString("a_token", risposta.getString("access_token"));
                            editor_connection.putString("r_token", risposta.getString("refresh_token"));
                            editor_connection.commit();
                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();

                        }

                        @Override
                        public void onError(JSONObject risposta) throws JSONException {
                            Snackbar.make(findViewById(R.id.login_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
                        }
                    };

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
                /*if (!Validator.validation(errori, null, null, null, emailEditText, pwdEditText,null,null)) {
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
                }*/
                TextView tv = (TextView) findViewById(R.id.titolo);
                Pair<View, String> p1, p2, p3, p4, p5;
                p4 = null;
                p5 = null;
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                i.putExtra("email", emailEditText.getText().toString());
                i.putExtra("password", pwdEditText.getText().toString());
                p1 = Pair.create((View) emailTextInput, "emailTextInput");
                p2 = Pair.create(findViewById(R.id.password_text_input_login), "passwordTextInput");
                p3 = Pair.create(findViewById(R.id.accedi_login), "pulsante");
                View statusBar = findViewById(android.R.id.statusBarBackground);
                View navigationBar = findViewById(android.R.id.navigationBarBackground);

                if (statusBar != null) {
                    p4 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                }
                if (navigationBar != null) {
                    p5 = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                }
                //Pair<View, String> p3 = Pair.create(findViewById(R.id.titolo), "titolo");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(LoginActivity.this, p1, p2, p3, p4, p5);
                startActivity(i, options.toBundle());
            }
        });
    }


}
