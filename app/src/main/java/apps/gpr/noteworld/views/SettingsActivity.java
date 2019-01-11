package apps.gpr.noteworld.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.SettingsAdapter;
import apps.gpr.noteworld.model.Settings;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.utils.RecyclerTouchListener;
import apps.gpr.noteworld.views.fragments.PasscodeLockFragment;
import apps.gpr.noteworld.views.fragments.SettingsFragment;
import apps.gpr.noteworld.views.fragments.ThemeColorFragment;

public class SettingsActivity extends BaseActivity implements SettingsAdapter.SettingsAdapterListener{

    private RecyclerView recyclerView;
    private FrameLayout frameLayout;

    private AddEmailFragment addEmailFragment;
    private PasscodeLockFragment passcodeLockFragment;
    private FragmentTransaction fragmentTransaction;
    DbReceiver dbReceiver;
    List<Settings> settingsData;
    private String settingsPC;
    PrefUtils prefUtils;

    List<String> settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle("Settings");

        prefUtils = new PrefUtils(this);
        addEmailFragment = new AddEmailFragment();
        passcodeLockFragment = new PasscodeLockFragment();

        settingsList = Const.settingsList;
        getSettings();

        recyclerView = findViewById(R.id.settings_recycler_view);
        recyclerView.setHasFixedSize(true);
        frameLayout = findViewById(R.id.settings_inner_frame);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);

                switch (Const.settingsList.get(position)) {
                    case Const.THEME_AND_MODE:
                        Intent in = new Intent(getApplicationContext(), ScreenModeActivity.class);
                        startActivity(in);
                        break;
                    case Const.PASSWORD_LOCK:
                        getSupportActionBar().setTitle(Const.SETTINGS_PASSWORD_LOCK);

                        recyclerView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        addPasscodeLockFragment(bundle);

                        break;
                    case Const.ADD_EMAIL:
                        getSupportActionBar().setTitle(Const.SETTINGS_ADD_EMAIL);

                        recyclerView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        addEmailFragment();

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("LockSetUp")){
                if (bundle.getString("LockSetUp").equals(Const.PASSCODE_NEW))
                    Toast.makeText(getApplicationContext(), "Passcode Set up is done", Toast.LENGTH_LONG).show();

                if (bundle.getString("LockSetUp").equals(Const.PASSCODE_CHANGE))
                    Toast.makeText(getApplicationContext(), "Passcode changed", Toast.LENGTH_LONG).show();

            }
        }

        SettingsAdapter adapter = new SettingsAdapter(this, settingsList, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public String getScreenSize() {
        return super.getScreenSize();
    }

    private boolean visibleFragment(){
        AddEmailFragment addEmail = (AddEmailFragment) getSupportFragmentManager().findFragmentByTag("AddEmail");
        PasscodeLockFragment passcodeLock = (PasscodeLockFragment) getSupportFragmentManager().findFragmentByTag("PasscodeLock");

        if (addEmail != null && addEmail.isVisible()){
            return true;
        } else return passcodeLock != null && passcodeLock.isVisible();
    }

    private void addEmailFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        passcodeLockFragment = new PasscodeLockFragment();
        fragmentTransaction.remove(passcodeLockFragment);
        if (visibleFragment()) {
            fragmentTransaction.replace(R.id.settings_inner_frame, addEmailFragment, "AddEmail");
        } else {
            fragmentTransaction.add(R.id.settings_inner_frame, addEmailFragment, "AddEmail");
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addPasscodeLockFragment(Bundle bundle){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        passcodeLockFragment.setArguments(bundle);

        addEmailFragment = new AddEmailFragment();
        fragmentTransaction.remove(addEmailFragment);
        if (visibleFragment()) {
            fragmentTransaction.replace(R.id.settings_inner_frame, passcodeLockFragment, "PasscodeLock");
        } else {
            fragmentTransaction.add(R.id.settings_inner_frame, passcodeLockFragment, "PasscodeLock");
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        try {
            CommonUtilities.hideKeyboard(this);

            addEmailFragment = (AddEmailFragment) getSupportFragmentManager().findFragmentByTag("AddEmail");
            passcodeLockFragment = (PasscodeLockFragment) getSupportFragmentManager().findFragmentByTag("PasscodeLock");

            if (addEmailFragment == null && passcodeLockFragment == null){
                backToNotes();
            }
            else if (addEmailFragment == null && passcodeLockFragment != null){
                if (passcodeLockFragment.isVisible())
                    backToSettings();
                else
                    backToNotes();
            }
            else if (addEmailFragment != null && passcodeLockFragment == null){
                if (addEmailFragment.isVisible())
                    backToSettings();
                else
                    backToNotes();
            }
            else if (addEmailFragment != null && passcodeLockFragment != null){
                if (addEmailFragment.isVisible() || passcodeLockFragment.isVisible())
                    backToSettings();
                else
                    backToNotes();
            }
            else{
                backToNotes();
            }
        }catch (Exception e){
            e.printStackTrace();
            backToNotes();
        }
    }

    private void backToSettings(){
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void backToNotes(){
        Intent intent = new Intent(getApplicationContext(), NotesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDbReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtilities.hideKeyboard(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dbReceiver);
    }



    public void insertPassCode(String pass_code){
        try{
            Settings settings = new Settings();
            settings.setPasscode(pass_code);
            settings.setCreated_date(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE));

            // In SharedPreferences
            prefUtils.savePreference(PrefUtils.KEY_PASS_CODE,pass_code);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getSettings(){
        if (prefUtils.hasKey(PrefUtils.KEY_PASS_CODE)) {
            settingsPC = prefUtils.getStringPreference(PrefUtils.KEY_PASS_CODE);
        }else
            settingsPC = "";
    }

    private void setDbReceiver(){
        try {
            dbReceiver = new DbReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Const.FILTER_ACTION_SETTINGS_DATA);
            LocalBroadcastManager.getInstance(this).registerReceiver(dbReceiver, intentFilter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class DbReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();

                if (action.equals(Const.FILTER_ACTION_SETTINGS_DATA)){
                    settingsData = bundle.getParcelableArrayList("list");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }





    public static class AddEmailFragment extends Fragment {


        private PrefUtils prefUtils;

        public AddEmailFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            prefUtils = new PrefUtils(getActivity());
        }

        private View view;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_settings,container, false);

            ListView colorsList = view.findViewById(R.id.colors_list);
            colorsList.setVisibility(View.GONE);

            RelativeLayout add_email_layout = view.findViewById(R.id.add_email_layout);
            add_email_layout.setVisibility(View.VISIBLE);

            TextView email_note = view.findViewById(R.id.email_note);
            if (PrefUtils.getModePreference(getContext()).equals(Const.SCREEN_DARK)){
                email_note.setTextColor(getResources().getColor(R.color.colorWhite));
            }else{
                email_note.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            final EditText add_email_edittext = view.findViewById(R.id.add_email_edittext);
            if (prefUtils.hasKey("email_id")) {
                if (!prefUtils.getStringPreference("email_id").equals("")) {
                    add_email_edittext.setHint("");
                    add_email_edittext.setText(prefUtils.getStringPreference("email_id"));
                }
            }

            add_email_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (add_email_edittext.getText().equals("")) {
                        if (hasFocus) {
                            add_email_edittext.setHint("");
                        } else {
                            add_email_edittext.setHint(getResources().getString(R.string.example_email_text));
                        }
                    }
                }
            });

            Button btn_save = view.findViewById(R.id.btn_save);
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!add_email_edittext.getText().equals("")){
                        prefUtils.savePreference("email_id",add_email_edittext.getText().toString());
                        add_email_edittext.setText("");
                        add_email_edittext.setHint(getResources().getString(R.string.example_email_text));

                        Toast.makeText(getContext(), "Email Id saved", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getContext(), SettingsActivity.class);
                        startActivity(in);
                        getActivity().finish();
                    }
                }
            });

            return view;
        }
    }
}

