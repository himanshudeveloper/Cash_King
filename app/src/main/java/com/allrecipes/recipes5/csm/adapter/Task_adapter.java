package com.allrecipes.recipes5.csm.adapter;

import static com.allrecipes.recipes5.Just_base.task_claim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.R;

import com.allrecipes.recipes5.csm.model.Task_model;

import java.util.List;



public class Task_adapter extends RecyclerView.Adapter<Task_adapter.ViewHolder> {
    public List<Task_model> historyList;
    public Context context;
    public String check;


    public Task_adapter(List<Task_model> historyList, Context context,String check) {
        this.historyList = historyList;
        this.context = context;
        this.check = check;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_task, parent, false);
        return new ViewHolder(view);

    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task_model model = historyList.get(position);
        holder.name.setText("Invite "+model.getInvites()+" friends");
        if (model.getRef()>=model.getInvites())
        {
            holder.prize.setText(model.getInvites()+"/"+model.getInvites()+" Invited");
        }else
        {
            holder.prize.setText(model.getRef()+"/"+model.getInvites()+" Invited");
        }

        holder.claim.setEnabled(false);
        holder.coins.setText(model.getPoints()+" coins");
        holder.count.setText(""+model.getTitle());
        holder.progressBar.setMax(model.getInvites());
        holder.progressBar.setProgress(model.getRef());

        if (model.getRef()>=model.getInvites())
        {
                holder.claim.setBackground(ContextCompat.getDrawable(context, R.drawable.claim_back));
            if (model.getCheck().equals("true")) {
               holder.claim.setEnabled(false);
               holder.claim.setText("Done");

            }else
            {
                holder.claim.setBackground(ContextCompat.getDrawable(context, R.drawable.claim_back));
                holder.claim.setEnabled(true);
                holder.claim.setText("Claim");
            }
        }

        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getCheck().equals("false"))
                {
                    task_claim(context,model.getId(), holder.claim);
                    //Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        if (check.equals("0"))
        {
            if (historyList.size()>3)
            {
                return 3;
            }else
            {
                return historyList.size();
            }

        }else
        {
            return historyList.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {




        TextView name,prize,coins,count;
        Button claim;
        ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.t_id);
            prize = itemView.findViewById(R.id.sub);
            claim = itemView.findViewById(R.id.claim);
            coins = itemView.findViewById(R.id.coins);
            count = itemView.findViewById(R.id.count);
            progressBar = itemView.findViewById(R.id.progressBar);



        }




    }
}
