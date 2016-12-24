package com.bignerdranch.android.criminallntent;

import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Created by rhkdd on 2016-12-13.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    //해당 "onCreate"메소드는 생략되어있었는데, 왜 적어 놨냐면,
    //여기 해당 "onCreate"메소드가 상위 클래스 "onCreate"메소드를 호출하고 있음을 보여주기위해서
    //표시해둠.(즉,안드로이드매니페스트에 "CrimeListActivity"가 시작메소드이고, 현재 onCreate()메소드
    //에서 상위클래스의 onCreate()메소드를 호출하고 있으니 상위클래스인 "SingleFragmentActivity"가
    //불려지고 이 클래스의 onCreate()메소드가 실행된다.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
