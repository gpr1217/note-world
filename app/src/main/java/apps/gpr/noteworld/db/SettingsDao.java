package apps.gpr.noteworld.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import apps.gpr.noteworld.model.Settings;

/**
 * Dao is the data access object contains CRUD operation abstract methods using annotations
 */

@Dao
public interface SettingsDao {

    @Insert
    void insertSettings(Settings settings);

    @Query("SELECT * FROM Settings WHERE id = :settingsId")
    Settings getSettingsById(int settingsId);

    @Query("SELECT * FROM Settings ORDER BY created_date DESC")
    List<Settings> getSettingsList();

    @Update
    void updateSettings(Settings settings);

    @Query("UPDATE Settings SET passcode = :passcode, modified_date = :timeStamp WHERE id = :settingsId")
    void updateSettingsByColumn(int settingsId,String passcode,String timeStamp);
}
