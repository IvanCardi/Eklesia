package io.eklesia.eklesia;

import android.support.design.widget.TextInputEditText;
import android.widget.EditText;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by ivanc on 28/02/2018.
 */

public class Validator {
    static boolean risposta;

    public static boolean validation(Map<Integer, String> map, EditText nome, EditText cognome, EditText email, EditText pwd, EditText conferma_pwd, EditText data) {

        map.clear();
        risposta = true;
        String name_regex = "^[\\p{L}\\s.'\\-,]+$";
        Pattern pattern = Pattern.compile(name_regex, Pattern.CASE_INSENSITIVE);

        if (email != null) {
            if (email.getText().length() == 0) {
                map.put(email.getId(), "Inserisci la mail");
                risposta = false;
            } else {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
                    map.put(email.getId(), "Formato mail non corretto");
                    risposta = false;
                }
            }
        }

        if (pwd != null) {
            if (pwd.length() < 6) {
                map.put(pwd.getId(), "Inserisci una password corretta");
                risposta = false;
            }
        }

        if (nome != null) {
            if (nome.getText().length() == 0) {
                map.put(nome.getId(), "Inserisci il nome");
                risposta = false;
            } else {
                if (!pattern.matcher(nome.getText()).find()) {
                    map.put(nome.getId(), "Formato nome non corretto");
                    risposta = false;
                }
            }
        }

        if (cognome != null) {
            if (cognome.getText().length() == 0) {
                map.put(cognome.getId(), "Inserisci il cognome");
                risposta = false;
            } else {
                if (!pattern.matcher(cognome.getText()).find()) {
                    map.put(cognome.getId(), "Formato cognome non corretto");
                    risposta = false;
                }
            }
        }

        if (conferma_pwd != null) {
            if (conferma_pwd.getText().length() == 0) {
                map.put(conferma_pwd.getId(), "Inserisci la password");
                risposta = false;
            } else {
                if (!pwd.getText().toString().equals(conferma_pwd.getText().toString())) {
                    map.put(conferma_pwd.getId(), "Le due password non coincidono");
                    risposta = false;
                }
            }
        }

        if (data != null) {
            if (data.getText().length() == 0) {
                map.put(data.getId(), "Inserisci la data");
                risposta = false;
            }
        }

        return risposta;
    }
}
