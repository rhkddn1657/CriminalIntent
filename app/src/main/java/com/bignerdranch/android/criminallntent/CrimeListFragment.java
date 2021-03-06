package com.bignerdranch.android.criminallntent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

/**
 * Created by rhkdd on 2016-12-13.
 */

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CALL_PERMISSION = 2 ;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final int REQUEST_CRIME = 1;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private TextView mEmptyView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test2","onCreate() start");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //"FragmentManager"에게 onCreateOptionsMenu()메소드를 호출해야된다는
        //것을 명시적으로 알려주기위한 메소드

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test2","onCreateView() start");

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyView = (TextView) view.findViewById(R.id.empty_crime_view);
        if(savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

//        Fragment ddd = this;

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("test2","onCreateOptionsMenu() start");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                Log.d("test2","onOptionItemSelected start");
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        Log.d("test2","updateSubtitle start()");

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if(!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        Log.d("test2","updateUI start()");
        CrimeLab crimeLab = CrimeLab.get(getActivity());

        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter); // RecyclerView 와 어댑터 연결.

        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

        if (crimes.isEmpty()) {
            mCrimeRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
        updateSubtitle();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;
        private static final int VIEW_TYPE_AD = 0;
        private static final int VIEW_TYPE_LIST = 1;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }


        private class AdvertisementHolder extends RecyclerView.ViewHolder {
            private ImageView mImageView;

            public void bindCrime() {
                Glide.with(getActivity()).load("http://goo.gl/gEgYUd")
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .into(mImageView);
            }

            public AdvertisementHolder(View itemView) {
                super(itemView);
                mImageView = (ImageView)
                        itemView.findViewById(R.id.list_item_ad_image_view);
            }

        }

        private class CrimeHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {
            private Crime mCrime;
            private TextView mTitleTextView;
            private TextView mDateTextView;
            private CheckBox mSolvedCheckBox;

            public void bindCrime(Crime crime) {
                mCrime = crime;

                mTitleTextView.setText(mCrime.getTitle());
                String dateFormat1 = "yyyy년 MMM dd일 EEEE";
                String dateString1 = DateFormat.format(dateFormat1, mCrime.getDate()).toString();
                String dateFormat2 = "aa H시 mm분";
                String dateString2 = DateFormat.format(dateFormat2, mCrime.getDate()).toString();
                mDateTextView.setText(dateString1+"\b"+dateString2);
                mSolvedCheckBox.setChecked(mCrime.isSolved());
            }


            public CrimeHolder(View itemView) {
                super(itemView);
                Log.d("test", "CrimeHolder start");
                itemView.setOnClickListener(this); // 하나의 항목(리스트)을 클릭하였을때,
                //OnClickListener(인터페이스)의 구현메소드인 "onClick"를 실행시킨다.

                mTitleTextView = (TextView)
                        itemView.findViewById(R.id.list_item_crime_title_text_view);
                mDateTextView = (TextView)
                        itemView.findViewById(R.id.list_item_crime_date_text_view);
                mSolvedCheckBox = (CheckBox)
                        itemView.findViewById(R.id.list_item_crime_solved_check_box);
            }

            @Override
            public void onClick(View v) {
                Log.d("test", "onClick start");
                Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                startActivity(intent);
            }
        }

        @Override
        public int getItemViewType(int position) {
            Log.d("test11","getItemViewType():" + position);
            if (position % 5 == 0 && position != 0) {
                return VIEW_TYPE_AD;
            } else {
                return VIEW_TYPE_LIST;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //"RecyclerView"는 해당 메소드를 호출하여 리스트에서 항목 하나하나를 ViewHolder 에 보낸다.
            //"ViewHolder"에서는 이 항목 하나를 받아서 항목에있는 위젯(TextView,imageView,CheckBox..등)
            //들을 참조를 받아온다.
            Log.d("test11", "onCreateViewHolder start viewType:"+viewType);

            if (viewType == VIEW_TYPE_LIST) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
                return new CrimeHolder(view);
            } else if (viewType == VIEW_TYPE_AD) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.list_item_advertisement, parent, false);
                return new AdvertisementHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof  CrimeHolder) {
                Crime crime = mCrimes.get(position);
                CrimeHolder crimeholder = (CrimeHolder)holder;
                crimeholder.bindCrime(crime);
            } else if (holder instanceof AdvertisementHolder) {
                AdvertisementHolder advertisementHolder = (AdvertisementHolder)holder;
                advertisementHolder.bindCrime();
            }
        }

       /* @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            //"RecyclerView"가 아까 "onCreateViewHolder"에서 생성된 ViewHolder(리스트항목(위젯들)와
            //리스트 위치를 받아와서 그 모델 데이터를 찾은 후 해당 ViewHolder에 데이터들을 보내고
            //아까 참조받은 위젯들의 각각 항목들을 데이터와 맞게 결합시켜 인플레이트한다.
            Log.d("test11", "onBindViewHolder() position:" +  position);
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }*/

        @Override
        public int getItemCount() {
            Log.d("test","getItemCount()" + mCrimes.size());
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

       /* @Override
        public int getItemViewType(int position) {
            if(mCrimes.get(position) instanceof Crime)
                return 0;
            else if(mCrimes.get(position) instanceof Banner)
                return 1;
        }
        */
    }
}
