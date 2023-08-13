package com.allrecipes.recipes5.csm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.model.Leaderboard_model;

import java.util.List;
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    public List<Leaderboard_model> historyList;
    public Context context;

    public LeaderboardAdapter(List<Leaderboard_model> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);

    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Leaderboard_model model = historyList.get(position);

        Glide.with(context).load(model.getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(holder.img);

        holder.tag.setText(model.getTag());
        holder.name.setText(model.getName());
        holder.points.setText(model.getPoints());



    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag,name,points;
        ImageView img;
        //CardView click;

        public ViewHolder(View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.tag);
            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.pro);
            points = itemView.findViewById(R.id.points);


        }

    }

}