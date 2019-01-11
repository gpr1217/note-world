package apps.gpr.noteworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apps.gpr.noteworld.model.Settings;

public class PrefUtils {

    private static final String nwPreferences = "nwPreferences";
    public static final String KEY_FIRST_LAUNCH = "first_launch";
    public static final String KEY_THEME_COLOR = "theme_color";
    public static final String KEY_SCREEN_MODE = "screen_mode";
    public static final String KEY_PASS_CODE_STATUS = "pass_code_status";
    public static final String KEY_PASS_CODE = "pass_code";

    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public PrefUtils(Context c) {
        Log.d("PrefUtils","in here" + " c = " + c);
        this.preferences = c.getSharedPreferences(nwPreferences,Context.MODE_PRIVATE);
        this.editor = preferences.edit();
        Log.d("PrefUtils","in here");
    }

    public void savePreference(String key, String value){
        editor.putString(key,value);
        editor.commit();
    }

    public void savePreference(String key, int value){
        editor.putInt(key,value);
        editor.commit();
    }

    public void savePreference(String key, boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    public void savePreference(HashMap<String,String> map){
        if (!map.isEmpty()){
            for (Map.Entry<String, String> entry : map.entrySet())
                editor.putString(entry.getKey(),entry.getValue());

            editor.commit();
        }
    }

    public boolean getPreference(){
        Log.d("PrefUtils",preferences.toString());
        return preferences.getBoolean(KEY_FIRST_LAUNCH,false);
    }

    public String getStringPreference(String key){
        Log.d("PrefUtils",preferences.toString());
        return preferences.getString(key,"");
    }

    public int getIntPreference(String key){
        Log.d("PrefUtils",preferences.toString());
        return preferences.getInt(key,0);
    }

    public boolean getBooleanPreference(String key){
        Log.d("PrefUtils",preferences.toString());
        return preferences.getBoolean(key,false);
    }

    public void removePreference(String key){
        editor.remove(key);
        editor.commit();
    }

    public static void setModePreference(String value, Context c){
        SharedPreferences pref = c.getSharedPreferences(nwPreferences,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_SCREEN_MODE,value);
        editor.commit();
    }

    public static String getModePreference(Context c){
        SharedPreferences pref = c.getSharedPreferences(nwPreferences,Context.MODE_PRIVATE);

        return pref.getString(KEY_SCREEN_MODE,"");
    }

    public boolean hasKey(String key){
        return preferences.contains(key);
    }
}
