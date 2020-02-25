package timilehin.com.chopalarm.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import timilehin.com.chopalarm.Alarm;
import timilehin.com.chopalarm.AlarmDao;

@Database(entities = {Alarm.class}, version = 3)
public abstract class AlarmRoomDatabase extends RoomDatabase {

    private static AlarmRoomDatabase INSTANCE;

    public abstract AlarmDao alarmDao();

    /**
     * Migrate from:
     * version 1 - using the SQLiteDatabase API
     * to
     * version 2 - using Room
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        // Since I didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE alarms_new (alarmid TEXT NOT NULL, hour INTEGER NOT NULL, minute INTEGER NOT NULL," +
                            " repeat INTEGER NOT NULL, daysoftheweek TEXT, ison INTEGER NOT NULL, difficulty INTEGER NOT NULL," +
                            " tone TEXT, vibrate INTEGER NOT NULL, snooze INTEGER NOT NULL, PRIMARY KEY(alarmid))");
            database.execSQL("INSERT INTO alarms_new (alarmid, hour, minute, repeat, daysoftheweek," +
                    " ison, difficulty, tone, vibrate, snooze) SELECT alarmid, hour, minute, repeat, daysoftheweek," +
                    " ison, difficulty, tone, vibrate, snooze FROM alarms");
            database.execSQL("DROP TABLE alarms");
            database.execSQL("ALTER TABLE alarms_new RENAME TO alarms");
        }
    };



    public static AlarmRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (AlarmRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlarmRoomDatabase.class, "alarm_database" )
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
