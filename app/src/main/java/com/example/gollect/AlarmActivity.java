package com.example.gollect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gollect.utility.DeleteNetworkManager;
import com.example.gollect.utility.GetNetworkManager;
import com.example.gollect.utility.PostNetworkManager;
import com.wefika.flowlayout.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class AlarmActivity extends AppCompatActivity {

    private FlowLayout mHashtagContainer;
    private String TAG = "AlarmActivity";
    final FlowLayout.LayoutParams hashTagLayoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final EditText enterKeywordView = findViewById(R.id.enterAlarmKeyword);
        Button addKeywordBt = findViewById(R.id.addAlarmKeyword);
        addKeywordBt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                postAddKeyword(enterKeywordView.getText().toString());
                addKeyword(enterKeywordView.getText().toString(),hashTagLayoutParams);
                enterKeywordView.setText("");
            }
        });
        setKeyword();
    }

    private void setKeyword()
    {
        hashTagLayoutParams.setMargins(16,16,16,16);
        mHashtagContainer = (FlowLayout)findViewById(R.id.hashtagContainer);
        final ArrayList<String> keyworList = getKeyword();

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO
                for (final String keyword : keyworList)
                {
                    final TextView textView = (TextView)getLayoutInflater().inflate(R.layout.keyword_item, null);
                    textView.setText(keyword);
                    textView.setBackgroundColor(getRandomHashtagColor(AlarmActivity.this));
                    textView.setLayoutParams(hashTagLayoutParams);
                    textView.setOnTouchListener(new View.OnTouchListener()
                    {
                        @Override
                        public boolean onTouch(View v, MotionEvent event)
                        {
                            if(event.getAction() == MotionEvent.ACTION_DOWN)
                            {

                            }
                            return false;
                        }
                    });
                    textView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Log.d(TAG,"Keyword: " +textView.getText().toString());
                            deleteKeyword(textView.getText().toString());
                            mHashtagContainer.removeView(textView);
                        }
                    });
                    mHashtagContainer.addView(textView);
                }
            }
        }, 150);
    }

    public void postAddKeyword(String keyword){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("filterword",keyword);
            Log.d(TAG,jsonObject.toString());
            new PostNetworkManager("/filterwords/users/"+ 23,jsonObject) {
                @Override
                public void errorCallback(int status) {
                    super.errorCallback(status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAffinity();
                                    System.runFinalization();
                                    System.exit(0);
                                    dialog.dismiss();
                                }
                            };

                            new android.app.AlertDialog.Builder(AlarmActivity.this)
                                    .setTitle(getString(R.string.network_err_msg))
                                    .setPositiveButton(getString(R.string.ok), exitListener)
                                    .setCancelable(false)
                                    .show();
                        }
                    });
                }

                @Override
                public void responseCallback(JSONObject responseJson) {
                    try {
                        if (responseJson.getString("result").contains("success")) {
                            Log.d(TAG,"필터 워드 추가 성공");
                        }else{
                            Log.d(TAG,"필터 워드 추가 실패");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void deleteKeyword(String keyword){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("filterword", keyword);
            Log.d(TAG, "JSON file: "+jsonObject.toString());
            new DeleteNetworkManager("/filterwords/users/" + 23, jsonObject) {
                @Override
                public void errorCallback(int status) {
                    super.errorCallback(status);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAffinity();
                                    System.runFinalization();
                                    System.exit(0);
                                    dialog.dismiss();
                                }
                            };

                            new android.app.AlertDialog.Builder(AlarmActivity.this)
                                    .setTitle(Resources.getSystem().getString(R.string.network_err_msg))
                                    .setPositiveButton(Resources.getSystem().getString(R.string.ok), exitListener)
                                    .setCancelable(false)
                                    .show();
                        }
                    });
                }

                @Override
                public void responseCallback(JSONObject responseJson) {
                    try {
                        if (responseJson.getString("result").contains("success")) {
                            Log.d(TAG, "필터 워드 삭제 성공");
                        } else {
                            Log.d(TAG, "필터 워드 삭제 실패");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getKeyword() {
        BaseActivity baseActivity = new BaseActivity();
        int user_id = 23;
        JSONObject jsonObject = new JSONObject();
        final ArrayList<String> keywordList = new ArrayList<>();

        //플랫폼 아이디 추가해야함
        new GetNetworkManager( "/filterwords/users/" +23) {
            @Override
            public void errorCallback(int status) {
                super.errorCallback(status);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogInterface.OnClickListener exitListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.runFinalization();
                                System.exit(0);
                                dialog.dismiss();
                            }
                        };

                        new android.app.AlertDialog.Builder(AlarmActivity.this)
                                .setTitle(getString(R.string.network_err_msg))
                                .setPositiveButton(getString(R.string.ok), exitListener)
                                .setCancelable(false)
                                .show();
                    }
                });
            }

            @Override
            public void responseCallback(JSONObject responseJson) {
                try {
                    if (responseJson.getString("result").contains("success")) {
                        Log.d(TAG,responseJson.toString());
                        for (int i=0; i<responseJson.getJSONArray("filterwords").length(); i++) {
                            Log.d(TAG,responseJson.getJSONArray("filterwords").getString(i));
                            keywordList.add(responseJson.getJSONArray("filterwords").getString(i)) ;
                        }
                    }else{
                        Log.d(TAG,responseJson.getString("result"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
        return keywordList;
    }

    private int getRandomHashtagColor(Context context)
    {
        int[] hashtagColors = context.getResources().getIntArray(R.array.hashtag_colors);
        int randomColor = hashtagColors[new Random().nextInt(hashtagColors.length)];
        return randomColor;
    }

    private void addKeyword(final String keyword, FlowLayout.LayoutParams hashTagLayoutParams){
        final TextView textView = (TextView)getLayoutInflater().inflate(R.layout.keyword_item, null);
        textView.setText(keyword);
        textView.setBackgroundColor(getRandomHashtagColor(AlarmActivity.this));
        textView.setLayoutParams(hashTagLayoutParams);
        textView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {

                }
                return false;
            }
        });
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG,"Keyword: " +textView.getText().toString());
                deleteKeyword(textView.getText().toString());
                mHashtagContainer.removeView(textView);
            }
        });
        mHashtagContainer.addView(textView);
    }
}