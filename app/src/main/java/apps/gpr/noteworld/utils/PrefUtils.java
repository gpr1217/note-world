package apps.gpr.noteworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PrefUtils {

    private static final String nwPreferences = "nwPreferences";
    public static final String KEY_FIRST_LAUNCH = "first_launch";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PrefUtils(Context c) {
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

        Log.d("PrefUtils","savePreference");
        Log.d("PrefUtils", String.valueOf(preferences.getBoolean(KEY_FIRST_LAUNCH,false)));
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
}
