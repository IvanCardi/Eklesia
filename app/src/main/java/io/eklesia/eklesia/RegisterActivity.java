package io.eklesia.eklesia;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText nome = (EditText) findViewById(R.id.nome_register);
        final EditText cognome = (EditText) findViewById(R.id.cognome_register);
        final EditText email = (EditText) findViewById(R.id.email_register);
        final EditText pwd = (EditText) findViewById(R.id.pwd_register);
        final EditText conferma_pwd = (EditText) findViewById(R.id.conferma_pwd_register);
        final EditText data_nascita = (EditText) findViewById(R.id.data_nascita_register);
        final TextView sesso_message_register = (TextView) findViewById(R.id.sesso_message_register);
        final RadioGroup sesso = (RadioGroup) findViewById(R.id.sesso_register);
        ImageButton add_photo = (ImageButton) findViewById(R.id.add_photo_register);
        final ImageView pic = (ImageView) findViewById(R.id.pic_register);
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
                Snackbar.make(findViewById(R.id.register_layout), risposta.getString("message"), Snackbar.LENGTH_LONG).show();
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
            }
        };

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic.setImageResource(R.drawable.photo);
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int scelta = sesso.getCheckedRadioButtonId();
                RadioButton sesso_scelto = (RadioButton) findViewById(scelta);

                if (validator(errori, nome, cognome, email, pwd, conferma_pwd, sesso)) {

                    try {
                        jsonObject.put("nome", nome.getText());
                        jsonObject.put("cognome", cognome.getText());
                        jsonObject.put("email", email.getText());
                        jsonObject.put("password", pwd.getText());
                        jsonObject.put("password_confirmation", conferma_pwd.getText());
                        jsonObject.put("data_nascita", data_nascita.getText());
                        jsonObject.put("sesso", sesso_scelto.getText().equals("Maschio")?"1":"0");
                        //jsonObject.put("foto", encodeFileToBase64Binary(new File("C:\\Users\\ivanc\\AndroidStudioProjects\\Eklesia\\app\\src\\main\\res\\drawable")));

                        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                        requestQueue.add(Connessione.sendPost(null, "api/utente", jsonObject, cbf));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    String n = errori.get(R.id.nome_register)!= null? errori.get(R.id.nome_register):"";
                    String c = errori.get(R.id.cognome_register) != null? errori.get(R.id.cognome_register):"";
                    String e = errori.get(R.id.email_register)!= null? errori.get(R.id.email_register):"";
                    String p = errori.get(R.id.pwd_register) != null? errori.get(R.id.pwd_register):"";
                    String cp = errori.get(R.id.conferma_pwd_register) != null? errori.get(R.id.conferma_pwd_register):"";
                    String s = errori.get(R.id.sesso_register) != null? errori.get(R.id.sesso_register):"";

                    if (n.length()> 0){
                        nome.setText("");
                        nome.setHint(n);
                    }

                    if (c.length()> 0){
                        cognome.setText("");
                        cognome.setHint(c);
                    }

                    if (e.length()> 0){
                        email.setText("");
                        email.setHint(e);
                    }

                    if (p.length()> 0){
                        pwd.setText("");
                        pwd.setHint(p);
                    }

                    if (cp.length()> 0){
                        conferma_pwd.setText("");
                        conferma_pwd.setHint(cp);
                    }

                    if (s.length() >0){
                        sesso_message_register.setText(s);
                    }

                    Snackbar.make(findViewById(R.id.register_layout), "Formato credenziali errato", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }
    private static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeToString(bytes, 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedfile;
    }

    protected boolean validator(Map<Integer, String> map, EditText nome, EditText cognome, EditText email, EditText pwd, EditText conferma_pwd, RadioGroup sesso) {
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

        if (email.getText().length() == 0) {
            map.put(email.getId(), "Inserisci la mail");
            risposta = false;
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                map.put(email.getId(), "Formato mail non corretto");
                risposta = false;
            }
        }

        if (pwd.getText().length() == 0) {
            map.put(pwd.getId(), "Inserisci la password");
            risposta = false;
        } else {
            if (pwd.length() < 6) {
                map.put(pwd.getId(), "La password deve avere almeno sei caratteri");
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
            } else  {
                if (!pwd.getText().toString().equals(conferma_pwd.getText().toString())){
                    map.put(conferma_pwd.getId(), "Le due password non coincidono");
                    risposta = false;
                }
            }
        }

        if(sesso.getCheckedRadioButtonId() == -1){
            map.put(sesso.getId(), "Seleziona il sesso");
            risposta = false;
        }

        return risposta;
    }
}
