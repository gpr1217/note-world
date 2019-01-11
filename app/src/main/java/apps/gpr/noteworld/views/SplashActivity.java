package apps.gpr.noteworld.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity{

    private static final String TAG = "SplashActivity";
    PrefUtils preferences;

    private Animation shakeAnimation;

    @BindView(R.id.btn_get_started) Button btn_get_started;
    @BindView(R.id.loading) TextView loading;
    @BindView(R.id.welcome) TextView welcome;

    @BindView(R.id.splash_layout) RelativeLayout splash_layout;
    @BindView(R.id.lock_layout) RelativeLayout lock_layout;
    @BindView(R.id.rounds_layout) LinearLayout rounds_layout;

    @BindViews({R.id.round_solid_num1,R.id.round_solid_num2,R.id.round_solid_num3,R.id.round_solid_num4})
    List<ImageView> solidImageViews;

    @BindViews({R.id.round_hollow_num1,R.id.round_hollow_num2,R.id.round_hollow_num3,R.id.round_hollow_num4})
    List<ImageView> hollowImageViews;

    @BindViews({R.id.btn_0,R.id.btn_1,R.id.btn_2,R.id.btn_3,R.id.btn_4,R.id.btn_5,R.id.btn_6,R.id.btn_7,
            R.id.btn_8,R.id.btn_9})
    List<Button> numButtons;

    @BindView(R.id.main_layout) RelativeLayout main_layout;

    String pass_code;
    String code = "";
    int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        preferences = new PrefUtils(this);

        shakeAnimation = AnimationUtils.loadAnimation(this,R.anim.shake_anim);
    }

    @OnClick(R.id.btn_get_started)
    public void getStarted(Button button){
        preferences.savePreference(PrefUtils.KEY_FIRST_LAUNCH,true);
        startNotesActivity();
    }

    private void startNotesActivity(){
        Intent in = new Intent(getApplicationContext(), NotesActivity.class);
        startActivity(in);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        if (!preferences.getPreference()){
            btn_get_started.setVisibility(View.VISIBLE);
            //loading.setVisibility(View.GONE);
        }else{
            btn_get_started.setVisibility(View.GONE);
            welcome.setText(getResources().getString(R.string.app_name));
            //loading.setVisibility(View.VISIBLE);

            pass_code = preferences.getStringPreference("pass_code");
            boolean lock_enabled = preferences.getBooleanPreference("pass_code_status");
            if (!pass_code.equals("") && lock_enabled){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        splash_layout.setVisibility(View.GONE);
                        lock_layout.setVisibility(View.VISIBLE);
                    }
                },1500);
            }else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startNotesActivity();
                    }
                }, 2000);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_0)
    void onBtn0Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(0).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_1)
    void onBtn1Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(1).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_2)
    void onBtn2Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(2).getText().toString();
        }
        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_3)
    void onBtn3Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(3).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_4)
    void onBtn4Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(4).getText().toString();
        }
        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_5)
    void onBtn5Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(5).getText().toString();
        }
        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }

    }

    @OnClick(R.id.btn_6)
    void onBtn6Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(6).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_7)
    void onBtn7Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(7).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_8)
    void onBtn8Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(8).getText().toString();
        }
        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    @OnClick(R.id.btn_9)
    void onBtn9Clicked(){
        if (length <= 3) {
            length++;
            code = code + numButtons.get(9).getText().toString();
        }

        enableCircle();
        if (length == 4){
            verifyPasscode(code);
        }
    }

    private void verifyPasscode(String pCode){
        if (pCode.equals(pass_code)){
            startNotesActivity();
        }else {
            length = 0;
            code = "";
            solidImagesVisibility(View.GONE);

            hollowImagesVisibility(View.VISIBLE);

            rounds_layout.startAnimation(shakeAnimation);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rounds_layout.clearAnimation();
                    /*if (canCancelAnimation()){
                        //rounds_layout.getAnimation().cancel();
                    }*/
                }
            },1000);
        }
    }

    public static boolean canCancelAnimation(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    private void enableCircle(){
        if (length == 1){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.GONE);
            solidImageViews.get(2).setVisibility(View.GONE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.VISIBLE);
            hollowImageViews.get(2).setVisibility(View.VISIBLE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else if (length == 2){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.VISIBLE);
            solidImageViews.get(2).setVisibility(View.GONE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.GONE);
            hollowImageViews.get(2).setVisibility(View.VISIBLE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else if (length == 3){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.VISIBLE);
            solidImageViews.get(2).setVisibility(View.VISIBLE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.GONE);
            hollowImageViews.get(2).setVisibility(View.GONE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else if (length == 4){
            solidImagesVisibility(View.VISIBLE);

            hollowImagesVisibility(View.GONE);
        }
    }

    private void deleteNum(int len){
        if (len == 1){
            solidImagesVisibility(View.GONE);

            hollowImagesVisibility(View.VISIBLE);
        }
        else if (len == 2){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.GONE);
            solidImageViews.get(2).setVisibility(View.GONE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.VISIBLE);
            hollowImageViews.get(2).setVisibility(View.VISIBLE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else if (len == 3){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.VISIBLE);
            solidImageViews.get(2).setVisibility(View.GONE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.GONE);
            hollowImageViews.get(2).setVisibility(View.VISIBLE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else if (len == 4){
            solidImageViews.get(0).setVisibility(View.VISIBLE);
            solidImageViews.get(1).setVisibility(View.VISIBLE);
            solidImageViews.get(2).setVisibility(View.VISIBLE);
            solidImageViews.get(3).setVisibility(View.GONE);

            hollowImageViews.get(0).setVisibility(View.GONE);
            hollowImageViews.get(1).setVisibility(View.GONE);
            hollowImageViews.get(2).setVisibility(View.GONE);
            hollowImageViews.get(3).setVisibility(View.VISIBLE);
        }
        else{
            solidImagesVisibility(View.GONE);

            hollowImagesVisibility(View.VISIBLE);
        }
    }

    private void solidImagesVisibility(int visibility){
        solidImageViews.get(0).setVisibility(visibility);
        solidImageViews.get(1).setVisibility(visibility);
        solidImageViews.get(2).setVisibility(visibility);
        solidImageViews.get(3).setVisibility(visibility);
    }

    private void hollowImagesVisibility(int visibility){
        hollowImageViews.get(0).setVisibility(visibility);
        hollowImageViews.get(1).setVisibility(visibility);
        hollowImageViews.get(2).setVisibility(visibility);
        hollowImageViews.get(3).setVisibility(visibility);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
