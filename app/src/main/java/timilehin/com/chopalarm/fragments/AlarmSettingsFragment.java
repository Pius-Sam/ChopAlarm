package timilehin.com.chopalarm.fragments;

/*
* This is the fragment that contains the settings UI of the program.
* Called when the add('+') button is pressed or when an existing alarm
* is pressed.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import timilehin.com.chopalarm.Alarm;
import timilehin.com.chopalarm.AlarmViewModel;
import timilehin.com.chopalarm.R;
import timilehin.com.chopalarm.activities.AlarmMathActivity;


public class AlarmSettingsFragment extends Fragment {

    TextView mTime;
    ToggleButton mRSunTButton;
    ToggleButton mRMonTButton;
    ToggleButton mRTueTButton;
    ToggleButton mRWedTButton;
    ToggleButton mRThuTButton;
    ToggleButton mRFriTButton;
    ToggleButton mRSatTButton;
    Switch mRepeatSwitch;
    Spinner mDifficultySpinner;
    Spinner mToneSpinner;
    EditText mSnoozeText;
    Switch mVibrateSwitch;
    Button mTestButton;


    Alarm mAlarm;
    Alarm mTestAlarm;
    String mId, mRepeat;
    boolean mAdd = false;

    Uri[] mAlarmTones;

    private AlarmViewModel alarmViewModel;

    private static final int REQUEST_TIME = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TEST = 1;


    private static final String TAG = "AlarmSettings";

    public static AlarmSettingsFragment newInstance() {
        return new AlarmSettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
//    }

    //Creates view and initializes button
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mId = extras.getString(AlarmFragment.GET_ALARM);
        }

        alarmViewModel = ViewModelProviders.of(getActivity()).get(AlarmViewModel.class);
        View v = inflater.inflate(R.layout.fragment_alarm_settings, parent, false);


        //mAlarmViewModel = AlarmViewModel.get(getActivity());


        if (!mId.isEmpty()) {
            alarmViewModel.getAlarm(mId);

            alarmViewModel.getAlarmResult().observe(getViewLifecycleOwner(), new Observer<Alarm>() {
                @Override
                public void onChanged(@Nullable Alarm alarm) {
//                    Alarm alarm1 = alarm;
                    mAlarm = alarm;
                }
            });
//            mAlarm = alarmViewModel.getAlarmResult().getValue();

            alarmViewModel.updateAlarm(mAlarm);
//            mAlarm = AlarmRepository.getInstance(getActivity()).getAlarm(mId);
        } else {
            mAlarm = new Alarm();
            Calendar cal = Calendar.getInstance();

            mAlarm.setHour(cal.get(Calendar.HOUR_OF_DAY));
            mAlarm.setMinute(cal.get(Calendar.MINUTE));
            mAdd = true;
        }

        if (savedInstanceState != null) {
            mAlarm.setHour(savedInstanceState.getInt("hour", 0));
            mAlarm.setMinute(savedInstanceState.getInt("minute", 0));
            mAlarm.setRepeatDays(savedInstanceState.getString("repeat"));
            mAlarm.setRepeat(savedInstanceState.getBoolean("repeatweekly"));
        }


        mRepeat = mAlarm.getRepeat();

        mTime = v.findViewById(R.id.settings_time);
        mTime.setText(mAlarm.getFormatTime());
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mAlarm.getHour(), mAlarm.getMinute());
                dialog.setTargetFragment(AlarmSettingsFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mRSunTButton = v.findViewById(R.id.set_repeat_sun);
        mRSunTButton.setChecked(mRepeat.charAt(Alarm.SUN) == 'T');
        mRSunTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRSunTButton.setChecked(mRepeat.charAt(Alarm.SUN) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.SUN) == 'T') {
                    sb.setCharAt(Alarm.SUN, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.SUN, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRMonTButton = v.findViewById(R.id.set_repeat_mon);
        mRMonTButton.setChecked(mRepeat.charAt(Alarm.MON) == 'T');
        mRMonTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRMonTButton.setChecked(mRepeat.charAt(Alarm.MON) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.MON) == 'T') {
                    sb.setCharAt(Alarm.MON, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.MON, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRTueTButton = v.findViewById(R.id.set_repeat_tue);
        mRTueTButton.setChecked(mRepeat.charAt(Alarm.TUE) == 'T');
        mRTueTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRTueTButton.setChecked(mRepeat.charAt(Alarm.TUE) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.TUE) == 'T') {
                    sb.setCharAt(Alarm.TUE, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.TUE, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRWedTButton = v.findViewById(R.id.set_repeat_wed);
        mRWedTButton.setChecked(mRepeat.charAt(Alarm.WED) == 'T');
        mRWedTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRWedTButton.setChecked(mRepeat.charAt(Alarm.WED) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.WED) == 'T') {
                    sb.setCharAt(Alarm.WED, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.WED, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRThuTButton = v.findViewById(R.id.set_repeat_thu);
        mRThuTButton.setChecked(mRepeat.charAt(Alarm.THU) == 'T');
        mRThuTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRThuTButton.setChecked(mRepeat.charAt(Alarm.THU) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.THU) == 'T') {
                    sb.setCharAt(Alarm.THU, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.THU, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRFriTButton = v.findViewById(R.id.set_repeat_fri);
        mRFriTButton.setChecked(mRepeat.charAt(Alarm.FRI) == 'T');
        mRFriTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRFriTButton.setChecked(mRepeat.charAt(Alarm.FRI) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.FRI) == 'T') {
                    sb.setCharAt(Alarm.FRI, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.FRI, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRSatTButton = v.findViewById(R.id.set_repeat_sat);
        mRSatTButton.setChecked(mRepeat.charAt(Alarm.SAT) == 'T');
        mRSatTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRSatTButton.setChecked(mRepeat.charAt(Alarm.SAT) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.SAT) == 'T') {
                    sb.setCharAt(Alarm.SAT, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.SAT, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRepeatSwitch = v.findViewById(R.id.settings_repeat_switch);
        mRepeatSwitch.setChecked(mAlarm.isRepeat());
        mRepeatSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatSwitch.setChecked(!mAlarm.isRepeat());
                mAlarm.setRepeat(!mAlarm.isRepeat());
            }
        });


        //Getting system sound files for tone and displaying in spinner
        mToneSpinner = v.findViewById(R.id.settings_tone_spinner);
        List<String> toneItems = new ArrayList<>();
        RingtoneManager ringtoneMgr = new RingtoneManager(getActivity());
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();

        if (alarmsCount == 0) { //if there are no alarms, use notification sounds
            ringtoneMgr.setType(RingtoneManager.TYPE_NOTIFICATION);
            alarmsCursor = ringtoneMgr.getCursor();
            alarmsCount = alarmsCursor.getCount();

            if (alarmsCount == 0) { //if no alarms and notification sounds, finally use ringtones
                ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
                alarmsCursor = ringtoneMgr.getCursor();
                alarmsCount = alarmsCursor.getCount();
            }
        }

        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            Toast.makeText(getActivity(), "No sound files available", Toast.LENGTH_SHORT).show();
        }

        int previousPosition = 0;

        //If there are sound files, add them
        if (alarmsCount != 0) {
            mAlarmTones = new Uri[alarmsCount];

            String currentTone = mAlarm.getAlarmTone();

            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int currentPosition = alarmsCursor.getPosition();
                mAlarmTones[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
                toneItems.add(ringtoneMgr.getRingtone(currentPosition)
                        .getTitle(getActivity()));

                if (currentTone != null &&
                        currentTone.equals(mAlarmTones[currentPosition].toString())) {
                    previousPosition = currentPosition;
                }
            }

        }

        if (toneItems.isEmpty()) {
            toneItems.add("Empty");
        }


        ArrayAdapter<String> toneAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, toneItems);
        mToneSpinner.setAdapter(toneAdapter);
        mToneSpinner.setSelection(previousPosition);

        mDifficultySpinner = v.findViewById(R.id.settings_math_difficulty_spinner);
        String[] difficultyItems = new String[]{"Easy", "Medium", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, difficultyItems);
        mDifficultySpinner.setAdapter(difficultyAdapter);
        mDifficultySpinner.setSelection(mAlarm.getDifficulty());

        mSnoozeText = v.findViewById(R.id.settings_snooze_text);
        mSnoozeText.setText(String.format(Locale.US, "%d",mAlarm.getSnooze()));
        mSnoozeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mAlarm.setSnooze(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVibrateSwitch = v.findViewById(R.id.settings_vibrate_switch);
        mVibrateSwitch.setChecked(mAlarm.isVibrate());
        mVibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrateSwitch.setChecked(!mAlarm.isVibrate());
                mAlarm.setVibrate(!mAlarm.isVibrate());
            }
        });

        mTestButton = v.findViewById(R.id.settings_test_button);
        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestAlarm = new Alarm();
                mTestAlarm.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                if (mAlarmTones.length != 0) {
                    mTestAlarm.setAlarmTone(mAlarmTones[mToneSpinner
                            .getSelectedItemPosition()].toString());
                }
                mTestAlarm.setVibrate(mVibrateSwitch.isChecked());
                mTestAlarm.setSnooze(0);
                alarmViewModel.addAlarm(mTestAlarm);
                //AlarmViewModel.get(getActivity()).addAlarm(mTestAlarm);
                Intent test = new Intent(getActivity(), AlarmMathActivity.class);
                test.putExtra(Alarm.ALARM_EXTRA, mTestAlarm.getId());
                startActivityForResult(test, REQUEST_TEST);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.alarm_settings_menu, menu);
    }

    //Setting the on click action of delete and done on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_settings_done:
                //Setting difficulty + alarm tone
                mAlarm.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                if (mAlarmTones.length != 0) {
                    mAlarm.setAlarmTone(mAlarmTones[mToneSpinner
                            .getSelectedItemPosition()].toString());
                }

                //schedule alarm, update to database and close settings
                if (mAdd) {
                    scheduleAndMessage();
                    alarmViewModel.addAlarm(mAlarm);
                    //AlarmViewModel.get(getActivity()).addAlarm(mAlarm);
                } else {
                   // Alarm oldAlarm = AlarmViewModel.get(getActivity()).getAlarm(mAlarm.getId());
                    alarmViewModel.getAlarm(mAlarm.getId());
                    alarmViewModel.getAlarmResult().observe(getViewLifecycleOwner(), new Observer<Alarm>() {
                        @Override
                        public void onChanged(Alarm alarm) {
//                            Alarm oldAlarm = alarm;

                            if (alarm.isOn()) {
                                alarm.cancelAlarm(getActivity());
                            }
                            scheduleAndMessage();
                            alarmViewModel.updateAlarm(mAlarm);
                            //AlarmViewModel.get(getActivity()).updateAlarm(mAlarm);
                        }
                    });
//                    Alarm oldAlarm = AlarmRepository.getInstance(getActivity()).getAlarm(mAlarm.getId());

                }

                getActivity().finish();
                return true;
            case R.id.fragment_settings_delete:
                if (!mAdd) {
                    //if the settings was not reached by the add button,
                    //then it needs to be deleted off the database
                    if (mAlarm.isOn()) {
                        mAlarm.cancelAlarm(getActivity());
                    }
                    alarmViewModel.deleteAlarm(mAlarm);
                    //AlarmViewModel.get(getActivity()).deleteAlarm(mAlarm);
                }

                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int min = data.getIntExtra(TimePickerFragment.EXTRA_MIN, 0);
            mAlarm.setHour(hour);
            mAlarm.setMinute(min);
            mTime.setText(mAlarm.getFormatTime());
        } else {
            if (requestCode == REQUEST_TEST) {
                alarmViewModel.deleteAlarm(mTestAlarm);
                //AlarmViewModel.get(getActivity()).deleteAlarm(mTestAlarm);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("hour", mAlarm.getHour());
        savedInstanceState.putInt("minute", mAlarm.getMinute());
        savedInstanceState.putString("repeat", mAlarm.getRepeat());
        savedInstanceState.putBoolean("repeatweekly", mAlarm.isRepeat());
    }

    public void scheduleAndMessage() {
        //schedule it and create a toast
        if (mAlarm.scheduleAlarm(getActivity())) {
            Toast.makeText(getActivity(), mAlarm.getTimeLeftMessage(getActivity()),
                    Toast.LENGTH_SHORT).show();
            mAlarm.setIsOn(true);
        } else {
            mAlarm.setIsOn(false);
        }
    }
}