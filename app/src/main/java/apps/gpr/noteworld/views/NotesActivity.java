package apps.gpr.noteworld.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.R;
import apps.gpr.noteworld.adapters.NotesAdapter;
import apps.gpr.noteworld.core.DatabaseService;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.utils.BaseActivity;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;

/**
 *  TODO - Implement ActionBar and change each row display time format
 *  TODO - if date is today's display today time else date time and yesterday for previous days
 *  TODO - Delete on left slide of row
 *  TODO - Long press email and share
 */
public class NotesActivity extends AppCompatActivity implements NotesAdapter.NotesAdapterListener{

    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Notes> notesList;
    private Notes notes = new Notes();
    MenuItem action_search;
    boolean isSearch = false;

    DbReceiver dbReceiver;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewNoteDialog();
            }
        });

        notesList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.notes_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notes notes = notesList.get(position);
                Intent intent = new Intent(getApplicationContext(),EditNoteActivity.class);
                intent.putExtra("clickedRowData", notes);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                openOptionsDialog();
            }
        }));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                CommonUtilities.log("onMove");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                CommonUtilities.log("onSwiped");
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                CommonUtilities.log("onChildDraw");
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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
        CommonUtilities.log("onPause 4");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(dbReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
            String[] options = {"Email", "Save to drive"};
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose");
            builder.setItems(options, actionListener);
            builder.setNegativeButton("Cancel", null);
            builder.create();

            builder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which){
                case 0:
                    // Email
                    CommonUtilities.log("Email");
                    break;
                case 1:
                    // save to drive
                    CommonUtilities.log("Drive");
                    break;
                default:
                    break;
            }
        }
    };

    private void openNewNoteDialog() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_new_note);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            dialog.show();

            final EditText new_note = dialog.findViewById(R.id.new_note);
            new_note.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            final Button btn_save = dialog.findViewById(R.id.btn_save);
            final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String note = new_note.getText().toString();
                    saveNote(note);
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateNoteList(){
        try {
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.FETCH);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void saveNote(final String note) {
        try {
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.INSERT);
            intent.putExtra("note",note);
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNote(final Notes note){
        try {
            notes = note;
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.UPDATE);
            intent.putExtra("notesObj",notes);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteNote(final Notes note){
        try {
            notes = note;
            Intent intent = new Intent(this, DatabaseService.class);
            intent.putExtra("type", Const.DELETE);
            intent.putExtra("notesObj",notes);
            startService(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateView(){
        if (!notesList.isEmpty()) {
            /*for (Notes notes : notesList) {
                CommonUtilities.log("Notes::saveNote", notes.toString());
            }*/
            adapter = new NotesAdapter(this,notesList,this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
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
        CommonUtilities.log("Note Selected - ",note.getNote());
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
                //CommonUtilities.log("SearchQuery::onQueryTextSubmit",s);
                isSearch = true;
                searchNotes(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                isSearch = true;
                //CommonUtilities.log("SearchQuery::onQueryTextChange",s);
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
                return true;

            case R.id.action_about:
                //Toast.makeText(getApplicationContext(), "About feature is in progress", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
