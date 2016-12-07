package com.bignerdranch.android.criminallntent;

import java.util.Date;
import java.util.UUID;


public class Crime {
    private UUID mId;
    private String mTitle;


    public Crime() {
        // 고유한 식별자를 생성한다.
        mId = UUID.randomUUID();

    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
