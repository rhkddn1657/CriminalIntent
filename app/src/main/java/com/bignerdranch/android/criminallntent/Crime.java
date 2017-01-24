package com.bignerdranch.android.criminallntent;

import java.util.Date;
import java.util.UUID;


public class Crime {
    private UUID mId;
    private String mTitle;
    private String mContent;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private String mContactId;
    public Crime() {
        // 고유한 식별자를 생성한다.
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
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

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getContactId()  {return mContactId;}

    public void setContactId(String contactId) { mContactId = contactId;}

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public String getContent() {
        return mContent;
    }
    public void setContent(String content) {mContent = content;}
}
