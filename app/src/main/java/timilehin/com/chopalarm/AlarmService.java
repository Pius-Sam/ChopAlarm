package timilehin.com.chopalarm;

/*
* Gets called by wakefulbroadcastreceiver and completes the wakelock.
* This class in turn calls the math activity.
 */
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import timilehin.com.chopalarm.activities.AlarmMathActivity;

public class AlarmService extends JobIntentService {

    // Service unique ID
    static final int SERVICE_JOB_ID = 50;

//    public AlarmService() {
//        super("AlarmService");
//    }

    public static void enqueueWork(Context context, Intent service) {
        enqueueWork(context,AlarmService.class, SERVICE_JOB_ID, service);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        onHandleIntent(intent);
    }


    public void onHandleIntent(Intent intent) {

        Intent mathActivity = new Intent(this, AlarmMathActivity.class);
        mathActivity.putExtra(Alarm.ALARM_EXTRA, String.valueOf(intent.getExtras().get(Alarm.ALARM_EXTRA)));
        mathActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mathActivity);
    }

}
