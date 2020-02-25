package timilehin.com.chopalarm;

/*
* The receiver the receives the boot completed intent when a device successfully boots up.
* Reschedules all enabled alarm since AlarmManager does not retain alarms after a device
* turns off.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import timilehin.com.chopalarm.fragments.AlarmFragment;


public class RebootReceiver extends BroadcastReceiver {
    private AlarmViewModel alarmViewModel;
    @Override
    public void onReceive(Context context, Intent intent) {
        alarmViewModel = ViewModelProviders.of(AlarmFragment.newInstance()).get(AlarmViewModel.class);

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            LiveData<List<Alarm>> alarms = alarmViewModel.getAlarms();
            for (int i = 0; i < alarms.getValue().size(); i++) {
                Alarm alarm = alarms.getValue().get(i);
                if (alarm.isOn()) {
                    alarm.scheduleAlarm(context);
                }
            }
        }
    }
}
