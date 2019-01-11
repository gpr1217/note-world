package apps.gpr.noteworld.core;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.db.NotesDatabase;
import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.model.Settings;
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;

public class DatabaseService extends IntentService {

    public DatabaseService(String name) {
        super(name);
    }

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            NotesDatabase notesDatabase = DatabaseManager.getInstance(this);

            String type = intent.getStringExtra("type");
            String dataTable = intent.getStringExtra("table");

            if (dataTable.equals(Const.TABLE_SETTINGS)){
                List<Settings> settingsData;
                Intent in = new Intent(Const.FILTER_ACTION_SETTINGS_DATA);
                switch (type){
                    case Const.INSERT:
                        Settings settings = intent.getParcelableExtra("settingsObj");
                        notesDatabase.settingsDao().insertSettings(settings);

                        in.putExtra("message","Insert Settings Success");
                        break;
                    case Const.FETCH:
                        in.putExtra("message","Settings Data Fetched");
                        break;
                }
                settingsData = notesDatabase.settingsDao().getSettingsList();
                in.putExtra("list", (ArrayList) settingsData);
                LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            } else {
                List<Notes> list;

                Intent in = new Intent(Const.FILTER_ACTION_DATA);
                switch (type) {
                    case Const.FETCH:
                        in.putExtra("message", "Data fetched!");
                        break;
                    case Const.INSERT: {
                        String note = intent.getStringExtra("note");

                        Notes notes = new Notes();
                        notes.setNote(note);
                        notes.setStatus("1");
                        notes.setTimeStamp(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE_TIME));
                        notesDatabase.notesDao().insertNote(notes);

                        in.putExtra("message", "Insert Success");
                        break;
                    }
                    case Const.UPDATE: {
                        Notes notes = intent.getParcelableExtra("notesObj");
                        notesDatabase.notesDao().updateNote(notes);

                        in.putExtra("message", "Update Success");
                        break;
                    }
                    case Const.DELETE: {
                        Notes notes = intent.getParcelableExtra("notesObj");
                        notesDatabase.notesDao().deleteNoteByStatus(notes.getId(), "0", CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE_TIME));

                        in.putExtra("message", "Delete Success");
                        break;
                    }
                }

                list = notesDatabase.notesDao().getAllNotes();
                in.putExtra("list", (ArrayList) list);
                LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
