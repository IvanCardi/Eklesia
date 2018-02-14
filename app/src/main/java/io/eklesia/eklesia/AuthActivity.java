package io.eklesia.eklesia;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        final SharedPreferences sp=getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        //editor.putString("a_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImE2Mzc3Mjg2NzhmODBjN2NjYTRhYzBiODlhMGM5Y2RmZjA5OGMzNzI3OThhMmY2MDAzOWM4ZTljOTJjYmFhNTc0Yjg2MDNjMjdiYzE2YzJhIn0.eyJhdWQiOiIzIiwianRpIjoiYTYzNzcyODY3OGY4MGM3Y2NhNGFjMGI4OWEwYzljZGZmMDk4YzM3Mjc5OGEyZjYwMDM5YzhlOWM5MmNiYWE1NzRiODYwM2MyN2JjMTZjMmEiLCJpYXQiOjE1MTg2MDYxNzcsIm5iZiI6MTUxODYwNjE3NywiZXhwIjoxNTE4NjA2Mjk3LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.ECJAWOumygH3Cn_G2wGqDhnP8wIagqO2yUfg1uajH3n4xJiHKesE0KCBlONKWwr-o9knXdhkwW0cBBKKaWsa9qz_szg33HC23c6p3y19kuVLiJUmHwx50WEafbt-KTEdsnmx4w5ZJ_VclqMq4RlG5hVr8onx6gZIwh3L4d_eYlPqEY9X18bHdLF8JtHmPkQp_KmhNyDyEelgPEkAv_kj5d2aLMx-kDZybRzGPCf-zffnJqRLUthwYsSkWbbWW4lKQqYhdSk0JnaTbOmddSRg6GF04FGJIU_jTU5Thj6-_E_olyXrZ96-9gqmy5aLmE4_9CYkUVS9qBm0VXtg6pM3COzAIl9yYN9HP-Q3ZaWwj6_G-cu5MFz6kODeMuAd_0QmonRtkUodCVTa909b16o42flUOZcRPfTdebDt0rx0flEmM3F26vEBp5jCKuPnRMpbWz1Qil-smv27vIZSnP0M9qVN_ISPQpSWGBSu-bYeghd57r7QUqYhdxuu0-Tgd_v7ufCXrKnCneUb0zREXkyz9vkl_8zUOhLs-BolEJV_H3tEoVMQqIXxfgV3YWEnFz1dr837YCw1S9DDF_f3pYAvHZ3XAjpkWZUF4bvvySKsXOhm8tvkZyc8thj2-lAIdUQF8HZ2wwbwg-LokN5AabXtWaHMiXpwp_BoRVRYqM8aTlo");
        //editor.putString("r_token", "def5020059b81bdf5e00957a3502a84e97225910f4f2e348b5d00def8bb8cb3c00553e8204f871a6e902a4dec787d24acf55d55ee93fba95fe15c701fb3978ffcab50b2b8b04aa8d18518436395d9b4b6c59bea5db1af6942a2b1467ff4d1d13acbb20b7d2406aae72ada2ef347e1a4e65d0698a7db8fcf62066a0b9cde1b70034334f498114a0227e0b56719d4a55a761dd8af8a3d0504c4f99936f59cdb02a68d875a85513c1cea8e186c700b6a83dc8cd236ea518935e8e1eca8a4e33ffc4f1215f51152bb4c99b7ef9fe5d6d29a056996cac194ef3497a6be7983588852fbbea98377114ea7d3a1d33109730a92df6c5c89d7355145b349b25e81c0bf982045156991d5abec1367248f83a64467ce5b222da76c8049b96aa8cc93b8bbf4279894c4e8a5e6c5d8ca8a723f95446fe9c9027f8b6490d6ab081e85b2b5babcd511df7303d87b33a6b8410f4deb194dabb2311c0ccac8533ac99dc8e080d9f8e8f");
        //editor.commit();

        final String client_id = "1";
        final String client_secret = "mlJHYMcFYLDTzjxio1SR7crta42sEAvzr21WXAxj";
/*        final String client_id = "3";
        final String client_secret = "PgAXIt0XZFe32G7BbJKOKWEUriZd720rj2AXJ19";*/

        final String a_token, r_token;
        a_token=sp.getString("a_token",null);
        r_token=sp.getString("r_token",null);
        if(a_token!=null&&r_token!=null)
        {
            CallbackFunction cbf = new CallbackFunction() {
                @Override
                public void onResponse(JSONObject risposta) {
                    Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                    startActivity(i);
                }

                @Override
                public void onError(JSONObject risposta) throws JSONException {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("r_token", r_token);
                    jsonObject.put("grant_type", "refresh_token");
                    jsonObject.put("client_id", client_id);
                    jsonObject.put("client_secret", client_secret);
                    jsonObject.put("refresh_token", sp.getString("r_token", ""));

                    CallbackFunction cbf2 = new CallbackFunction() {
                        @Override
                        public void onResponse(JSONObject risposta) throws JSONException {
                            editor.putString("a_token", risposta.getString("access_token"));
                            editor.putString("r_token",risposta.getString("refresh_token"));
                            editor.commit();

                            Intent i = new Intent(AuthActivity.this, DashboardActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void onError(JSONObject risposta) {
                            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
                    requestQueue.add(Connessione.sendPost(null,"oauth/token", jsonObject, cbf2));
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(AuthActivity.this);
            Map<String,String> map= new HashMap<>();
            map.put("a_token",sp.getString("a_token",""));
            requestQueue.add(Connessione.sendGet(map,"api/utente", cbf));
        }
        else{
            Intent i = new Intent(AuthActivity.this, LoginActivity.class);
            startActivity(i);

        }



    }
}
