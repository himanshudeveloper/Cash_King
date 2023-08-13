package com.allrecipes.recipes5.csm.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.topsheet.Dialog_Reward;

import java.util.List;



public class Redeem_adapter extends RecyclerView.Adapter<Redeem_adapter.ViewHolder> {
    public List<Redeem_model> historyList;
    public Context context;


    public Redeem_adapter(List<Redeem_model> historyList, Context context) {
        this.historyList = historyList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_redeem_package, parent, false);
        return new ViewHolder(view);

    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Redeem_model model = historyList.get(position);
        Glide.with(context).load(model.getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(holder.img);
        holder.amount.setText(model.getSymbol()+model.amount);
        holder.coins.setText(model.getCoins());

        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                Dialog_Reward newFragment = new Dialog_Reward();
                Bundle args = new Bundle();
                args.putString("coins", model.getCoins());
                args.putString("id", model.getId());
                args.putString("uri", model.getImage());
                args.putString("symbol", model.getSymbol());
                args.putString("amount", model.getAmount());
                args.putString("type", model.getType());
                args.putString("hint", model.getHint());
                args.putString("more", model.getDetails());
                args.putString("amount_id", model.getAmount_id());

                newFragment.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            }
        });


    }



    @Override
    public int getItemCount() {

            return historyList.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView amount,coins;
        RelativeLayout click;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            amount = itemView.findViewById(R.id.amount);
            coins = itemView.findViewById(R.id.coins);
            click = itemView.findViewById(R.id.click);




        }




    }
}

