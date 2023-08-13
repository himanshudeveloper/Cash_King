package com.allrecipes.recipes5.watchandearn.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.databinding.FragmentContactBinding;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.google.android.material.textfield.TextInputEditText;
import com.ironsource.mediationsdk.IronSource;

public class ContactFragment extends Fragment {

    ShowAds showAds = new ShowAds();
    FragmentContactBinding binding;
    private TextInputEditText subject, message;
    private AppCompatButton btn;
    // TODO: Rename and change types of parameters
    private ImageView back;

    public ContactFragment() {

    }


    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        subject = view.findViewById(R.id.subject);
        message = view.findViewById(R.id.message);
        btn = view.findViewById(R.id.send_btn);
        back = view.findViewById(R.id.back_img_contact);
        intView();
        return view;
    }

    private void intView() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subject.getText().toString().length() == 0) {
                    subject.setError("Enter Subject");
                    subject.requestFocus();
                } else if (message.getText().toString().length() == 0) {
                    message.setError("Please Type Message");
                    message.requestFocus();
                } else {
                    if (getActivity() == null) {
                        return;
                    }
                    String[] Email = {getResources().getString(R.string.gmail_id)};
                    String Subject = subject.getText().toString();
                    String Message = message.getText().toString();
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"));
                    intent.putExtra(Intent.EXTRA_EMAIL, Email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, Subject);
                    intent.putExtra(Intent.EXTRA_TEXT, Message);
                    getActivity().startActivity(Intent.createChooser(intent, "Send Via"));
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        showAds.showBottomBanner(requireActivity(), binding.adViewBottom);
        showAds.showTopBanner(requireActivity(), binding.adViewTop);
        IronSource.onResume(requireActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        showAds.destroyBanner();
        IronSource.onPause(requireActivity());

    }


}