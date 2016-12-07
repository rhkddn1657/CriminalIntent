package com.bignerdranch.android.criminallntent;

import java.util.Date;
import java.util.UUID;


public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        // 고유한 식별자를 생성한다.
        mId = UUID.randomUUID();
        mDate = new Date();
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

    public Date getDate() {return mDate;}

    public void setDate(Date date) {mDate = date;}

    public void setSolved(boolean solved) {mSolved = solved;}

    public boolean isSolved() {return mSolved;}
}
