package io.eklesia.eklesia;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanc on 13/02/2018.
 */

public class Connessione {
    static String pref = "http://192.168.1.5/portale/public/";
    boolean risposta;

    public Connessione(){
        super();
    }

    public static JsonObjectRequest sendGet (String uri, final CallbackFunction cbf){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, pref + uri, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cbf.onResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cbf.onError(error.toString());
            }
        }){
            public Map<String,String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjE3MzI5MGNiNDljZGM0OWJjM2ExZTcxODEyNTkxNmVmMGJlMzU0MGY1YmUxNTQwNmEyYWRhZjMwOWM3NmNlMjJkZmI2YWU2NDVmZjA5YzdiIn0.eyJhdWQiOiIxIiwianRpIjoiMTczMjkwY2I0OWNkYzQ5YmMzYTFlNzE4MTI1OTE2ZWYwYmUzNTQwZjViZTE1NDA2YTJhZGFmMzA5Yzc2Y2UyMmRmYjZhZTY0NWZmMDljN2IiLCJpYXQiOjE1MTg1NDg0ODQsIm5iZiI6MTUxODU0ODQ4NCwiZXhwIjoxNTE4NjM0ODgzLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.oLgk8EqThroE1O4kQoEwVmPCFQOr6NVe6AtaoS8d6LXL8p1ghBUwdJALkx-Zf1ZgJ-NwcWdao0PXckT7DOVB_WGjy7xTnsgeO0bzLD9r5KIDMFeXa0wir4aPN_DdNq6dZ6zDo4HLJfM2hJX1n8ercOLMfANdyhSnXvFsye0SYh9cRWrVg7OqcWbTkdqbL6_7zuldwXxpp5aSoF3dQScnbPi0Ju1tReMa8Dp-dedykAzrYl3S2Phcxxrxj9IPmoHRQ8HtrJypYmGlN6OCqQfZvWxBUeG6TuAMrcjooA18m6DrG424vYtlBct8zMBVFy4Q9f6XZlw-q6H5HtVl5lAoq-4-Mk4EY0YG10ZF6YLfj6LhvhYvtO-JKr6A_90z3bG4sOlrPsjPvR9zE4df9dy7oz56fJ3qkpcaNtLYNbQeC1qeLt5pGjuymUnmSkKyjAsqjeb8fy67C9L0nyhWaI7bqFxneq64I6WOiCqgT7_xyZDxVU30EMH542efETETwMDjvAMDCZq3xxPWerymqzsjn6_YJePHdwFoUgU78ruh3cjHWYqK3hMO__wu91Q2AkumEAdzhIciCd8yXvPQpePi4CN7CK3PUqLMJvlN4a-gV0KICGuHoopMfjM1no0CTDvhkvPh0x610NAJwVueJb3hme0t49GT1ZKxceLUqcU0tew");
                return headers;
            }
        };

        return jsonObjectRequest;
    }

}
