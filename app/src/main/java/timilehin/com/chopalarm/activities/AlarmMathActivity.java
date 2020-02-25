package timilehin.com.chopalarm.activities;

import androidx.fragment.app.Fragment;

import timilehin.com.chopalarm.fragments.AlarmMathFragment;

public class AlarmMathActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return AlarmMathFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}
