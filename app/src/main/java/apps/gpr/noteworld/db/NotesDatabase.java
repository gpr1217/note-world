package apps.gpr.noteworld.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import apps.gpr.noteworld.model.Notes;
import apps.gpr.noteworld.model.Settings;

/**
 * This is database class for whole app; entities = table name(Model class which getters and setters)
 * NotesDao is database access (interface which has CRUD operations)
 *
 */

@Database(entities = {Notes.class,Settings.class}, version = 4, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase{
    public abstract NotesDao notesDao();

    public abstract SettingsDao settingsDao();
}
