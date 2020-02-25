package timilehin.com.chopalarm;

/*
 * Singleton class the holds the database of the alarm.
 * It does the database querying, updating, and deleting
 */

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.UUID;

public class AlarmViewModel extends AndroidViewModel {
    AlarmRepository mRepository;

    private LiveData<List<Alarm>> mAlarms;
    private MutableLiveData<Alarm> alarmResult;

    public AlarmViewModel(Application application) {
        super(application);
        mRepository = AlarmRepository.getInstance(application);
        mAlarms = mRepository.getAlarms();
        alarmResult = mRepository.getAlarmResult();
    }


    public LiveData<List<Alarm>> getAlarms() {

        return mAlarms;
    }

    public MutableLiveData<Alarm> getAlarmResult() {
        return alarmResult;
    }

    public void getAlarm(String alarmId) {
        mRepository.getAlarm(alarmId);
    }

    public void updateAlarm(Alarm alarm) {
        mRepository.updateAlarm(alarm);
    }

    public void addAlarm(Alarm alarm) {
        mRepository.addAlarm(alarm);
    }

    public void deleteAlarm(Alarm alarm) {
        mRepository.deleteAlarm(alarm);
    }

    public int getSize() {
        //return mAlarms.getValue().size();

        return mRepository.getSize();
    }
}
