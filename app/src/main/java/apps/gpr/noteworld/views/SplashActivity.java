package apps.gpr.noteworld.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.PrefUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private PrefUtils preferences;

    @BindView(R.id.btn_get_started) Button btn_get_started;
    @BindView(R.id.loading) TextView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        preferences = new PrefUtils(this);

        Log.d(TAG, String.valueOf(preferences.getPreference()));
        if (!preferences.getPreference()){
            preferences.savePreference(PrefUtils.KEY_FIRST_LAUNCH,true);
            btn_get_started.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }else{
            btn_get_started.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            Intent in = new Intent(getApplicationContext(), NotesActivity.class);
            startActivity(in);
        }
        Log.d(TAG, String.valueOf(preferences.getPreference()));
    }

    @OnClick(R.id.btn_get_started)
    public void getStarted(Button button){
        Intent in = new Intent(getApplicationContext(), NotesActivity.class);
        startActivity(in);
    }
}
