package com.bignerdranch.android.criminallntent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Created by rhkdd on 2016-12-18.
 */

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android." +
            "criminalintent.crime_id";
    private static final int REQUEST_CONTACT = 1;
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private String Id = null;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Log.d("test","getItem position:" + position);
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
    public void askPermission(String contactId) {
        Id = contactId;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CrimePagerActivity.this, new String[]{android.Manifest
                    .permission.READ_CONTACTS}, REQUEST_CONTACT);

        } else {
            callPhone();
        }
    }

    private void callPhone() {
        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String projection[] = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String whereClause = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?";
        String args[] = new String[]{Id};

        Cursor c = getContentResolver().query(contentUri, projection, whereClause, args, null);
        try {
            if (c.getCount() == 0) {

                return;
            }
            c.moveToFirst();
            String number = c.getString(0);
            Uri phoneNumber = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(phoneNumber);
            startActivity(intent);

        } finally {
            c.close();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]
            grantResults) {
        switch (requestCode) {
            case REQUEST_CONTACT:
                //
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //..........
                } else {

                    callPhone();
                }
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[], @NonNull int[] grantResults) {
////        Log.d("test9", "requestCode:" + requestCode);
//
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//        }
//    }

}
