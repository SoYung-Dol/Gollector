package com.example.gollect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gollect.R;
import com.example.gollect.item.TextContentsItem;
import com.example.gollect.item.VideoContentsItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TcViewAdapter extends  RecyclerView.Adapter<TcViewAdapter.TcViewHolder> {

    private List<TextContentsItem> items;
    private Context context;
    private Date date;
    private SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String getTime;
    public TcViewAdapter(List<TextContentsItem> listitems, Context context){
        this.items = listitems;
        this.context = context;
    }
    public class TcViewHolder extends RecyclerView.ViewHolder {

        private TextView image;
        private TextView title;
        private TextView summary;
        private TextView upload_time;

        public TcViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.platform);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.summary);
            upload_time = itemView.findViewById(R.id.uploaded_time);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = getAdapterPosition();
                    TextContentsItem textContentsitem = items.get(position);
                    String url = textContentsitem.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
    @NonNull
    @Override
    public TcViewAdapter.TcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_contents_view,parent,false);

        TcViewAdapter.TcViewHolder tcViewHolder = new TcViewAdapter.TcViewHolder(view);

        return tcViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TcViewAdapter.TcViewHolder holder, int position) {
        TextContentsItem item = items.get(position);

        long now = System.currentTimeMillis();
        date = new Date(now);
        getTime = simpleDateFormat.format(date);

        String date = item.getUploaded_at().substring(5,10);
        String time = item.getUploaded_at().substring(11,16);
        String currentDate = getTime.substring(5,10);

        holder.image.setText(item.getPlatformId());
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());
        if(date == currentDate) holder.upload_time.setText(time);
        else holder.upload_time.setText(date);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
/*
    public void addItem(String platformId, String title, String summary) {
        TextContentsItem item = new TextContentsItem();

        item.setPlatformId(platformId);
        item.setTitle(title);
        item.setSummary(summary);

        items.add(item);
    }

 */
}