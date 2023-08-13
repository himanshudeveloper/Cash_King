package com.allrecipes.recipes5.watchandearn.feature;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;

import com.allrecipes.recipes5.BuildConfig;
import com.allrecipes.recipes5.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonMethods {

    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "Hey there," + "I am recommending you this Scratch and Win App Amazing App\n" + "Install Now!" + "\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public static void contactUs(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setPackage("com.google.android.gm");
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"help.filesharing@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        i.putExtra(Intent.EXTRA_TEXT, "I need some help regarding ");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public static void whatsApp(Context context) throws UnsupportedEncodingException, PackageManager.NameNotFoundException {
        String contact = "+91 8630334698"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + URLEncoder.encode("Hello, I need some help regarding ", "UTF-8");
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse(url));
            context.startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            try {
                PackageManager pm = context.getPackageManager();
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            } catch (PackageManager.NameNotFoundException exception) {
                e.printStackTrace();
                Toast.makeText(context, "WhatsApp is not installed on this Device.", Toast.LENGTH_SHORT).show();

            }

//            whatsApp(context, "com.whatsapp.w4b");
        }


    }

    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, String uri) {
        // package name is the default package
        // for our custom chrome tab
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(uri);//replace with string to compare
        if (m.find()) {
            String packageName = "com.android.chrome";
            if (packageName != null) {

                // we are checking if the package name is not null
                // if package name is not null then we are calling
                // that custom chrome tab with intent by passing its
                // package name.
                customTabsIntent.intent.setPackage(packageName);

                // in that custom tab intent we are passing
                // our url which we have to browse.
                customTabsIntent.launchUrl(activity, Uri.parse(uri));
            } else {
                // if the custom tabs fails to load then we are simply
                // redirecting our user to users device default browser.
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
            }
        }


    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return (wifiInfo != null && wifiInfo.isConnected()) || (mobInfo != null && mobInfo.isConnected());
    }
}
