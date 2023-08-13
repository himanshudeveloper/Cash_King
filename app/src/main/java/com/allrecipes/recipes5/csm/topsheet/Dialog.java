package com.allrecipes.recipes5.csm.topsheet;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.Common;
import com.allrecipes.recipes5.csm.PlayActivity;

public class Dialog extends DialogFragment{
    View root_view;
    Button yes,no;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate( R.layout.fragment_dialog, container, false);


        yes = root_view.findViewById(R.id.yes);
        no = root_view.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.openBackCustomTab(getActivity());
                getActivity().finish();
                System.exit(0);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return root_view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {

        super.onCancel(dialog);
        dismiss();
    }
}