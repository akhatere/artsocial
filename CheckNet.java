package com.example.paragon.socialapp.Webservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by khatereh on 8/4/2016.
 */
public class CheckNet {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
//        Toast.makeText(CheckNet,"خطا در اتصال به شبکه",Toast.LENGTH_SHORT);
        return false;
    }
}
