package com.allrecipes.recipes5.csm.topsheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.allrecipes.recipes5.R;

public class OfferDialog extends DialogFragment {
        View root_view;
        ImageView close;
        TextView name,disc,amountt,diss;
        RoundedImageView image;

        LinearLayout btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_offer_dialog, container, false);
        // Inflate the layout for this fragment
        close = root_view.findViewById(R.id.close);
        name = root_view.findViewById(R.id.name);
        disc = root_view.findViewById(R.id.disc);
        amountt = root_view.findViewById(R.id.amount);
        image = root_view.findViewById(R.id.image);
        diss = root_view.findViewById(R.id.diss);
        btn = root_view.findViewById(R.id.btn);
        LinearLayout blur = root_view.findViewById(R.id.blur);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }});

        String offer_name = getArguments().getString("Offer_name");
        String offer_disc = getArguments().getString("offer_disc");
        String disclaimer = getArguments().getString("disclaimer");
        String amount = getArguments().getString("amount");
        String Image = getArguments().getString("Image");
        String url = getArguments().getString("url");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        Glide.with(getContext()).load(Image)
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher_round))
                .into(image);

        txt(name,offer_name);
        txt(disc,offer_disc);
        txt(amountt,amount);
        txt(diss,disclaimer);


        return root_view;
    }
    private void txt(TextView txt,String text) { txt.setText(text); }

}