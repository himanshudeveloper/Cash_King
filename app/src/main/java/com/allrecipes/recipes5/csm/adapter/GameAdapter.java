package com.allrecipes.recipes5.csm.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.PlayActivity;
import com.allrecipes.recipes5.csm.model.GameModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<GameModel> historyList;
    public Context context;
    int check;

    public GameAdapter(List<GameModel> historyList, Context context, int check) {
        this.historyList = historyList;
        this.context = context;
        this.check = check;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (check == 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_modern, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_games_varti, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final GameModel model = historyList.get(position);

        final ViewHolder holder = (ViewHolder) viewHolder;
        if (check == 0) {
            int i = position + 1;
            holder.count.setText("#" + i);
        }
        Glide.with(context).load(model.getImage())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(holder.img_2);
        setViewHolderItem(holder, model);
    }

    private void setViewHolderItem(ViewHolder holder, GameModel model) {
        holder.title.setText(model.getTitle());
        holder.click.setOnClickListener(view -> {
            Intent i = new Intent(context, PlayActivity.class);
            i.putExtra("url", model.getGame_link());
            i.putExtra("image", model.getImage());
            i.putExtra("name", model.getTitle());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if (check == 0) {
            if (historyList.size() <= 5) {
                return historyList.size();
            } else {
                return 5;
            }
        } else {
            return historyList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, count;
        ImageView img;
        LinearLayout click;
        RoundedImageView img_2;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            click = itemView.findViewById(R.id.click);
            img_2 = itemView.findViewById(R.id.img_2);
            count = itemView.findViewById(R.id.count);
        }
    }

    public static class NativeAdHolder extends RecyclerView.ViewHolder {
        FrameLayout native_container;

        public NativeAdHolder(View itemView) {
            super(itemView);
            native_container = itemView.findViewById(R.id.native_container);
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

    public static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(getPressedColorSelector(normalColor, pressedColor), getColorDrawableFromColor(normalColor), null);
    }

    public static ColorStateList getPressedColorSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]
                        {
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_activated},
                                new int[]{}
                        },
                new int[]
                        {
                                pressedColor,
                                pressedColor,
                                pressedColor,
                                normalColor
                        }
        );
    }

    public static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}


