package apps.gpr.noteworld.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import apps.gpr.noteworld.model.Notes;

/**
 * Dao is the data access object contains CRUD operation abstract methods using annotations
 */

@Dao
public interface NotesDao {

    @Insert
    void insertNote(Notes notes);

    @Query("SELECT * FROM Notes WHERE id = :noteId")
    Notes getNoteById(int noteId);

    @Query("SELECT * FROM Notes WHERE status = 1 ORDER BY id DESC")
    List<Notes> getAllNotes();

    @Update
    void updateNote(Notes notes);

    @Query("UPDATE Notes SET note = :note, modifiedTimeStamp = :timeStamp WHERE id = :noteId")
    void updateSpecifiedNote(int noteId,String note,String timeStamp);

    @Query("SELECT * FROM Notes WHERE note LIKE :query")
    List<Notes> searchNotes(String query);

    @Query("UPDATE Notes SET status = :status, modifiedTimeStamp = :timeStamp WHERE id = :noteId")
    void deleteNoteByStatus(int noteId,String status,String timeStamp);

    @Delete
    void deleteNote(Notes notes);
}
