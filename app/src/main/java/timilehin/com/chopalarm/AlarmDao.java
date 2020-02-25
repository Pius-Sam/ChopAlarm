package timilehin.com.chopalarm;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addAlarm(Alarm alarm);

    @Update
    void updateAlarm(Alarm... alarm);

    @Delete
    void deleteAlarm(Alarm alarm);

    @Query("SELECT * FROM alarms WHERE alarmid = :alarmUid LIMIT 1")
    Alarm getAlarm(String alarmUid);

    @Query("SELECT * FROM alarms")
    LiveData<List<Alarm>> getAlarms();

    @Query("SELECT COUNT(*) FROM alarms")
    public Integer getSize();
}
