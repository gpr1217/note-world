package apps.gpr.noteworld.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.BaseActivity;

public class FullScreenActivity extends BaseActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.full_screen_mode);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int key = bundle.getInt("image_key");
            String title = bundle.getString("title");
            if (key != 0){
                getSupportActionBar().setTitle(title);
                imageView.setImageResource(key);
            }
        }
    }

}
