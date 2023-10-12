package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.facetec.sdk.FaceTecVocalGuidanceCustomization;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecFaceScanResultCallback;
import com.facetec.sdk.FaceTecIDScanResultCallback;

public class FacetecActivity extends Activity {

    private String SessionToken;
  	private boolean LineActivation; /** Cambio realizado */
    private String ScanResultBlob;
    private String TAG = "FacetecActivity";
    private FaceTecSDK.InitializeCallback callback;
    private Processor lastProcessor = null;
    public static String scan1 = null;
    public static String scan2 = null;
    public static FaceTecFaceScanResultCallback faceScanResultCallback = null;
    public static FaceTecIDScanResultCallback idScanResultCallback = null;

    private static final int REQUEST_APP_PREF = 168;
    private static final String[] requiredPermissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create");
        permissions();
    }

    private void permissions() {
        if (Build.VERSION.SDK_INT > 22) {
            Log.d(TAG, "Requesting Permissions");
            checkPermissions(requiredPermissions);
        } else {
            initializeFacetecSDK();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissions(@NonNull String... permissions) {

        List<String> ungranted = new ArrayList<String>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    permission) != PackageManager.PERMISSION_GRANTED) {
                ungranted.add(permission);
            }
        }

        if (ungranted.size() != 0) {
            ActivityCompat.requestPermissions(this,
                    ungranted.toArray(new String[0]),
                    REQUEST_APP_PREF);
        } else {
            Log.d(TAG, "hasPermissions");
            initializeFacetecSDK();
        }
    }

    private void initializeFacetecSDK() {

        Config.appContext = this;
        SessionToken = getIntent().getStringExtra("SessionToken");
      	LineActivation = getIntent().getBooleanExtra("LineActivation",false); /** Cambio realizado */

        if (getIntent().getStringExtra("ScanResultBlob") != null) {
            ScanResultBlob = getIntent().getStringExtra("ScanResultBlob");
        }

        /***********************************************************************************************
         * SDK keys - begin
         * this is what you want to replace
         * Remember to keep the same format
         */
        String DKI_Key = getIntent().getStringExtra("DeviceKeyIdentifier");
        String PKM_EndDate = getIntent().getStringExtra("ProductionKeyMobileEndDate");
        String PKM_Key = getIntent().getStringExtra("ProductionKeyMobile");
        String PFSEK_Key = getIntent().getStringExtra("PublicFaceScanEncritptionKey");

        String DeviceKeyIdentifier = DKI_Key;
        String ProductionKeyMobile = "appId      = *\n" +
                "expiryDate = " + PKM_EndDate + "\n" +
                "key        = " + PKM_Key + "\n";
        String PublicFaceScanEncritptionKey = "-----BEGIN PUBLIC KEY-----\n" + PFSEK_Key + "-----END PUBLIC KEY-----";

        /**
         * Initialize SDK
         */
        FaceTecSDK.initializeInProductionMode(this, ProductionKeyMobile, DeviceKeyIdentifier,
                PublicFaceScanEncritptionKey, callback);

        /**
         * Customize SDK - Vocal guidance OFF
         */
        FaceTecSDK.setCustomization(Config.currentCustomization);
        Config.currentCustomization.vocalGuidanceCustomization.mode = FaceTecVocalGuidanceCustomization.VocalGuidanceMode.NO_VOCAL_GUIDANCE;

        /**
         * Call Facetec
         */
        lastProcessor = new PhotoIDMatchProcessor(SessionToken, LineActivation, this); /** Cambio realizado */

        Intent i = new Intent();

        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
