package apps.gpr.noteworld.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.fragments.ModeListFragment;
import apps.gpr.noteworld.views.fragments.ThemeColorFragment;

public class ScreenModeActivity extends BaseActivity{

    PrefUtils prefUtils;
    public static String savedMode;
    FrameLayout fragment_top,fragment_bottom;

    private ThemeColorFragment colorFragment;
    private ModeListFragment modeListFragment;
    private FragmentTransaction ft;

    TextView theme_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_mode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Const.SETTINGS_THEME_MODE);

        fragment_top = findViewById(R.id.fragment_top);
        fragment_bottom = findViewById(R.id.fragment_bottom);
        theme_title = findViewById(R.id.theme_title);

        prefUtils = new PrefUtils(this);
        if (!prefUtils.hasKey(PrefUtils.KEY_SCREEN_MODE)){
            PrefUtils.setModePreference(Const.SCREEN_LIGHT,this);
        }

        String screenMode = prefUtils.getStringPreference(PrefUtils.KEY_SCREEN_MODE);
        if (screenMode != null && screenMode.equals(Const.SCREEN_DARK)){
            savedMode = screenMode;
            theme_title.setTextColor(getResources().getColor(R.color.colorWhite));
        }else{
            screenMode = Const.SCREEN_LIGHT;
            PrefUtils.setModePreference(screenMode,this);
            theme_title.setTextColor(getResources().getColor(R.color.colorBlack));
        }

        Bundle bundle = new Bundle();

        modeListFragment = new ModeListFragment();
        colorFragment = new ThemeColorFragment();

        addModeListFragment(bundle);
        addThemeColorFragment(bundle);
    }

    private boolean visibleFragment(){
        ModeListFragment modeListFragment = (ModeListFragment) getSupportFragmentManager().findFragmentByTag("ModeList");
        ThemeColorFragment themeColor = (ThemeColorFragment) getSupportFragmentManager().findFragmentByTag("ThemeColor");

        if (themeColor != null && themeColor.isVisible()){
            return true;
        } else return modeListFragment != null && modeListFragment.isVisible();
    }

    private void addModeListFragment(Bundle args){
        ft = getSupportFragmentManager().beginTransaction();
        modeListFragment.setArguments(args);

        if (visibleFragment()){
            ft.replace(R.id.fragment_top, modeListFragment,"ModeList");
        }else {
            ft.add(R.id.fragment_top, modeListFragment,"ModeList");
        }

        ft.addToBackStack(null);
        ft.commit();
    }

    private void addThemeColorFragment(Bundle args){
        ft = getSupportFragmentManager().beginTransaction();
        colorFragment.setArguments(args);

        if (visibleFragment()){
            ft.replace(R.id.fragment_bottom, colorFragment,"ThemeColor");
        }else {
            ft.add(R.id.fragment_bottom, colorFragment,"ThemeColor");
        }

        ft.addToBackStack(null);
        ft.commit();
    }

    public void saveThemeColor(String color){
        if (!color.equals(""))
            prefUtils.savePreference(PrefUtils.KEY_THEME_COLOR, color);

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("StartFragment",true);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        modeListFragment = (ModeListFragment) getSupportFragmentManager().findFragmentByTag("ModeList");
        colorFragment = (ThemeColorFragment) getSupportFragmentManager().findFragmentByTag("ThemeColor");

        if (modeListFragment != null && colorFragment != null) {
            if (modeListFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(modeListFragment);
            }

            if (colorFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(colorFragment);
            }
            backToSettings();
        }else if (modeListFragment != null){
            if (modeListFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(modeListFragment);
            }
            backToSettings();

        }else if (colorFragment != null){
            if (colorFragment.isVisible()) {
                getSupportFragmentManager().beginTransaction().remove(colorFragment);
            }
            backToSettings();
        } else{
            backToSettings();
        }
    }

    private void backToSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
