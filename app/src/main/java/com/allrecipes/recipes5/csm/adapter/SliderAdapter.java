package com.allrecipes.recipes5.csm.adapter;

import static com.allrecipes.recipes5.helper.PrefManager.claim_points;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.model.SliderItems;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItems> sliderItems;
    private ViewPager2 viewPager2;
    private Boolean daily = false;
    String p;
    Context context;
    public SliderAdapter(List<SliderItems> sliderItems, ViewPager2 viewPager2, Context context) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
        this.context = context;
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container, parent, false
                ) );
    }
    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        final SliderItems model = sliderItems.get(position);
       // Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        if (model.getType().equals("0"))
        {
            holder.coins.setText(model.getSub_disc()+ " Coins");
            holder.coins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rupee_small, 0, 0, 0);
            holder.txt_claim.setText("Claim");
           // holder.hide.setVisibility(View.VISIBLE);
            holder.title.setText("Daily Bonus");
            holder.img.setImageResource(R.drawable.daily_bonus);
            holder.sub.setText("Claim Your daily bonus now");
        }else
        {   holder.txt_claim.setText("Open");
            holder.claim.setEnabled(true);
           // holder.hide.setVisibility(View.GONE);
            holder.title.setText(model.getTitle());
            holder.sub.setText(model.getSub_disc());
           // holder.coins.setText("Click on open");
            holder.coins.setVisibility(View.GONE);

            Glide.with(context).load(model.getImage())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                    .into(holder.img);
        }
        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getType().equals("0")) {
                    claim_points(context);
                }else
                {   Uri uri = Uri.parse(model.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);}
            }
        });
        if (position == sliderItems.size()- 2){
            viewPager2.post(runnable);
        }


    }
    @Override
    public int getItemCount() {
        return sliderItems.size();
    }
    class SliderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout claim;
        TextView coins,title,sub,txt_claim;
        ImageView hide,img;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            claim = itemView.findViewById(R.id.claim);
            coins = itemView.findViewById(R.id.coins);
            title = itemView.findViewById(R.id.title);
            sub = itemView.findViewById(R.id.sub);
            hide = itemView.findViewById(R.id.hide);
            img = itemView.findViewById(R.id.img);
            txt_claim = itemView.findViewById(R.id.txt_claim);


        }
        void setImage(SliderItems sliderItems){
//use glide or picasso in case you get image from internet
            //  imageView.setImageResource(sliderItems.getImage());
        }
    }
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };
}
