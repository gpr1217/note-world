package apps.gpr.noteworld.views;

import android.os.Bundle;
import android.widget.TextView;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;

import android.support.v7.widget.Toolbar;

public class AboutActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About NoteWorld");

        TextView textView = findViewById(R.id.about);

        if (PrefUtils.getModePreference(this).equals(Const.SCREEN_DARK)){
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
        }else {
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }
}
