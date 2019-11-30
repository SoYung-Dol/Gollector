package com.example.gollect.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    public class TcViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener, View.OnClickListener, MenuItem.OnMenuItemClickListener{

        private TextView platform;
        private TextView title;
        private TextView summary;
        private TextView upload_time;
        private ImageView imageView;

        public TcViewHolder(@NonNull View itemView) {
            super(itemView);
            platform = itemView.findViewById(R.id.platform);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.summary);
            upload_time = itemView.findViewById(R.id.uploaded_time);
            imageView = itemView.findViewById(R.id.imageSrc);

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

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();

            switch (id){
                case R.id.bookmark_menu:
                    String titleStr = items.get(getAdapterPosition()).getTitle();
                    Log.d("jaejin",titleStr+"짱짱");
                    break;
            }
            return true;
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem bookmark = menu.add(Menu.NONE,R.id.bookmark_menu,1,"즐겨찾기");
            bookmark.setOnMenuItemClickListener(this);
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

        holder.platform.setText(item.getPlatformId());
        holder.title.setText(item.getTitle());
        holder.summary.setText(item.getSummary());
        if(date == currentDate) holder.upload_time.setText(time);
        else holder.upload_time.setText(date);

        Glide.with(holder.itemView.getContext())
                .load(item.getImg_src())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
