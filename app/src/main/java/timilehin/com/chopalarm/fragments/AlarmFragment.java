package timilehin.com.chopalarm.fragments;

/*
*   This class is the fragment of the main activity. Contains the list of alarm.
*   This initializes the RecyclerView, its adapter and the layout buttons
 */

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import timilehin.com.chopalarm.Alarm;
import timilehin.com.chopalarm.AlarmViewModel;
import timilehin.com.chopalarm.R;
import timilehin.com.chopalarm.activities.AlarmSettingsActivity;


public class AlarmFragment extends Fragment {

    private RecyclerView mAlarmRecyclerView;
    private RelativeLayout mEmptyView;
    private AlarmAdapter mAdapter;

    private AlarmViewModel alarmViewModel;

    public static final String GET_ALARM = "GET";

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_alarm_list, container, false);

        mAlarmRecyclerView = v.findViewById(R.id.alarm_recycler_view);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyView = v.findViewById(R.id.alarm_empty_view);

        updateUI();

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.alarm_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_add_alarm_menu:
                Intent intent = new Intent(getContext(), AlarmSettingsActivity.class);
                intent.putExtra(GET_ALARM, ""); //Empty indicates new alarm
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }


    // UpdateUI is called when the program returns to this fragment
    // Used to update the alarm information on the UI
    public void updateUI() {
        alarmViewModel = ViewModelProviders.of(getActivity()).get(AlarmViewModel.class);

        final LiveData<List<Alarm>> alarms = alarmViewModel.getAlarms();

        if (mAdapter == null) {
            mAdapter = new AlarmAdapter(alarms);
            mAlarmRecyclerView.setAdapter(mAdapter);
        } else {
            alarmViewModel.getAlarms().observe(getViewLifecycleOwner(), new Observer<List<Alarm>>() {
                @Override
                public void onChanged(@Nullable final List<Alarm> alarmList) {
                    mAdapter.setAlarms(alarms);
                }
            });
            mAdapter.notifyDataSetChanged();
        }

        if (alarmViewModel.getSize() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

        //Setting the RecyclerView's ViewHolder and Adapter
        private class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Alarm mAlarm;
        private String mRepeat;
        private TextView mTimeTextView;
        private TextView mRepeatTextViewMon;
        private TextView mRepeatTextViewTue;
        private TextView mRepeatTextViewWed;
        private TextView mRepeatTextViewThu;
        private TextView mRepeatTextViewFri;
        private TextView mRepeatTextViewSat;
        private TextView mRepeatTextViewSun;
        private Switch mSwitchButton;
        private RelativeLayout mLayout;

        public AlarmHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mLayout = itemView.findViewById(R.id.list_item);
            mTimeTextView = itemView.findViewById(R.id.alarm_time);
            mRepeatTextViewMon = itemView.findViewById(R.id.alarm_repeat_mon);
            mRepeatTextViewTue = itemView.findViewById(R.id.alarm_repeat_tue);
            mRepeatTextViewWed = itemView.findViewById(R.id.alarm_repeat_wed);
            mRepeatTextViewThu = itemView.findViewById(R.id.alarm_repeat_thu);
            mRepeatTextViewFri = itemView.findViewById(R.id.alarm_repeat_fri);
            mRepeatTextViewSat = itemView.findViewById(R.id.alarm_repeat_sat);
            mRepeatTextViewSun = itemView.findViewById(R.id.alarm_repeat_sun);

            mSwitchButton = itemView.findViewById(R.id.alarm_switch_button);
            mSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlarm.setIsOn(!mAlarm.isOn());
                    mSwitchButton.setChecked(mAlarm.isOn());

                    if (mAlarm.isOn()) {
                        if (mAlarm.scheduleAlarm(getActivity())) {
                            Toast.makeText(getActivity(), mAlarm.getTimeLeftMessage(getActivity()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mAlarm.setIsOn(false);
                            mSwitchButton.setChecked(false);
                        }
                    } else {
                        mAlarm.cancelAlarm(getActivity());
                    }

                    alarmViewModel.updateAlarm(mAlarm);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), AlarmSettingsActivity.class);
            intent.putExtra(GET_ALARM, mAlarm.getId());
            startActivity(intent);
        }

        public void bindAlarm(Alarm alarm) {

            mAlarm = alarm;
            mRepeat = alarm.getRepeat();

            int hour = mAlarm.getHour();
            if (hour < 12) {
                mLayout.setBackgroundColor(ContextCompat
                        .getColor(getContext(), R.color.colorSkyBlue));
            } else {
                mLayout.setBackgroundColor(ContextCompat
                        .getColor(getContext(), R.color.colorNightBlue));
            }

            mSwitchButton.setChecked(mAlarm.isOn());

            mTimeTextView.setText(mAlarm.getFormatTime());
            int color;
            if (mAlarm.isRepeat()) {
                color = R.color.colorGold;
            } else {
                color = R.color.colorWhite;
            }
            mTimeTextView.setTextColor(ContextCompat.getColor(getContext(),
                    color));

            if (mRepeat.charAt(Alarm.SUN) == 'T') {
                mRepeatTextViewSun.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewSun.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.MON) == 'T') {
                mRepeatTextViewMon.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewMon.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.TUE) == 'T') {
                mRepeatTextViewTue.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewTue.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.WED) == 'T') {
                mRepeatTextViewWed.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewWed.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.THU) == 'T') {
                mRepeatTextViewThu.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewThu.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.FRI) == 'T') {
                mRepeatTextViewFri.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else
                mRepeatTextViewFri.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.SAT) == 'T') {
                mRepeatTextViewSat.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorGold));
            } else {
                mRepeatTextViewSat.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));
            }


        }
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

        private LiveData<List<Alarm>> mAlarms;

        public AlarmAdapter(LiveData<List<Alarm>> alarms) {
            mAlarms = alarms;
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.alarm_item, parent, false);
            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            Alarm alarm = mAlarms.getValue().get(position);
            holder.bindAlarm(alarm);
        }

        @Override
        public int getItemCount() {
            if (mAlarms.getValue() != null) {
                return mAlarms.getValue().size();
            } else {
                return 0;
            }

        }

        public void setAlarms(LiveData<List<Alarm>> alarms) {
            mAlarms = alarms;
        }
    }
}
