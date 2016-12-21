package com.bignerdranch.android.criminallntent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by rhkdd on 2016-12-13.
 */

public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private static final int REQUEST_CRIME = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        Fragment ddd = this;

        updateUI();

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if(mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter); // RecyclerView 와 어댑터 연결.

        } else {
            mAdapter.notifyDataSetChanged();
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
            mDateTextView.setText(mCrime.getDate().toString());
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

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }


        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //"RecyclerView"는 해당 메소드를 호출하여 리스트에서 항목 하나하나를 ViewHolder 에 보낸다.
            //"ViewHolder"에서는 이 항목 하나를 받아서 항목에있는 위젯(TextView,imageView,CheckBox..등)
            //들을 참조를 받아온다.
            Log.d("test", "onCreateViewHolder start viewType:"+viewType);

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            //"RecyclerView"가 아까 "onCreateViewHolder"에서 생성된 ViewHolder(리스트항목(위젯들)와
            //리스트 위치를 받아와서 그 모델 데이터를 찾은 후 해당 ViewHolder에 데이터들을 보내고
            //아까 참조받은 위젯들의 각각 항목들을 데이터와 맞게 결합시켜 인플레이트한다.
            Log.d("test", "onBindViewHolder() position:" +  position);
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
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
