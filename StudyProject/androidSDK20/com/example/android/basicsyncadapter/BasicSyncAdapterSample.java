package com.example.android.basicsyncadapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gtr.studyproject.activity.R;

/**
 * Activity for holding EntryListFragment.
 */
public class BasicSyncAdapterSample extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);
    }
}
