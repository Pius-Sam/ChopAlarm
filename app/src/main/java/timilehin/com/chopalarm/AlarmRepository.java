package timilehin.com.chopalarm;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import timilehin.com.chopalarm.room.AlarmRoomDatabase;

public class AlarmRepository {

    private static final String TAG = "AlarmRepository";
    private static AlarmRepository sAlarmRepository;

    private AlarmDao mAlarmDao;
    private LiveData<List<Alarm>> mAlarms;
    private MutableLiveData<Alarm> alarmResult =
            new MutableLiveData<>();
    private static Alarm mAlarm;
    private static int mSize;

    public static AlarmRepository getInstance(Application application) {
        if (sAlarmRepository == null) {
            sAlarmRepository = new AlarmRepository(application);
        }

        return sAlarmRepository;
    }

    private AlarmRepository(Application application) {
        AlarmRoomDatabase db = AlarmRoomDatabase.getDatabase(application);
        mAlarmDao = db.alarmDao();
        mAlarms = mAlarmDao.getAlarms();
//        mAlarm = mAlarmDao.getAlarm();
    }

    LiveData<List<Alarm>> getAlarms() {return mAlarms;}

    MutableLiveData<Alarm> getAlarmResult() {return alarmResult;}

    public int getSize() {
        new sizeAsyncTask(mAlarmDao).execute();
        return  mSize;
    }

    private static class sizeAsyncTask extends AsyncTask<Void, Void, Integer> {
        private AlarmDao mAsyncTaskDao;

        sizeAsyncTask (AlarmDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.getSize();
        }

        @Override
        protected void onPostExecute(Integer size) {
            super.onPostExecute(size);
            mSize = size;
        }
    }

    private void asyncFinished(Alarm result) {
        Log.d(TAG, "asyncFinished: on Thread: " +Thread.currentThread().getName());
        alarmResult.setValue(result);
    }

    void getAlarm(String alarmUid) {
         getAsyncTask task = new getAsyncTask(mAlarmDao);
         task.delegate = this;
         task.execute(alarmUid);
    }

    public void addAlarm(Alarm alarm) {
        new insertAsyncTask(mAlarmDao).execute(alarm);
    }

    private static class getAsyncTask extends AsyncTask<String, Void, Alarm> {
        private AlarmDao mAsyncTaskDao;
        private AlarmRepository delegate = null;

        getAsyncTask (AlarmDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Alarm doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: getAsyncTask: on thread: " + Thread.currentThread().getName());
            return mAsyncTaskDao.getAlarm(strings[0]);
        }

        @Override
        protected void onPostExecute(Alarm alarm) {

            Log.d(TAG, "onPostExecute: alarm get");
            delegate.asyncFinished(alarm);
        }

        @Override
        protected void onCancelled(Alarm alarm) {
            super.onCancelled(alarm);
            delegate.asyncFinished(alarm);
        }
    }

    private static class insertAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao mAsyncTaskDao;

        insertAsyncTask (AlarmDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.addAlarm(params[0]);
            return null;
        }
    }

    public void updateAlarm(Alarm alarm) {new updateAsyncTask(mAlarmDao).execute(alarm);}

    private static class updateAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao mAsyncTaskDao;

        updateAsyncTask(AlarmDao dao) {mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Alarm... alarms) {
            mAsyncTaskDao.updateAlarm(alarms[0]);
            return null;
        }
    }

    private static class deleteAlarmAsyncTask extends AsyncTask<Alarm, Void, Void> {
        private AlarmDao mAsyncTaskDao;

        deleteAlarmAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Alarm... params) {
            mAsyncTaskDao.deleteAlarm(params[0]);
            return null;
        }
    }

    public void deleteAlarm(Alarm alarm) {
        new deleteAlarmAsyncTask(mAlarmDao).execute(alarm);
    }
}
