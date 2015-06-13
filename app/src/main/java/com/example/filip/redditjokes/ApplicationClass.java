package com.example.filip.redditjokes;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationClass extends Application {


    //I've added this class for the font
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }
}
