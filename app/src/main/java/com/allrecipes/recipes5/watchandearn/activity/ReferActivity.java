package com.allrecipes.recipes5.watchandearn.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.allrecipes.recipes5.R;
import com.allrecipes.recipes5.csm.Common;
import com.allrecipes.recipes5.watchandearn.feature.ShowAds;
import com.allrecipes.recipes5.watchandearn.fragments.ContactFragment;
import com.allrecipes.recipes5.watchandearn.fragments.GoldFragment;
import com.allrecipes.recipes5.watchandearn.fragments.PlatinumFragment;
import com.allrecipes.recipes5.watchandearn.fragments.ReferFragment;
import com.allrecipes.recipes5.watchandearn.fragments.SilverFragment;
import com.allrecipes.recipes5.watchandearn.fragments.WalletFragment;
import com.allrecipes.recipes5.watchandearn.utils.Constant;

public class ReferActivity extends AppCompatActivity {

    ShowAds showAds = new ShowAds();
    private String type;
    private ReferActivity activity;
    private Fragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_watchandearn);
        activity = this;
        type = getIntent().getStringExtra("type");

        if (MainActivity.checkInterstialIsCalled) {
            showAds.showInterstitialAds(this);
            MainActivity.checkInterstialIsCalled = false;
        }
        if (type != null) {
            switch (type) {
                case "wallet":
                    fm = WalletFragment.newInstance();
                    break;
                case "contact":
                    fm = ContactFragment.newInstance();
                    break;
                case "refer":
                    fm = ReferFragment.newInstance();
                    break;
                case "Silver Scratch":
                    fm = SilverFragment.newInstance();
                    break;
                case "Platinum Scratch":
                    fm = PlatinumFragment.newInstance();
                    break;
                case "Gold Scratch":
                    fm = GoldFragment.newInstance();
                    break;
            }
            if (fm != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout_refer, fm).commit();
            }
        } else {
            Constant.showToastMessage(activity, "Something Went Wrong...");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.openBackCustomTab(ReferActivity.this);
        finish();
    }


}