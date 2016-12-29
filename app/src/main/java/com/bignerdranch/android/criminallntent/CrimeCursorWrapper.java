package com.bignerdranch.android.criminallntent;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.bignerdranch.android.criminallntent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rhkdd on 2016-12-29.
 */

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor); //내가 읽고 싶은 데이터를 커서에 정보를 넣은 뒤 그 커서를
        // CursorWrapp클래스에서의 cursor에 정보를 대입시킨다. 그리고 나서 getColumnIndex를 호출해서
        //찾고싶은 열값을 넣어주면 커서에 있는 정보에서 그 열값에 존재하는 값을 가져온다.
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }

}
