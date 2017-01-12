package com.bignerdranch.android.criminallntent;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO = 2;
    public static final int REQUEST_CALL_PERMISSION = 3;

    private Crime mCrime;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mSuspectCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true); //현재의 "Fragment"가 옵션메뉴에 항목을 추가하겠다는 것을 명시해야한다.
        // 만약 명시를 안한다면 "onCreateOptionsMenu()" 함수는 호출되지 않을 것이다.

    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("test", "beforeTextChanged() :CharSequence:" + s + " start : " + start + ", count : " + count + ", after : " + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("test", "onTextChanged() :CharSequence:" + s + " start : " + start + ", count : " + count);
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("test", "afterTextChanged() : s : " + s);
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });
        //mDateButton.setEnabled(false);//버튼클릭비활성화메소드

        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //범죄 해결 여부 속성 값을 결정.
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_suspect))
                        .setChooserTitle(getString(R.string.send_report))
                        .startChooser(); //15장 챌린지 1번문제
                /*
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                //매번 선택기가 나타나게 할 수 있는 매소드(객체와 선택기의 제목 문자열을 인자로 전달)
                startActivity(i);
                */
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        //Context 넣는값에 getActivity()메소드를 넣었다. 그 이유는 "Fragment"는 "Activity"가 아니기
        //때문에 "context"를 가지지 않는다. 그래서 "Fragment" 구현시 "context"를 이용해야 할 경우
        //에는 getActivity()함수를 이용해야한다.
        mSuspectCallButton = (Button) v.findViewById(R.id.crime_suspect_call);
        mSuspectCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  http:dyy2016.cafe24.com/?p=360 도움
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCallResult = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE);
                    int permissionContactsResult = ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_CONTACTS);

                    if (permissionCallResult != PackageManager.PERMISSION_GRANTED && permissionContactsResult
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest.permission.READ_CONTACTS)) {

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                                dialog.setTitle("권한이 필요합니다.")
                                        .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기 및 관리\" " +
                                                "권한이 필요합니다. 계속하시겠습니까?")
                                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE,
                                                            Manifest.permission.READ_CONTACTS}, REQUEST_CALL_PERMISSION);
                                                }
                                            }
                                        })
                                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                Toast.makeText(getActivity(), "앱 실행을 위해서는 전화 관리 권한을 " +
                                                        "설정해야 합니다", Toast.LENGTH_SHORT);
                                            }
                                        })
                                        .create()
                                        .show();
                            } else {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE,
                                        Manifest.permission.READ_CONTACTS}, REQUEST_CALL_PERMISSION);
                            }
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_CONTACTS}, REQUEST_CALL_PERMISSION);
                            Log.d("test9", "성공");
                        }
                    } else {
                        callPhone();
                    }
                } else {
                    callPhone();
                }
            }


        });
        //장치내에 연락처 앱이 없을때 앱이 중단되는걸 막기위한 대비책.
        //"PackageManager"는 안드로이즈 장치에 설치된 모든 컴포넌트와 그것의 액티비티를 알고 있다.
        //(348p참고)
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //전체 해상도로 찍은 사진을 받으려면 이미지를 저장할 파일 시스템의 위치를 알려줄 필요가 있다.
            //그래서 뒤에 사진을 저장할 위치를 가리키는 "Uri"를 전달.
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);

        updatePhotoView();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("test3", "onAcitivityResult : requestCode:" + requestCode + "| resultCode:" + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
            updateTime();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                String contactId = c.getString(1);

                mCrime.setContactId(contactId);
                mCrime.setSuspect(suspect);

                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
                Crime crime = CrimeLab.get(getContext()).getCrime(crimeId);
                CrimeLab.get(getContext()).deleteCrime(crime);
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // requestPermissions가 실행되면 실행되는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.d("test9", "requestCode:" + requestCode);
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: {
                Log.d("test9", "grantResults[0]:" + grantResults[0] + "grantResult[1]:" + grantResults[1]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("test9", "성공2");
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Log.d("test9", "성공3");
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                                != PackageManager.PERMISSION_GRANTED) {
                            Log.d("test9", "성공4");
                            callPhone();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "권한 요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void callPhone() {
        Log.d("test9", "callPhone");
        Uri contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryField = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String whereClause = ContactsContract.CommonDataKinds.Phone._ID + " = ?";
        String args[] = new String[]{mCrime.getContactId()};

        Cursor c = getActivity().getContentResolver()
                .query(contentUri, queryField, whereClause, args, null);

        try {
            if (c.getCount() == 0) {
                return;
            }
            c.moveToFirst();
            String number = c.getString(0);
            Log.d("test10", number);
            Uri phoneNumber = Uri.parse("tel:" + number);
            Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
            startActivity(intent);
        } finally {
            c.close();
        }
    }

    private void updateDate() {

        String dateFormat = "yyyy년 MMM dd일 EEEE";
        CharSequence dateString = DateFormat.format(dateFormat, mCrime.getDate());
        mDateButton.setText(dateString);


//        DateFormat newDate = new DateFormat();
//        CharSequence newFormat = newDate.format("yyyy년 MMM dd일 EEEE",mCrime.getDate());
//        mDateButton.setText(newFormat);

    }

    private void updateTime() {
        /*
        String dateFormat = "aa H시 mm분";
        CharSequence dateString = DateFormat.format(dateFormat, mCrime.getDate());
        mTimeButton.setText(dateString);
        */
        DateFormat newDate = new DateFormat();
        CharSequence newFormat = newDate.format("aa H시 mm분", mCrime.getDate());
        mTimeButton.setText(newFormat);
    }

    private String getCrimeReport() {
        String solvedString = "제목 없음!";
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "yyyy년 MMM dd일 EEEE aa H시 mm분";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString
                , solvedString, suspect);
        return report;
    }

    private void updatePhotoView() {
//        File file = CrimeLab.get(getActivity()).getPhotoFile(crime);
//        Glide.with(this).load(mPhotoFile.getPath()).into(mPhotoView);

//        http://galmaegi74.tistory.com/2 도움 주소
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            //"Glide"를 사용하면 자동으로 글라이드에서 캐싱을 하기 때문에 신규 이미지가아닌 계속 기존 이미지가
            //보여질수도 있다. 그래서 수동으로 새로고침을 하기위해 ".signature"를 사용한다.
            //-도움주소(Glide 신규이미지 새로고침에 대한 설명.)
            //http://galmaegi74.tistory.com/2
            //https://plus.google.com/111427815590592024590/posts/Qusy9fiwkkV
            Glide.with(this).load(mPhotoFile.getPath())
            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .into(mPhotoView );
//            Bitmap bitmap = PictureUtils.getScaledBitmap(
//                    mPhotoFile.getPath(), getActivity());
//            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
