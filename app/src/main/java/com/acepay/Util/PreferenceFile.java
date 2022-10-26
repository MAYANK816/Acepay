package com.acepay.Util;

/**
 * Created by pro55 on 28/4/17.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceFile {

    private static PreferenceFile instance=null;
    private static SharedPreferences sharedPreferences;


    public PreferenceFile(){
        sharedPreferences=App.getGlobalPefs();
    }

    public static PreferenceFile getInstance()
    {

        if (instance !=null) {
            return instance;
        }
        else {
            return  instance=new PreferenceFile();
        }
    }

    public void saveData(Context context, String Key, String Data) {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Key,Data);
        editor.apply();
    }

    public String getPreferenceData(Context context, String Key) {
        SharedPreferences sharedPreferences=App.getGlobalPefs();
        return sharedPreferences.getString(Key,null);
    }

    public  void logout() {

        App.getGlobalPefs().edit().clear().apply();
        instance = null;
    }
}