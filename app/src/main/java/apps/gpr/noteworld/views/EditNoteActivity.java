package apps.gpr.noteworld.views;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.core.DatabaseService;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EditNoteActivity extends BaseActivity {

    @BindView(R.id.edit_note) EditText edit_note;
    private boolean isNoteSaved = false;
    private boolean isEdited = false;
    private AlertDialog.Builder builder;
    private DbReceiver dbReceiver;
    private MenuItem action_save;
    private MenuItem action_speak;

    private Notes notesData;

    private String note_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra(Const.INTENT_ACTION_NOTES).equals(Const.ACTION_NEW_NOTE)) {
                getSupportActionBar().setTitle("Add new note");
                note_type = Const.ACTION_NEW_NOTE;
                notesData = new Notes();
            }else if(getIntent().getStringExtra(Const.INTENT_ACTION_NOTES).equals(Const.ACTION_EDIT_NOTE)){
                getSupportActionBar().setTitle("Edit note");
                note_type = Const.ACTION_EDIT_NOTE;
                notesData = getIntent().getParcelableExtra("clickedRowData");
            }
        }

        if (note_type.equals(Const.ACTION_EDIT_NOTE)){
            edit_note.setText(notesData.getNote());
            edit_note.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            edit_note.setSelection(edit_note.getText().toString().length());

            final String selectedNote = notesData.getNote();
            edit_note.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if (charSequence.length() > selectedNote.length()){
                        isEdited = true;
                        if (action_save != null)
                            action_save.setVisible(true);
                    }
                    else {
                        isEdited = false;
                        if (action_save != null)
                            action_save.setVisible(false);
                    }

                    notesData.setNote(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            edit_note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        updateNote(notesData.getId(), notesData.getNote());
                        isNoteSaved = true;
                        return true;
                    }
                    isNoteSaved = false;
                    return false;
                }
            });
        }else{
            edit_note.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (!charSequence.toString().equals("")){
                        if (action_save != null)
                            action_save.setVisible(true);

                        isEdited = true;
                        notesData.setNote(charSequence.toString());
                    }else {
                        isEdited = false;
                        if (action_save != null)
                            action_save.setVisible(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            edit_note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        insertNote(notesData.getNote());
                        isNoteSaved = true;
                        return true;
                    }
                    isNoteSaved = false;
                    return false;
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDbReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dbReceiver);
    }

    @Override
    public void onBackPressed() {
        if (!isNoteSaved && isEdited){
            openYesNoAlertDialog();
        }else{
            //super.onBackPressed();
            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
            finish();
        }
    }

    private void setDbReceiver(){
        try {
            dbReceiver = new DbReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Const.FILTER_ACTION_DATA);
            LocalBroadcastManager.getInstance(this).registerReceiver(dbReceiver, intentFilter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertNote(String note){
        try {
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.INSERT);
            intent.putExtra("table", Const.TABLE_NOTES);
            intent.putExtra("note", note);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateNote(int id,String note){
        try{
            Notes notesObj = new Notes();
            notesObj.setId(id);
            notesObj.setNote(note);
            notesObj.setStatus(notesData.getStatus());
            notesObj.setTimeStamp(notesObj.getTimeStamp());
            notesObj.setModifiedTimeStamp(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE));

            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.UPDATE);
            intent.putExtra("table", Const.TABLE_NOTES);
            intent.putExtra("notesObj",notesObj);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void backToList(){
        Intent intent = new Intent(getApplicationContext(),NotesActivity.class);
        startActivity(intent);
        finish();
    }

    private void openYesNoAlertDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Do want to save the note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (note_type.equals(Const.ACTION_NEW_NOTE)){
                    insertNote(notesData.getNote());
                }else {
                    updateNote(notesData.getId(), notesData.getNote());
                }
                backToList();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"Note not saved!",Toast.LENGTH_LONG).show();
            }
        });
        builder.create();

        builder.show();
    }

    private class DbReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals(Const.FILTER_ACTION_DATA)){
                    //backToList();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        action_save = menu.findItem(R.id.action_save);
        action_save.setVisible(false);


        action_speak = menu.findItem(R.id.action_speak);

        /*if (isRecognitionSpeechAvailable())
            action_speak.setVisible(true);
        else*/
            action_speak.setVisible(false);

        MenuItem action_search = menu.findItem(R.id.action_search);
        action_search.setVisible(false);

        MenuItem action_empty = menu.findItem(R.id.action_empty);
        action_empty.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                isNoteSaved = true;
                if (note_type.equals(Const.ACTION_NEW_NOTE)){
                    insertNote(notesData.getNote());
                }else {
                    updateNote(notesData.getId(), notesData.getNote());
                }
                backToList();
                return true;
            case R.id.action_speak:
                promptSpeech();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isRecognitionSpeechAvailable(){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0){
            return true;
        }
        return false;
    }

    private void promptSpeech(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi! Speak Something");

        try{
            startActivityForResult(intent, 100);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), "Sorry, your device doesnt support speech support", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 100:
                if (resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String note = edit_note.getText().toString();
                    if (note_type.equals(Const.ACTION_EDIT_NOTE) && !note.equals("")){
                        edit_note.setText(note + " " +result.get(0));
                    }else{
                        edit_note.setText(result.get(0));
                    }
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Sorry, your device doesn't support speech support", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
