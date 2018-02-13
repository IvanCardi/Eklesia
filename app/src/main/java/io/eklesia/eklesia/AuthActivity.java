package io.eklesia.eklesia;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sp !=null){
            String a_token, r_token;
            a_token=sp.getString("a_token",null);
            r_token=sp.getString("r_token",null);
            if(a_token!=null&&r_token!=null)
            {

            }
        }
        else{

        }


    }
}
