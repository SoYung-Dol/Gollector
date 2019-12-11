package com.example.gollect.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gollect.BaseActivity;
import com.example.gollect.PlatformData;
import com.example.gollect.R;

import com.example.gollect.SubscribeActivity;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.gollect.PlatformData.HEADER_TYPE;
import static com.example.gollect.PlatformData.CHILD_TYPE;

public class SubscribeRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<PlatformData> mList;
    private ArrayList<PlatformData> mSubsList = null;
    Activity activity;
    private FlowLayout mHashtagContainer;
    static SubscribeActivity subscribeActivity = new SubscribeActivity();

    public SubscribeRecyclerviewAdapter(Activity act, List<PlatformData> list, ArrayList<PlatformData> subsList) {
        this.activity = act;
        this.mList = list;
        this.mSubsList = subsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_haeder, parent, false);
                return new SubscribeRecyclerviewAdapter.CityViewHolder(view);
            case CHILD_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_platform, parent, false);
                return new SubscribeRecyclerviewAdapter.EventViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlatformData object = mList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case HEADER_TYPE:
                    ((SubscribeRecyclerviewAdapter.CityViewHolder) holder).mTitle.setText(object.getPlatformName());
                    break;
                case CHILD_TYPE:
                    Log.d(TAG,"@@@@@@@@"+object.getPlatformName());
                    ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).platformName.setText(object.getPlatformName());
                    Glide.with(activity).load(mList.get(position).getImageUrl()).into(((SubscribeRecyclerviewAdapter.EventViewHolder) holder).platformLogo);
                    ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).subscriveBt.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mSubsList.size(); i++) {
                        if (mList.get(position).getPlatformId() == mSubsList.get(i).getPlatformId()) {
                            Log.d(Constraints.TAG, "position: " + position + " size: " + mSubsList.size() + " Name: " + mSubsList.get(i).getPlatformName());
                            ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).subscriveBt.setText("구독 취소");
                            ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).keywordBt.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).subscriveBt.setText("구독");
                            ((SubscribeRecyclerviewAdapter.EventViewHolder) holder).keywordBt.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            PlatformData object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public CityViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.header_title);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView platformLogo;
        TextView platformName ;
        Button keywordBt;
        Button subscriveBt;

        public EventViewHolder(View itemView) {
            super(itemView);
            BaseActivity baseActivity = new BaseActivity();
            final int userID = baseActivity.getUserData().getUserID();

            platformLogo = itemView.findViewById(R.id.platform_logo);
            platformName = itemView.findViewById(R.id.platform_name);
            subscriveBt = itemView.findViewById(R.id.subscribeBt);
            keywordBt = itemView.findViewById(R.id.keywordBt);

            subscriveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG,"This position: " + getAdapterPosition());
                    if(subscriveBt.getText().toString().contains("취소")){
                        subscribeActivity.deletePlatform(userID,mList.get(getAdapterPosition()).getPlatformId());
                        subscriveBt.setText("구독");
                        keywordBt.setVisibility(View.INVISIBLE);
                        PlatformData platform = new PlatformData(mList.get(getAdapterPosition()).getPlatformId(), false);
                        for(int i =0;i<mSubsList.size();i++){
                            if(mSubsList.get(i).getPlatformId()==mList.get(getAdapterPosition()).getPlatformId())
                                mSubsList.remove(i);
                        }
                    }else if(subscriveBt.getText().toString().contains("구독")) {
                        PlatformData platform = new PlatformData(mList.get(getAdapterPosition()).getPlatformId(), true);
                        mSubsList.add(platform);
                        subscribeActivity.postAddKeyword(userID, mList.get(getAdapterPosition()).getPlatformId(), "");
                        subscriveBt.setText("구독 취소");
                        keywordBt.setVisibility(View.VISIBLE);
                    }
                }
            });

            keywordBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int platform_id = mList.get(getAdapterPosition()).getPlatformId();
                    show(platform_id);
                }
            });
        }
    }

    public void show(final int platform_number)
    {
        BaseActivity baseActivity = new BaseActivity();
        final int userID = baseActivity.getUserData().getUserID();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog,null);

        builder.setView(dialogView);

        final FlowLayout.LayoutParams hashTagLayoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        hashTagLayoutParams.setMargins(16,16,16,16);
        mHashtagContainer = dialogView.findViewById(R.id.hashtagContainer);
        final ArrayList<String> keywordList = subscribeActivity.getKeyword(platform_number);
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO
                for (final String keyword : keywordList) {
                    if (keyword.contentEquals("") != true) {
                        final TextView textView = (TextView) activity.getLayoutInflater().inflate(R.layout.keyword_item, null);
                        textView.setText(keyword);
                        textView.setBackgroundColor(getRandomHashtagColor(activity));
                        textView.setLayoutParams(hashTagLayoutParams);
                        textView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    //터치 이벤트
                                }
                                return false;
                            }
                        });
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Do your action
                                subscribeActivity.deleteKeyword(userID, platform_number, textView.getText().toString());
                                mHashtagContainer.removeView(textView);
                            }
                        });

                        mHashtagContainer.addView(textView);
                    }
                }
            }
        }, 150);



        Button one = (Button) dialogView.findViewById(R.id.button1);
        Button three = (Button) dialogView.findViewById(R.id.button3);
        final EditText enterKeyword = (EditText)dialogView.findViewById(R.id.enterKeyword);

        final AlertDialog dialog = builder.create();


        one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        three.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String Keyword = enterKeyword.getText().toString();
                enterKeyword.setText("");
                Log.d(Constraints.TAG,Keyword);
                subscribeActivity.postAddKeyword(userID, platform_number, Keyword);
                //keywordList.add(Keyword);
                addKeyword(Keyword, hashTagLayoutParams, platform_number);
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        // Display the custom alert dialog on interface
        dialog.show();
    }

    private int getRandomHashtagColor(Context context)
    {
        int[] hashtagColors = context.getResources().getIntArray(R.array.hashtag_colors);
        int randomColor = hashtagColors[new Random().nextInt(hashtagColors.length)];
        return randomColor;
    }

    private void addKeyword(final String hashtag, FlowLayout.LayoutParams hashTagLayoutParams, final int platform_id){
        BaseActivity baseActivity = new BaseActivity();
        final int userID = baseActivity.getUserData().getUserID();
        final TextView textView = (TextView)activity.getLayoutInflater().inflate(R.layout.keyword_item, null);
        textView.setText(hashtag);
        textView.setBackgroundColor(getRandomHashtagColor(activity));
        textView.setLayoutParams(hashTagLayoutParams);
        textView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    //터치 이벤트
                }
                return false;
            }
        });
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Do your action
                subscribeActivity.deleteKeyword(userID, platform_id, textView.getText().toString());
                mHashtagContainer.removeView(textView);
            }
        });

        mHashtagContainer.addView(textView);
    }
}