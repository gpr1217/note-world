package apps.gpr.noteworld.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.PropertyResourceBundle;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.views.SettingsActivity;

public class PasscodeLockFragment extends Fragment {

    TextView passcode_title;
    private EditText num_1,num_2,num_3,num_4;
    private String input_pass_code, input_re_pass_code;
    private boolean isReEnterCode = false, isReReEnterCode = false;
    View view;

    private ListView lock_settings_list;
    ArrayAdapter<String> adapter;
    List<String> list;
    boolean hideKeyPad = true;

    private boolean prefPasscodeStatus;
    private String prefPasscode;

    PrefUtils prefUtils;
    String action = "";

    View passcode_lock_layout;

    int TIME_MILLIS_500 = 500;
    int TIME_MILLIS_1000 = 1000;

    public PasscodeLockFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefUtils = new PrefUtils(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_passcode_lock,container, false);

        passcode_title = view.findViewById(R.id.passcode_title);
        passcode_lock_layout = view.findViewById(R.id.passcode_lock_layout);
        lock_settings_list = view.findViewById(R.id.lock_settings_list);

        prefPasscodeStatus = prefUtils.getBooleanPreference(PrefUtils.KEY_PASS_CODE_STATUS);
        prefPasscode = prefUtils.getStringPreference(PrefUtils.KEY_PASS_CODE);

        if (!prefPasscodeStatus && prefPasscode.equals("")){
            action = Const.PASSCODE_NEW;
            passcode_title.setText(getResources().getString(R.string.str_set_passcode));
            list = Const.lockSettingsList;
        }
        else if (prefPasscodeStatus && !prefPasscode.equals("")){
            action = Const.PASSCODE_CHANGE;
            passcode_title.setText(getResources().getString(R.string.str_enter_old_passcode));
            list = Const.lockEnabledSettingsList;
        }
        else {
            action = Const.PASSCODE_NEW;
            passcode_title.setText(getResources().getString(R.string.str_set_passcode));
            list = Const.lockSettingsList;
        }

        settingUpAdapter();
        lock_settings_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Initialize a TextView for ListView each Item
                TextView tv = view.findViewById(android.R.id.text1);
                CommonUtilities.log("lock_settings_list",tv.getText().toString());
                boolean status = prefUtils.getBooleanPreference(PrefUtils.KEY_PASS_CODE_STATUS);
                CommonUtilities.log("lock_settings_list status", String.valueOf(status));

                CommonUtilities.log("lock_settings_list adapter pos",adapterView.getItemAtPosition(i).toString());
                if (tv.getText().equals(Const.SET_PASSCODE) || adapterView.getItemAtPosition(i).equals(Const.SET_PASSCODE)){
                    if (status) {
                        lock_settings_list.setVisibility(View.GONE);
                        passcode_title.setVisibility(View.VISIBLE);
                        passcode_lock_layout.setVisibility(View.VISIBLE);
                        showKeyPad();
                    }else {
                        Toast.makeText(getContext(), "Please enable Passcode Lock!",Toast.LENGTH_LONG).show();
                    }
                }

                else if (tv.getText().equals(Const.ENABLE_PASSCODE) && adapterView.getItemAtPosition(i).equals(Const.ENABLE_PASSCODE)){
                    Toast.makeText(getContext(), "Passcode Lock is Enabled",Toast.LENGTH_LONG).show();

                    prefUtils.savePreference(PrefUtils.KEY_PASS_CODE_STATUS,true);

                    tv.setText(Const.DISABLE_PASSCODE);

                    lock_settings_list.setVisibility(View.VISIBLE);
                    passcode_title.setVisibility(View.GONE);
                    passcode_lock_layout.setVisibility(View.GONE);
                }

                else if (tv.getText().equals(Const.DISABLE_PASSCODE) && adapterView.getItemAtPosition(i).equals(Const.ENABLE_PASSCODE)){
                    Toast.makeText(getContext(), "Passcode Lock is Disabled",Toast.LENGTH_LONG).show();

                    prefUtils.savePreference(PrefUtils.KEY_PASS_CODE_STATUS,false);

                    tv.setText(Const.ENABLE_PASSCODE);

                    lock_settings_list.setVisibility(View.VISIBLE);
                    passcode_title.setVisibility(View.GONE);
                    passcode_lock_layout.setVisibility(View.GONE);
                }

