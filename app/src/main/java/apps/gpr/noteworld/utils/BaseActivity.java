package apps.gpr.noteworld.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.views.SettingsActivity;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private PrefUtils prefUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSavedTheme());
    }

    private int getSavedTheme() {
        prefUtils = new PrefUtils(this);

        String mode = prefUtils.getStringPreference(PrefUtils.KEY_SCREEN_MODE);
        String theme = prefUtils.getStringPreference(PrefUtils.KEY_THEME_COLOR);

        switch (theme) {
            case "Black":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Black_Dark;
                else
                    return R.style.AppTheme_Black;
            case "Blue":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Blue_Dark;
                else
                    return R.style.AppTheme_Blue;
            case "Pink":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Pink_Dark;
                else
                    return R.style.AppTheme_Pink;
            case "Yellow":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Yellow_Dark;
                else
                    return R.style.AppTheme_Yellow;
            case "Gray":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Gray_Dark;
                else
                    return R.style.AppTheme_Gray;
            case "Teal":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Teal_Dark;
                else
                    return R.style.AppTheme_Teal;
            case "Light Brown":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Brown_Dark;
                else
                    return R.style.AppTheme_Brown;
            case "Purple":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Purple_Dark;
                else
                    return R.style.AppTheme_Purple;
            case "Cyan":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Cyan_Dark;
                else
                    return R.style.AppTheme_Cyan;
            case "Green":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Green_Dark;
                else
                    return R.style.AppTheme_Green;
            case "Indigo":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Indigo_Dark;
                else
                    return R.style.AppTheme_Indigo;
            case "Lime Yellow":
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Lime_Dark;
                else
                    return R.style.AppTheme_Lime;
            default:
                if (mode.equals(Const.SCREEN_DARK))
                    return R.style.AppTheme_Orange_Dark;
                else
                    return R.style.AppTheme_Orange;
        }
    }

    public String getScreenSize(){
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize){
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return Const.SCREEN_SIZE_LARGE;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return Const.SCREEN_SIZE_NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return Const.SCREEN_SIZE_SMALL;
            default:
                return "";
        }
    }
}
