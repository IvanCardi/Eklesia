package io.eklesia.eklesia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        final EditText nome = (EditText) findViewById(R.id.nome);
        final EditText cognome = (EditText) findViewById(R.id.cognome);
        final EditText email = (EditText) findViewById(R.id.email_reg);
        final EditText pwd = (EditText) findViewById(R.id.pwd);
        final EditText data_nascita = (EditText) findViewById(R.id.data_nascita);
        final EditText sesso = (EditText) findViewById(R.id.sesso);
        //final RadioGroup sesso = (RadioGroup) findViewById(R.id.sesso);
        ImageButton add_photo = (ImageButton) findViewById(R.id.add_photo);
        final ImageView pic = (ImageView) findViewById(R.id.pic);
        Button conferma_reg = (Button) findViewById(R.id.conferma_reg);

        final SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        final JSONObject jsonObject = new JSONObject();
        final String client_id = "3";
        final String client_secret = "PgAXIt0XZFe32G7BbJKOKWEUriZd720rj2AXJ19Q";

        final CallbackFunction cbf = new CallbackFunction() {
            @Override
            public void onResponse(JSONObject risposta) throws JSONException {
                Snackbar.make((LinearLayout) findViewById(R.id.layout), "OK", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String risposta) throws JSONException {
                Toast.makeText(RegActivity.this, risposta, Toast.LENGTH_LONG).show();
            }
        };

        add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pic.setImageResource(R.drawable.photo);
            }
        });

        conferma_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    jsonObject.put("nome", nome.getText());
                    jsonObject.put("cognome", cognome.getText());
                    jsonObject.put("email", email.getText());
                    jsonObject.put("password", pwd.getText());
                    jsonObject.put("password_confirmation", pwd.getText());
                    jsonObject.put("data_nscita", data_nascita.getText());
                    jsonObject.put("sesso", sesso.getText());
                    //jsonObject.put("foto", encodeFileToBase64Binary(new File("C:\\Users\\ivanc\\AndroidStudioProjects\\Eklesia\\app\\src\\main\\res\\drawable")));

                    RequestQueue requestQueue = Volley.newRequestQueue(RegActivity.this);
                    requestQueue.add(Connessione.sendPost(sp, "api/utente", jsonObject, cbf));
                } catch (JSONException e) {
                    e.printStackTrace();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return encodedfile;
    }
}
