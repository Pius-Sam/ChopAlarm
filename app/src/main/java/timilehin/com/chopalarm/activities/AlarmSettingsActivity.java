package timilehin.com.chopalarm.activities;

import androidx.fragment.app.Fragment;

import timilehin.com.chopalarm.fragments.AlarmSettingsFragment;

public class AlarmSettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AlarmSettingsFragment.newInstance();
    }
}