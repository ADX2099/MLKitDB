package com.huawei.mlkit.face.demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.mlkit.face.demo.utils.FaceConstants;

public class Singleton extends Application {

    private static Singleton m_Instance;
    private static AppCompatActivity currentActivity;
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;
    private Context context;


    public Singleton() {
        super();
        m_Instance = this;

    }

    public static Singleton getInstance() {
        if(m_Instance == null) {
            synchronized(Singleton.class) {
                if(m_Instance == null) new Singleton();
            }
        }
        return m_Instance;
    }


    public void onCreate() {
        super.onCreate();
        context = this;
        initPreferences();

    }

    public static void setCurrentActivity(AppCompatActivity arg) {
        currentActivity = arg;
    }

    public static AppCompatActivity getCurrentActivity(){
        return currentActivity;
    }

    private void initPreferences(){
        if(settings == null)
            settings = getSharedPreferences(FaceConstants.PREFS, Context.MODE_PRIVATE);
        if(editor == null)
            editor = settings.edit();

    }

    public static SharedPreferences getSettings(){
        return settings;
    }

    public static SharedPreferences.Editor getEditor(){
        return editor;
    }

    public static void savePreferences(String tag, String arg){
        editor.putString(tag, arg);
        editor.apply();
    }

    public static void savePreferences(String tag, int arg){
        editor.putInt(tag, arg);
        editor.apply();
    }

    public static void savePreferences(String tag, boolean arg){
        editor.putBoolean(tag, arg);
        editor.apply();
    }

    public static void savePreferences(String tag, long arg){
        editor.putLong(tag, arg);
        editor.apply();
    }

}


