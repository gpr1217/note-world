package apps.gpr.noteworld.core;

import android.arch.persistence.room.Room;
import android.content.Context;

import apps.gpr.noteworld.db.NotesDatabase;

/**
 * Singleton for Database abstract class, creates only one instance and can be used overall application
 */
class DatabaseManager {

    private static final String DATABASE_NAME = "notes_db";
    private static NotesDatabase notesDatabase;

    private DatabaseManager() {
        if (notesDatabase != null)
            throw new RuntimeException("Error: Call using getInstance");
    }

    public synchronized static NotesDatabase getInstance(Context context){
        if (notesDatabase == null) {
            notesDatabase = buildDatabase(context);
        }
        return notesDatabase;
    }

    private static NotesDatabase buildDatabase(Context context){
        // Deleting database each time to initialize each time app is installed - avoid version upgrade issues
        //context.getApplicationContext().deleteDatabase(DATABASE_NAME);
        notesDatabase = Room.databaseBuilder(context,NotesDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        return notesDatabase;
    }

}
