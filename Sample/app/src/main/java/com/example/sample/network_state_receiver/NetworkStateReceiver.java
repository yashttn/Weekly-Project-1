package com.example.sample.network_state_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.sample.data_manager.DataManager;

public class NetworkStateReceiver extends BroadcastReceiver {
    ConnectivityManager connectivityManager;
    IOnNetworkStateChanged onNetworkStateChanged;
    NetworkInfo networkInfo;

    public void setOnNetworkStateChanged(IOnNetworkStateChanged onNetworkStateChanged) {
        this.onNetworkStateChanged = onNetworkStateChanged;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        checkNetworkConnectivity();
    }

    public void checkNetworkConnectivity() {
        if (networkInfo != null && networkInfo.isConnected()) {
            if (onNetworkStateChanged != null) {
                onNetworkStateChanged.isNetworkConnected(true);
            }
        } else {
            if (onNetworkStateChanged != null) {
                onNetworkStateChanged.isNetworkConnected(false);
            }
        }
    }
}
