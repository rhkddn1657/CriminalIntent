package com.bignerdranch.android.criminallntent;

import android.support.v4.app.Fragment;

/**
 * Created by rhkdd on 2016-12-13.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
