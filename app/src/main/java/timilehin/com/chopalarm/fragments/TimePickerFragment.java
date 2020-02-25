package timilehin.com.chopalarm.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import timilehin.com.chopalarm.R;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_HOUR = "timilehin.com.chopalarm.hour";
    public static final String EXTRA_MIN = "timilehin.com.chopalarm.minute";
    private static final String ARG_HOUR = "hour";
    private static final String ARG_MIN = "min";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(int hour, int minute) {
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR, hour);
        args.putInt(ARG_MIN, minute);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int argHour = getArguments().getInt(ARG_HOUR);
        int argMin = getArguments().getInt(ARG_MIN);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = v.findViewById(R.id.dialog_time_time_picker);

        if (Build.VERSION.SDK_INT < 23) {
            mTimePicker.setCurrentHour(argHour);
            mTimePicker.setCurrentMinute(argMin);
        } else {
            mTimePicker.setHour(argHour);
            mTimePicker.setMinute(argMin);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title).setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour, minute;

                                if (Build.VERSION.SDK_INT < 23) {
                                    hour = mTimePicker.getCurrentHour();
                                    minute = mTimePicker.getCurrentMinute();
                                } else {
                                    hour = mTimePicker.getHour();
                                    minute = mTimePicker.getMinute();
                                }

                                sendResult(Activity.RESULT_OK, hour, minute);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, int hour, int min) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_HOUR, hour);
        intent.putExtra(EXTRA_MIN, min);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

