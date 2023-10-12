package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;

/**
 * This class echoes a string called from JavaScript.
 */
public class EntelFacetecPlugin extends CordovaPlugin {

    private CallbackContext scanCallbackContext;

    private String TAG = "EntelFacetecPlugin-Cordova";
    private int REQ_CODE = 1;

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {

        if (action.equals("start")) {

            scanCallbackContext = callbackContext;
            String SessionToken;
          	boolean LineActivation; /** Cambio realizado */
            String DeviceKeyIdentifier;
            String ProductionKeyMobileEndDate;
            String ProductionKeyMobile;
            String PublicFaceScanEncritptionKey;
            

            try {
                JSONObject options = args.getJSONObject(0);

                SessionToken = options.getString("sessionToken");
              	LineActivation = options.getBoolean("LineActivation"); /** Cambio realizado */
                DeviceKeyIdentifier = options.getString("deviceKeyIdentifier");
                ProductionKeyMobileEndDate = options.getString("productionKeyMobileEndDate");
                ProductionKeyMobile = options.getString("productionKeyMobile");
                PublicFaceScanEncritptionKey = options.getString("publicFaceScanEncritptionKey");

                Context appCtx = cordova.getActivity().getApplicationContext();
                Intent getresponseIntent = new Intent(appCtx, FacetecActivity.class);

                getresponseIntent.putExtra("SessionToken", SessionToken);
              	getresponseIntent.putExtra("LineActivation", LineActivation); /** Cambio realizado */
                getresponseIntent.putExtra("DeviceKeyIdentifier", DeviceKeyIdentifier);
                getresponseIntent.putExtra("ProductionKeyMobileEndDate", ProductionKeyMobileEndDate);
                getresponseIntent.putExtra("ProductionKeyMobile", ProductionKeyMobile);
                getresponseIntent.putExtra("PublicFaceScanEncritptionKey", PublicFaceScanEncritptionKey);

                cordova.startActivityForResult(this, getresponseIntent, REQ_CODE);

            } catch (JSONException e) {
                callbackContext.error("Error encountered: " + e.getMessage());
                return false;
            }
            return true;

        } else {

            callbackContext.error("\"" + action + "\" is not a recognized action.");
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "OnActivityResult code = " + String.valueOf(requestCode));

        if (requestCode == REQ_CODE) {

            if (data != null) {

                if (resultCode == Activity.RESULT_OK) {

                    String Params = Shared.obtainedPref("params");

                    JSONObject cordoResponse;
                    try {
                        cordoResponse = new JSONObject(Params);
                        scanCallbackContext.success(cordoResponse);
                        Shared.deletePref("params");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        scanCallbackContext.error("CANCEL");
                    }
                } else {
                    String status = (String) data.getExtras().get("status");
                    scanCallbackContext.error("CANCEL");
                    Log.d(TAG, "Status: " + status);
                }
            } else {
                Log.d(TAG, "Data is empty");
            }
        } else {
            Log.d(TAG, "Result code not recognized");
        }
    }

}
