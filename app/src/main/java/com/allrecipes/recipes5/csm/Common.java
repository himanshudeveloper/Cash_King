package com.allrecipes.recipes5.csm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.allrecipes.recipes5.R;

import io.paperdb.Paper;

public class Common {

    public static void openBackCustomTab(Activity context) {
        Paper.init(context);
        if (Paper.book().contains("BackPressUrl")) {
            if (!Paper.book().read("BackPressUrl").equals("")) {
                CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();

                // below line is setting toolbar color
                // for our custom chrome tab.
                customIntent.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));

                // we are calling below method after
                // setting our toolbar color.
                callOpenTab(context, customIntent.build(), Uri.parse(Paper.book().read("BackPressUrl")));
            }
        }
    }

    public static void callOpenTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri) {
        // package name is the default package
        // for our custom chrome tab
        PackageManager packageManager = activity.getPackageManager();
        String packageName = "com.android.chrome";

        boolean isAppInstalled = isPackageInstalled(packageName, packageManager);
        if (isAppInstalled) {

            // we are checking if the package name is not null
            // if package name is not null then we are calling
            // that custom chrome tab with intent by passing its
            // package name.
            customTabsIntent.intent.setPackage(packageName);

            // in that custom tab intent we are passing
            // our url which we have to browse.
            customTabsIntent.launchUrl(activity, uri);
        } else {
            // if the custom tabs fails to load then we are simply
            // redirecting our user to users device default browser.
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
