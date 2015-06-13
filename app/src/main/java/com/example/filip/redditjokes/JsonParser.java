package com.example.filip.redditjokes;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JsonParser {

    static JSONObject jObj = null;
    static String json = "";

    public JsonParser(){}

    public JSONObject getJSONFromURL(String url) throws IOException, JSONException {

        //Making an OkHTTPClient
        OkHttpClient client = new OkHttpClient();
        //Creating a request for the given url
        Request request = new Request.Builder()
                .url(url)
                .build();

        //Executing  the request
        Response response = client.newCall(request).execute();
        //Getting the response into a string
        json = response.body().string();

        //Getting the string into a jsonObject
        jObj = new JSONObject(json);

        return jObj;
    }

}