                else if (tv.getText().equals(Const.DISABLE_PASSCODE) || adapterView.getItemAtPosition(i).equals(Const.DISABLE_PASSCODE)
                        && status){

                    Toast.makeText(getContext(), "Passcode Lock is Disabled",Toast.LENGTH_LONG).show();

                    prefUtils.savePreference(PrefUtils.KEY_PASS_CODE_STATUS,false);

                    if (prefPasscode.equals("")) {
                        tv.setText(Const.ENABLE_PASSCODE);
                    }else {
                        list = Const.settingsList;
                        settingUpAdapter();
                    }

                    lock_settings_list.setVisibility(View.VISIBLE);
                    passcode_title.setVisibility(View.GONE);
                    passcode_lock_layout.setVisibility(View.GONE);
                }

                else if (tv.getText().equals(Const.CHANGE_PASSCODE) || adapterView.getItemAtPosition(i).equals(Const.CHANGE_PASSCODE)){
                    lock_settings_list.setVisibility(View.GONE);
                    passcode_title.setVisibility(View.VISIBLE);
                    passcode_lock_layout.setVisibility(View.VISIBLE);
                    showKeyPad();
                }
            }
        });

        num_1 = view.findViewById(R.id.num_1);
        num_1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        num_1.setTransformationMethod(new SolidCirclePasswordTransformationMethod());
        num_1.requestFocus();

        num_2 = view.findViewById(R.id.num_2);
        num_2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        num_2.setTransformationMethod(new SolidCirclePasswordTransformationMethod());

        num_3 = view.findViewById(R.id.num_3);
        num_3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        num_3.setTransformationMethod(new SolidCirclePasswordTransformationMethod());

        num_4 = view.findViewById(R.id.num_4);
        num_4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        num_4.setTransformationMethod(new SolidCirclePasswordTransformationMethod());

        // First Layout

        num_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (num_1.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && !isReEnterCode){
                    input_pass_code = num_1.getText().toString();
                }
                else if (num_1.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && isReEnterCode){
                    input_re_pass_code = num_1.getText().toString();
                }
                else if (num_1.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode){
                    input_pass_code = num_1.getText().toString();
                }
                else if (num_1.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && isReEnterCode && !isReReEnterCode){
                    input_re_pass_code = num_1.getText().toString();
                }
                else if (num_1.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode && isReReEnterCode) {
                    input_pass_code = num_1.getText().toString();
                }

                if (num_1.getText().length() == 1) {
                    num_2.requestFocus();
                }else {
                    num_1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        num_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (num_2.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && !isReEnterCode){
                    input_pass_code = input_pass_code + "" +num_2.getText().toString();
                }
                else if (num_2.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && isReEnterCode){
                    input_re_pass_code = input_re_pass_code + "" +num_2.getText().toString();
                }
                else if (num_2.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode){
                    input_pass_code = input_pass_code + "" +num_2.getText().toString();
                }
                else if (num_2.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && isReEnterCode && !isReReEnterCode){
                    input_re_pass_code = input_re_pass_code + "" +num_2.getText().toString();
                }
                else if (num_2.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode && isReReEnterCode) {
                    input_pass_code = input_pass_code + "" + num_2.getText().toString();
                }

                if (num_2.getText().length() == 1) {
                    num_3.requestFocus();
                }else {
                    num_2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        num_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (num_3.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && !isReEnterCode){
                    input_pass_code = input_pass_code + "" +num_3.getText().toString();
                }
                else if (num_3.getText().toString().length() == 1 && action.equals(Const.PASSCODE_NEW) && isReEnterCode){
                    input_re_pass_code = input_re_pass_code + "" +num_3.getText().toString();
                }
                else if (num_3.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode){
                    input_pass_code = input_pass_code + "" +num_3.getText().toString();
                }
                else if (num_3.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && isReEnterCode && !isReReEnterCode){
                    input_re_pass_code = input_re_pass_code + "" +num_3.getText().toString();
                }
                else if (num_3.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode && isReReEnterCode) {
                    input_pass_code = input_pass_code + "" + num_3.getText().toString();
                }

                if (num_3.getText().length() == 1) {
                    num_4.requestFocus();
                }else {
                    num_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final Handler handler = new Handler();
        num_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (num_4.getText().length() == 1 && action.equals(Const.PASSCODE_NEW) && !isReEnterCode) {
                    input_pass_code = input_pass_code + "" + num_4.getText().toString();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passcode_title.setText(getResources().getString(R.string.str_re_set_passcode));
                            isReEnterCode = true;
                            clearEditTexts();
                            num_1.requestFocus();
                        }
                    },TIME_MILLIS_500);
                }
                else if (num_4.getText().length() == 1 && action.equals(Const.PASSCODE_NEW) && isReEnterCode) {
                    input_re_pass_code = input_re_pass_code + "" + num_4.getText().toString();

                    if (!input_pass_code.equals(input_re_pass_code)){
                        clearEditTexts();
                        num_1.requestFocus();
                        Toast.makeText(getContext(),getResources().getString(R.string.str_passcode_doesnot_match), Toast.LENGTH_LONG).show();
                    }else {
                        isReEnterCode = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                savePasscodeData(input_re_pass_code, Const.PASSCODE_NEW);
                            }
                        },TIME_MILLIS_1000);
                    }
                }
                else if (num_4.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode && !isReReEnterCode){
                    input_pass_code = input_pass_code + "" +num_4.getText().toString();

                    if (!input_pass_code.equals(prefPasscode)){
                        clearEditTexts();
                        num_1.requestFocus();
                        Toast.makeText(getContext(),getResources().getString(R.string.str_old_passcode_wrong), Toast.LENGTH_LONG).show();
                    }else {
                        isReEnterCode = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clearEditTexts();
                                passcode_title.setText(getResources().getString(R.string.str_set_passcode));
                                num_1.requestFocus();
                            }
                        }, TIME_MILLIS_500);
                    }
                }
                else if (num_4.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && isReEnterCode && !isReReEnterCode){
                    input_re_pass_code = input_re_pass_code + "" +num_4.getText().toString();
                    isReReEnterCode = true;
                    isReEnterCode = false;

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clearEditTexts();
                            passcode_title.setText(getResources().getString(R.string.str_re_enter_new_passcode));
                            num_1.requestFocus();
                        }
                    },TIME_MILLIS_500);
                }
                else if (num_4.getText().toString().length() == 1 && action.equals(Const.PASSCODE_CHANGE) && !isReEnterCode && isReReEnterCode){
                    input_pass_code = input_pass_code + "" +num_4.getText().toString();

                    if (!input_re_pass_code.equals(input_pass_code)){
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                clearEditTexts();
                                num_1.requestFocus();
                                Toast.makeText(getContext(),getResources().getString(R.string.str_new_passcode_doesnot_match), Toast.LENGTH_LONG).show();
                            }
                        },TIME_MILLIS_500);
                    }else {
                        isReReEnterCode = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                savePasscodeData(input_re_pass_code, Const.PASSCODE_CHANGE);
                            }
                        },TIME_MILLIS_1000);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        num_1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_DEL){
                    num_1.requestFocus();
                }

                return false;
            }
        });

        num_2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && num_2.getText().length() == 1){
                    num_2.requestFocus();
                }

                if (keyCode == KeyEvent.KEYCODE_DEL && num_2.getText().length() == 0){
                    num_1.requestFocus();
                }

                return false;
            }
        });

        num_3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_DEL && num_3.getText().length() == 1){
                    num_3.requestFocus();
                }

                if (keyCode == KeyEvent.KEYCODE_DEL && num_3.getText().length() == 0){
                    num_2.requestFocus();
                }

                return false;
            }
        });

        num_4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_DEL && num_4.getText().length() == 1){
                    num_4.requestFocus();
                }

                if (keyCode == KeyEvent.KEYCODE_DEL && num_4.getText().length() == 0){
                    num_3.requestFocus();
                }

                return false;
            }
        });

        return view;
    }

    public void settingUpAdapter(){
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView tv = view.findViewById(android.R.id.text1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextAppearance(R.style.Passcode_Lock_List_Row_Styles);
                }else {
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    tv.setPadding(20,20,20,20);
                }

                if (prefPasscode.equals("") && prefPasscodeStatus && tv.getText().equals(Const.CHANGE_PASSCODE)){
                    tv.setText(Const.SET_PASSCODE);
                }

                return view;
            }
        };

        lock_settings_list.setAdapter(adapter);
    }


    private void savePasscodeData(String code, String type){
        ((SettingsActivity) getActivity()).insertPassCode(code);
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        intent.putExtra("LockSetUp", type);
        startActivity(intent);
    }

    private void clearEditTexts(){
        if (num_1.length() > 0)
            num_1.getText().clear();
        if (num_2.length() > 0)
            num_2.getText().clear();
        if (num_3.length() > 0)
            num_3.getText().clear();
        if (num_4.length() > 0)
            num_4.getText().clear();
    }

    private class SolidCirclePasswordTransformationMethod extends PasswordTransformationMethod{
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!hideKeyPad) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(num_1, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        CommonUtilities.hideKeyboardFrom(getContext(), view);
    }

    public void showKeyPad(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(num_1, InputMethodManager.SHOW_IMPLICIT);
    }
}
