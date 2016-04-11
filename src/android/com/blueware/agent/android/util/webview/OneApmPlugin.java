package com.blueware.agent.android.util.webview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import com.blueware.agent.android.util.OneapmWebViewClientApi;

import org.apache.cordova.*;
import org.json.JSONException;

/**
 * Created by andy on 4/11/2016.
 */
public class OneApmPlugin extends CordovaPlugin {
    private final static String VERSION = "0.0.1";
    private final static String TAG = "OneApmPlugin";
    private WebView webView;
    private boolean largThan40;//Cordova version > 4.0.0 ?
    private OneapmWebViewClientApi oneapmWebViewClientApi;
    private Context context;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        //get webview object .for cordova version larger than 4.0.0
        //for only android native webivew engine .
        //for other like crosswalk ,need ...
        largThan40 = largThan40();
        if(largThan40) {
            this.webView = (WebView) webView.getView();
            oneapmWebViewClientApi = new OneapmWebViewClientApi(this.webView);
            this.context = this.webView.getContext().getApplicationContext();
        } else {
            Log.e(TAG," ERROR ,cordova version less than 4.0 OR something exception occured while obtain corvoda version .");
        }
        Log.i(TAG," Starting plugin OneAPM android plugin for cordova version :"+VERSION);
    }

    // public static final String CORDOVA_VERSION = "4.1.0-dev";
    private boolean largThan40() {
        try {
            String nowVersion = CordovaWebView.CORDOVA_VERSION;
            String fistLetter[] = nowVersion.split(".");
            if(fistLetter !=null && fistLetter.length>=2) {
                String mainVersion  = fistLetter[0];
                int version = Integer.parseInt(mainVersion);
                if(version >=4) {
                    return true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //onPageFinished ,onNavigationAttempt(shouldOverrideUrlLoading).
    public Object onMessage(String var1, Object var2) {
       if("onPageFinished".equals(var1) && this.largThan40) {
           //inject js file .
           oneapmWebViewClientApi.onPageFinished(this.context);
       }
        return null;
    }

    //onOverrideUrlLoading(shouldOverrideUrlLoading)
    public boolean onOverrideUrlLoading(String var1) {
        if(TextUtils.isEmpty(var1)) {
            return false;
        }
        if(this.largThan40 && var1.startsWith("oneapm")) {
            this.oneapmWebViewClientApi.shouldOverrideUrlLoading(var1);
        }
        return false;
    }

    @Override
    public boolean execute(String action, final CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        //do nothing .
        //for some reason,now implement in a simple way
        //used some time else .
        return false;
    }
}