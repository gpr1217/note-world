package apps.gpr.noteworld.views;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
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

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.core.DatabaseService;
import apps.gpr.noteworld.db.NotesDatabase;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 *  TODO - DONE Button for saving and ActionBar
 *  TODO - Provide list options numbering and checkboxes
 *  TODO - Color change options
 *
 */
public class EditNoteActivity extends AppCompatActivity {

    @BindView(R.id.edit_note) EditText edit_note;
    private boolean isNoteSaved = false;
    private boolean isEdited = false;
    AlertDialog.Builder builder;
    private DbReceiver dbReceiver;
    private MenuItem action_save;

    Notes notesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        notesData = (Notes) getIntent().getParcelableExtra("clickedRowData");

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

                if (!charSequence.toString().equals(selectedNote)){
                    isEdited = true;
                    if (action_save != null)
                        action_save.setVisible(true);
                }else{
                    isEdited = false;
                    if (action_save != null)
                        action_save.setVisible(false);
                }

                notesData.setNote(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEdited = true;
            }
        });

        edit_note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    startDbService(notesData.getId(),notesData.getNote());
                    isNoteSaved = true;
                    return true;
                }
                isNoteSaved = false;
                return false;
            }
        });
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
            super.onBackPressed();
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

    private void startDbService(int id,String note){
        try{
            Notes notesObj = new Notes();
            notesObj.setId(id);
            notesObj.setNote(note);
            notesObj.setStatus(notesData.getStatus());
            notesObj.setTimeStamp(notesObj.getTimeStamp());
            notesObj.setModifiedTimeStamp(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE));

            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.UPDATE);
            intent.putExtra("notesObj",notesObj);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void backToList(){
        Intent intent = new Intent(getApplicationContext(),NotesActivity.class);
        startActivity(intent);
    }

    private void openYesNoAlertDialog(){
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Do want to save the note?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startDbService(notesData.getId(),notesData.getNote());
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
                startDbService(notesData.getId(),notesData.getNote());
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
