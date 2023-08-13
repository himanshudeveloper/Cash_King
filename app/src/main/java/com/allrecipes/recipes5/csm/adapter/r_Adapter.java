package com.allrecipes.recipes5.csm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.model.r_Model;

import java.util.List;

public class r_Adapter extends RecyclerView.Adapter<r_Adapter.ViewHolder> {
    public List<r_Model> historyList;
    public Context context;

    public r_Adapter(List<r_Model> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_design, parent, false);
        return new ViewHolder(view);

    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final r_Model model = historyList.get(position);

       Glide.with(context).load(model.getImage())
                .apply(new RequestOptions().placeholder(R.drawable.refer_banner))
                .into(holder.img);

       holder.title.setText(model.getPackage_name()+" "+model.getSymbol()+model.getAmount());
       holder.date.setText(model.getDate());


        if (model.getStatus().equals("1"))
        {
            holder.status.setBackgroundResource(R.drawable.r_approved);
            holder.status_txt.setText("Approved");

        }
        else if (model.getStatus().equals("3"))
        {
            holder.status.setBackgroundResource(R.drawable.r_approved);
            holder.status_txt.setText("Received");

        }
        else if (model.getStatus().equals("0"))
        {
            holder.status.setBackgroundResource(R.drawable.r_pending);
            holder.status_txt.setText("Pending");
        }

        else
        {
            holder.status.setBackgroundResource(R.drawable.claim_back);
            holder.status_txt.setText("Rejected");
        }

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,date,status_txt;
        ImageView img;
        LinearLayout click,status;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            click = itemView.findViewById(R.id.click);
            status = itemView.findViewById(R.id.status);
            status_txt = itemView.findViewById(R.id.status_txt);

        }

    }
    public int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }
}
