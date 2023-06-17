package com.bearya.sdk.socket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

public class NetTools {

    public static final int SOCKET_PORT = 7596;

    public static String getNetIpAddress(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (systemService != null) {
            NetworkInfo activeNetworkInfo = systemService.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return Formatter.formatIpAddress(
                            ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                                    .getConnectionInfo()
                                    .getIpAddress());
                }
            }
        }
        return null;
    }

}