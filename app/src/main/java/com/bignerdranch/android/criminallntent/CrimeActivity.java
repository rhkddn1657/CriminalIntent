package com.bignerdranch.android.criminallntent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.UUID;


public class CrimeActivity extends SingleFragmentActivity{

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        //위에서 newIntent에서 정보를담은 intent를 getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        //을 통해서 가져와서 그 인텐트에 들어가있는 정보 UUID값을 빼낸후, 내가 실행시킬
        //프레그먼트로 해당 UUID값을 보낸다.
        //주의할점이 있는데 인자번들을 프래그먼트에 추가할려면 프래그먼트가 액티비티에 추가되기
        //전에, 인자번들를 프래그먼트에 추가해야된다.
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
   

}
