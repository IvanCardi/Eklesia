package io.eklesia.eklesia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent rispostaIntent = getIntent();
        email = rispostaIntent.getStringExtra("email");
        password = rispostaIntent.getStringExtra("password");

        /*final TextInputLayout nomeTextInput = (TextInputLayout) findViewById(R.id.nome_text_input_register);
        final TextInputLayout cognomeTextInput = (TextInputLayout) findViewById(R.id.cognome_text_input_register);*/
        final TextInputLayout confermaPasswordTextInput = (TextInputLayout) findViewById(R.id.conferma_password_text_input_register);
        /*final TextInputLayout dataNascitaTextInput = (TextInputLayout) findViewById(R.id.data_nascita_text_input_register);
        final TextInputEditText nome = (TextInputEditText) findViewById(R.id.nome_edit_text_register);
        final TextInputEditText cognome = (TextInputEditText) findViewById(R.id.cognome_edit_text_register);*/
        final TextInputEditText conferma_pwd = (TextInputEditText) findViewById(R.id.conferma_password_edit_text_register);
        /*final TextInputEditText data_nascita = (TextInputEditText) findViewById(R.id.data_nascita_edit_text_register);
        final TextView sesso_label = (TextView) findViewById(R.id.sesso_label_register);
        final TextView sesso_message = (TextView) findViewById(R.id.sesso_message_register);
        final RadioGroup sesso = (RadioGroup) findViewById(R.id.sesso_register);
        final RadioButton femmina = (RadioButton) findViewById(R.id.f_register);*/
        final TextInputEditText emailEditText = (TextInputEditText) findViewById(R.id.email_edit_text_register);
        final TextInputEditText pwdEditText = (TextInputEditText) findViewById(R.id.password_edit_text_register);
        final TextInputLayout emailTextInput = (TextInputLayout) findViewById(R.id.email_text_input_register);
        final TextInputLayout passwordTextInput = (TextInputLayout) findViewById(R.id.password_text_input_register);
        emailEditText.setText(email);
        pwdEditText.setText(password);
        Button conferma = (Button) findViewById(R.id.continua_register);

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
            getWindow().setAllowEnterTransitionOverlap(false);
            getWindow().setAllowReturnTransitionOverlap(false);
            Slide slide=new Slide(Gravity.LEFT);
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Slide(Gravity.LEFT));
        }

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
                JSONObject jObj = risposta.getJSONObject("errors");
                JSONArray jsonArray = jObj.getJSONArray("email");
                Snackbar.make(findViewById(R.id.register_layout), jsonArray.getString(0), Snackbar.LENGTH_LONG).show();
            }
        };


        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.validation(errori, null, null, emailEditText, pwdEditText, conferma_pwd, null)) {

                    CallbackFunction controlloEmail = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException, ParseException {
                            Pair<View, String> p4, p5;
                            p4 = null;
                            p5 = null;
                            View statusBar = findViewById(android.R.id.statusBarBackground);
                            View navigationBar = findViewById(android.R.id.navigationBarBackground);

                            if (statusBar != null) {
                                p4 = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
                            }
                            if (navigationBar != null) {
                                p5 = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
                            }

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(RegisterActivity.this, p4, p5);

                            Intent i = new Intent(RegisterActivity.this, RegisterActivity2.class);
                            i.putExtra("email", emailEditText.getText().toString());
                            i.putExtra("password", pwdEditText.getText().toString());
                            i.putExtra("conferma_password", conferma_pwd.getText().toString());

                            startActivity(i,options.toBundle());
                            //overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        }

                        @Override
                        public void onError(JSONObject risposta) throws JSONException {
                            Snackbar.make(findViewById(R.id.register_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
                        }


                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    requestQueue.add(Connessione.sendGet(null, "api/mail/verification/" + emailEditText.getText().toString(), controlloEmail));
                    /*try {
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
                    }*/

                } else {

                    String e = errori.get(R.id.email_edit_text_register) != null ? errori.get(R.id.email_edit_text_register) : "";
                    String p = errori.get(R.id.password_edit_text_register) != null ? errori.get(R.id.password_edit_text_register) : "";
                    String cp = errori.get(R.id.conferma_password_edit_text_register) != null ? errori.get(R.id.conferma_password_edit_text_register) : "";

                    if (e.length() > 0) {
                        emailTextInput.setError(e);
                    }

                    if (p.length() > 0) {
                        passwordTextInput.setError(p);

                    }

                    if (cp.length() > 0) {
                        confermaPasswordTextInput.setError(cp);
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
    }

    @Override
    public void onBackPressed() {
        //finish();
        supportFinishAfterTransition();
    }

    /*public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment((TextInputEditText) findViewById(R.id.data_nascita_edit_text_register));
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }*/


}
