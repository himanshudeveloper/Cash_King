package com.allrecipes.recipes5.luck_draw.Dialog;


import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.allrecipes.recipes5.R;


public class Dialog_Activity extends DialogFragment {


    private View root_view;

    Button ok;

    TextView massage, title,date;
    RelativeLayout close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate( R.layout.dialog_activity, container, false);
        Bundle bundle = this.getArguments();

        final String get_title = bundle.getString("get_title");
        final String get_date = bundle.getString("get_date");
        final String get_massage = bundle.getString("get_message");
        massage = root_view.findViewById(R.id.message);
        title = root_view.findViewById(R.id.title);
        close = root_view.findViewById(R.id.close);
        date = root_view.findViewById(R.id.date);
        massage.setText(get_massage);
        date.setText(get_date);
        title.setText(get_title);
        ok = root_view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return root_view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}