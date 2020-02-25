package timilehin.com.chopalarm.activities;

import androidx.fragment.app.Fragment;

import timilehin.com.chopalarm.fragments.AlarmFragment;


public class AlarmActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AlarmFragment.newInstance();
    }
}

