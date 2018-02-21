package io.eklesia.eklesia;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by ivanc on 13/02/2018.
 */

public interface CallbackFunction {
    public void onResponse(JSONObject risposta) throws JSONException, ParseException;
    public void onError(JSONObject risposta) throws JSONException;
}
