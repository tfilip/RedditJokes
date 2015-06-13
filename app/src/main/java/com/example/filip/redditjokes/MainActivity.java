package com.example.filip.redditjokes;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseObject;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity {

    private final String REDDIT_URL = "https://www.reddit.com/r/MeanJokes/top.json";
    private ArrayList<Joke> mJokes = new ArrayList<Joke>();
    private TextView mJokeTitleView;
    private TextView mJokeTextView;
    private ShakeListener mShaker;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mJokeTitleView = (TextView) findViewById(R.id.jokeTitle);
        mJokeTextView = (TextView) findViewById(R.id.jokeText);
        mJokeTextView.setMovementMethod(new ScrollingMovementMethod());
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        mShaker = new ShakeListener(this);

        //Executing the parse
        JSONParse jsonParse = new JSONParse();
        jsonParse.execute();


        // If you click on the title it goes to the next joke
        mJokeTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             nextJoke();
            }
        });

        //Shake listener
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                vibe.vibrate(100);
                nextJoke();
            }
        });



    }


    //Methods for the shake listener
    @Override
    protected void onResume() {
        mShaker.resume();
        super.onResume();
    }

    @Override
    public void onPause()
    {
        mShaker.pause();
        super.onPause();
    }

    private class JSONParse extends AsyncTask<String, Void, JSONObject>{

        //Getting the json object from the jsonParser
        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jsonParser = new JsonParser();
            try {
                JSONObject json = jsonParser.getJSONFromURL(REDDIT_URL);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            JSONObject mainData = null;

            //Getting into the first data object from json
            try {
                mainData = jsonObject.getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();

            }
            //Getting into the children array object
            JSONArray children = null;
            try {
                children = mainData.getJSONArray("children");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int jsonLength = children.length();
            boolean firstime=false;
            for(int i=0;i<jsonLength;i++){

                //Geting the data object from the array
                JSONObject dataObject = null;
                try {
                    dataObject = children.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Getting the data object from the data object from the array ... This sounds like fucking inception
                JSONObject data = null;
                try {
                    data = dataObject.getJSONObject("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Getting the joke title and the joke text
                String jokeTitle = data.optString("title").toString();
                String jokeText = data.optString("selftext").toString();

                Log.i("PARSED DATA", jokeTitle + jokeText);

                //Creating the joke
                Joke newJoke = new Joke();
                newJoke.setmJoke(jokeText);
                newJoke.setmTitle(jokeTitle);
                mJokes.add(newJoke);
                //If it's the first the first joke loaded show it on the screen
                if(!firstime)
                    showTextOnScreen();
                firstime=true;


            }


        }
    }

    //Showing the joke on the screen

    public void showTextOnScreen(){


        mJokeTitleView.setText(mJokes.get(index).getmTitle());
        mJokeTextView.setText(mJokes.get(index).getmJoke());

    }


    //Method for the fonts

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //Going to the next joke from the ArrayList
    public void nextJoke(){
        index++;
        if (index == mJokes.size())
            index = 0;
        showTextOnScreen();
    }



}
