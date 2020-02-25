package timilehin.com.chopalarm;

/*
* Receives the intent from AlarmManager to start the math activity
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import java.util.UUID;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        service.putExtra(Alarm.ALARM_EXTRA, String.valueOf(intent.getExtras().get(Alarm.ALARM_EXTRA)));
        AlarmService.enqueueWork(context, service);
    }

}
