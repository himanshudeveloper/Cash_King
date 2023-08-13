package com.allrecipes.recipes5.luck_draw.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.R;

import java.util.List;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {
    public List<ActivityItem> historyList;


    public ActivityListAdapter(List<ActivityItem> historyList) {

        this.historyList = historyList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ViewHolder(view);

    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ActivityItem model = historyList.get(position);
        holder.setTitle(model.getTitle());
        holder.setMessage(model.getMessage());
        holder.setDate(model.getDate());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, title, message;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            title = (TextView) itemView.findViewById( R.id.title);
            message = (TextView) itemView.findViewById(R.id.message);
        }

        public void setDate(String title) {
            date.setText(title);
        }

        public void setTitle(String title1) {
            title.setText(title1);
        }

        public void setMessage(String message1) {
            message.setText(message1);
        }

    }
}

