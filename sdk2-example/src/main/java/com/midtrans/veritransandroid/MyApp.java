package com.midtrans.veritransandroid;

import android.app.Application;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.ui.CustomSetting;
import com.midtrans.sdk.ui.MidtransUi;

/**
 * Created by rakawm on 10/19/16.
 */

public class MyApp extends Application {
    public static final String CHECKOUT_URL = "https://rakawm-snap.herokuapp.com/installment/charge";
    private static final String CLIENT_KEY = "VT-client-E4f1bsi1LpL1p5cF";

    @Override
    public void onCreate() {
        super.onCreate();
        initMidtransSDK();
    }

    private void initMidtransSDK() {

        CustomSetting customSetting = new CustomSetting();
        MidtransUi.builder()
                .setClientKey(CLIENT_KEY)
                .setEnvironment(Environment.SANDBOX)
                .setCustomSetting(new CustomSetting())
                .enableLog(true)
                .build();
    }
}