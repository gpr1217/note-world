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
import apps.gpr.noteworld.utils.CommonUtilities;
import apps.gpr.noteworld.utils.Const;

public class DatabaseService extends IntentService {

    private NotesDatabase notesDatabase;

    public DatabaseService(String name) {
        super(name);
    }

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            CommonUtilities.log("DbReceiver::onHandleIntent", "Processing data and Sending Broadcast Response...");
            notesDatabase = DatabaseManager.getInstance(this);

            String type = intent.getStringExtra("type");
            CommonUtilities.log("DbReceiver::onHandleIntent:data Passed", type);
            List<Notes> list = new ArrayList<Notes>();

            Intent in = new Intent(Const.FILTER_ACTION_DATA);;
            if (type.equals(Const.FETCH))
            {
                in.putExtra("message", "Data fetched!");
            }
            else if (type.equals(Const.INSERT))
            {
                String note = intent.getStringExtra("note");

                Notes notes = new Notes();
                notes.setNote(note);
                notes.setStatus("1");
                notes.setTimeStamp(CommonUtilities.getCurrentDate(Const.FORMAT_DEFAULT_DATE_TIME));
                notesDatabase.notesDao().insertNote(notes);

                in.putExtra("message", "Insert Success");
            }
            else if (type.equals(Const.UPDATE))
            {
                Notes notes = intent.getParcelableExtra("notesObj");
                //notesDatabase.notesDao().updateSpecifiedNote(notes.getId(),notes.getStatus(),currDatetime);
                notesDatabase.notesDao().updateNote(notes);

                in.putExtra("message", "Update Success");
            }
            else if (type.equals(Const.DELETE))
            {
                Notes notes = intent.getParcelableExtra("notesObj");
                notesDatabase.notesDao().deleteNoteByStatus(notes.getId(),"0",notes.getNote());

                in.putExtra("message", "Delete Success");
            }


           /* if (type.equals(Const.SEARCH)){
                String query = intent.getStringExtra("search_query");
                CommonUtilities.log("SearchQuery::onHandleIntent",query);
                list = notesDatabase.notesDao().searchNotes(query);
                for (Notes n : list){
                    CommonUtilities.log("Id = "+n.getId()+" :: Note = "+n.getNote());
                }
                in.putExtra("message", "Search Success");
            }else {
                list = notesDatabase.notesDao().getAllNotes();
            }
*/
            list = notesDatabase.notesDao().getAllNotes();
            in.putExtra("list", (ArrayList) list);
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
