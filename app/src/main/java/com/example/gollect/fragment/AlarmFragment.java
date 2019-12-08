package com.example.gollect.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gollect.AlarmData;
import com.example.gollect.R;
import com.example.gollect.adapter.AlarmRecyclerviewAdapter;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AlarmFragment extends Fragment {

    //앱에 설치된 Realm파일을 찾아서 가져오는 코드
    Realm realm = Realm.getDefaultInstance();

    public AlarmFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);

        //전체 Diary 목록을 Realm에 요청해서 받아오는 코드입니다

        RealmResults<AlarmData> alarmRealmResults = getAlarmList();
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> nameList = new ArrayList<>();
        ArrayList<String> contentsList = new ArrayList<>();
        ArrayList<Integer> residList = new ArrayList<>();
        ArrayList<String> respackagerList = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        for (int i=0; i<alarmRealmResults.size()-1; i++) {
            nameList.add(String.format("%s", alarmRealmResults.get(i).getSender()));
            contentsList.add(String.format("%s", alarmRealmResults.get(i).getContents()));

            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            String date = transFormat.format(alarmRealmResults.get(i).getWriteAt());
            dateList.add(String.format("%s",date));
            residList.add(alarmRealmResults.get(i).getResId());
            respackagerList.add(String.format("%s",alarmRealmResults.get(i).getAppName()));
        }
        Log.d("@@@@@@",alarmRealmResults.toString());

        RecyclerView recyclerView = v.findViewById(R.id.rv_alarm);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager) ;
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        AlarmRecyclerviewAdapter adapter = new AlarmRecyclerviewAdapter(nameList, contentsList, residList, respackagerList, dateList) ;
        recyclerView.setAdapter(adapter) ;
        return v;
    }

    private RealmResults<AlarmData> getAlarmList(){
        //Realm에 저장된 Diary들을 모두 찾아달라고 Realm에 요청해서 받아오는 코드입니다
        RealmResults<AlarmData> alarmRealmResults = realm.where(AlarmData.class).findAll();
        return alarmRealmResults;
    }


}
