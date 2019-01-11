package apps.gpr.noteworld.views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.NotesAdapter;
import apps.gpr.noteworld.core.DatabaseService;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;
import apps.gpr.noteworld.utils.PrefUtils;
import apps.gpr.noteworld.utils.RecyclerTouchListener;

public class NotesActivity extends BaseActivity implements NotesAdapter.NotesAdapterListener{

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Notes> notesList;
    private Notes notes = new Notes();
    private MenuItem action_search;
    private boolean isSearch = false;

    private static String EMAIL_FILE = "file";
    private static String EMAIL_FILE_TO_ME = "file to me";
    private static String EMAIL_TEXT = "text";

    private Notes selectedItem;
    PrefUtils prefUtils;
    String email_id = "";

    private RelativeLayout relativeLayout;

    TextView empty_list_msg;

    private DbReceiver dbReceiver;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewNoteDialog();
            }
        });

        prefUtils = new PrefUtils(this);

        if (prefUtils.hasKey("email_id"))
            email_id = prefUtils.getStringPreference("email_id");


        notesList = new ArrayList<>();

        recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setHasFixedSize(true);

        empty_list_msg = findViewById(R.id.empty_list_msg);

        relativeLayout = findViewById(R.id.relativeLayout);

        adapter = new NotesAdapter(this, notesList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notes notes = notesList.get(position);
                Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
                intent.putExtra(Const.INTENT_ACTION_NOTES, Const.ACTION_EDIT_NOTE);
                intent.putExtra("clickedRowData", notes);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                selectedItem = notesList.get(position);
                openOptionsDialog();
            }
        }));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof NotesAdapter.NViewHolder){
                    final Notes deletedItem = notesList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    adapter.removeItem(viewHolder.getAdapterPosition());

                    Snackbar snackbar = Snackbar.make(relativeLayout, "Note Removed from list",Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.show();
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((NotesAdapter.NViewHolder) viewHolder).viewForeground;

                getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                final View foregroundView = ((NotesAdapter.NViewHolder) viewHolder).viewForeground;
                final View backgroundView = ((NotesAdapter.NViewHolder) viewHolder).viewBackground;
                backgroundView.setVisibility(View.VISIBLE);

                getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null){
                    final View foregroundView = ((NotesAdapter.NViewHolder) viewHolder).viewForeground;

                    getDefaultUIUtil().onSelected(foregroundView);
                }
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                final View foregroundView = ((NotesAdapter.NViewHolder) viewHolder).viewForeground;

                getDefaultUIUtil().clearView(foregroundView);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setDbReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSearch)
            updateNoteList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dbReceiver);
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

    private void openOptionsDialog(){
        try {
            String[] options;

            if (!email_id.equals(""))
                options = new String[]{"1. Text","2. File attachment","3. File attachment" + "(to " + email_id + ")"};
            else {
                options = new String[]{"1. Text","2. File attachment"};
            }

            builder = new AlertDialog.Builder(this);
            builder.setTitle("Share or Save - choose format");
            builder.setItems(options, actionListener);
            builder.setNegativeButton("Cancel", null);
            builder.create();

            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private final DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which){
                case 0:
                    checkStoragePermissions();
                    sendNoteToEmail(EMAIL_TEXT);
                    break;
                case 1:
                    checkStoragePermissions();
                    sendNoteToEmail(EMAIL_FILE);
                    break;
                case 2:
                    checkStoragePermissions();
                    sendNoteToEmail(EMAIL_FILE_TO_ME);
                    break;
                default:
                    break;
            }
        }
    };

    String filename;
    private void sendNoteToEmail(String type){

        if (selectedItem != null){

            filename = "note_"+selectedItem.getId()+".txt";
            String note = selectedItem.getNote();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            email.setType("text/plain");
            email.putExtra(Intent.EXTRA_SUBJECT, "From NoteWorld");

            if (type.equals(EMAIL_FILE)) {
                email.putExtra(Intent.EXTRA_TEXT, "File Attached");

                Uri uri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName()
                                + ".provider", CommonUtilities.getAttachment(note, filename));

                if (uri != null) {
                    email.putExtra(Intent.EXTRA_STREAM, uri);
                }
            }
            else if (type.equals(EMAIL_FILE_TO_ME)) {
                email.putExtra(Intent.EXTRA_EMAIL,new String[]{email_id});
                email.putExtra(Intent.EXTRA_TEXT, "File Attached");

                Uri uri = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName()
                                + ".provider", CommonUtilities.getAttachment(note, filename));

                if (uri != null) {
                    email.putExtra(Intent.EXTRA_STREAM, uri);
                }
            }
            else{
                email.putExtra(Intent.EXTRA_TEXT, note);
            }

            startActivityForResult(Intent.createChooser(email, "Choose one"), 11);
        }
    }

    private void checkStoragePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)){
                Toast.makeText(this, "To Send this note as file NoteWorld app needs to access device storage", Toast.LENGTH_LONG).show();
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.STORAGE}, 22);
            }
        }else {
            Toast.makeText(this, "Permissions already granted", Toast.LENGTH_LONG).show();
        }
    }

    private void openNewNoteDialog() {
        try {

            Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
            intent.putExtra(Const.INTENT_ACTION_NOTES, Const.ACTION_NEW_NOTE);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateNoteList(){
        try {

            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.FETCH);
            intent.putExtra("table", Const.TABLE_NOTES);
            startService(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateNote(final Notes note){
        try {

            notes = note;
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.UPDATE);
            intent.putExtra("table", Const.TABLE_NOTES);
            intent.putExtra("notesObj",notes);
            startService(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteNote(Notes item){
        try {

            notes = item;
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.DELETE);
            intent.putExtra("table", Const.TABLE_NOTES);
            intent.putExtra("notesObj",notes);
            startService(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateView(){
       if (!notesList.isEmpty()) {
           empty_list_msg.setVisibility(View.GONE);
           recyclerView.setVisibility(View.VISIBLE);

            adapter = new NotesAdapter(this,notesList,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
       }else {
           recyclerView.setVisibility(View.GONE);
           empty_list_msg.setVisibility(View.VISIBLE);
       }
    }

    private void searchNotes(String query){
        // Filtering list in the RecyclerView
        adapter = new NotesAdapter(this,notesList,this);
        adapter.getFilter().filter(query);
        recyclerView.setAdapter(adapter);
        isSearch = false;
    }

    @Override
    public void onNoteSelected(Notes note) {
    }

    private class DbReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();

                notesList = bundle.getParcelableArrayList("list");
                updateView();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem action_save = menu.findItem(R.id.action_save);
        action_save.setVisible(false);
        action_search = menu.findItem(R.id.action_search);
        action_search.setVisible(true);
        MenuItem action_empty = menu.findItem(R.id.action_empty);
        action_empty.setVisible(true);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) action_search.getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                isSearch = true;
                searchNotes(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                isSearch = true;
                searchNotes(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_about:
                Intent about = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK && resultCode == 11){
            if (filename != null) {
                boolean deleted = CommonUtilities.deleteAttachment(filename);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
