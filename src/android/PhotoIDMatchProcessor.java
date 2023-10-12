package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import android.content.Context;
import android.util.Log;

import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecFaceScanProcessor;
import com.facetec.sdk.FaceTecFaceScanResultCallback;
import com.facetec.sdk.FaceTecIDScanProcessor;
import com.facetec.sdk.FaceTecIDScanResult;
import com.facetec.sdk.FaceTecIDScanResultCallback;
import com.facetec.sdk.FaceTecIDScanStatus;
import com.facetec.sdk.FaceTecSDK;
import com.facetec.sdk.FaceTecSessionActivity;
import com.facetec.sdk.FaceTecSessionResult;
import com.facetec.sdk.FaceTecSessionStatus;

import org.json.JSONObject;
import java.util.ArrayList;

class PhotoIDMatchProcessor extends Processor implements FaceTecFaceScanProcessor, FaceTecIDScanProcessor {

    private boolean isSuccess = false;
    private boolean isOKGettingFacialData = false;
    private boolean isOKGettingFacialIsScanData = false;
  	private boolean LineActivation;
    JSONObject parameters = new JSONObject();

    public PhotoIDMatchProcessor(String sessionToken, boolean LineActivation, Context context) {
      
		this.LineActivation = LineActivation; 
        FaceTecSessionActivity.createAndLaunchSession(context, PhotoIDMatchProcessor.this, PhotoIDMatchProcessor.this,
                sessionToken);
        return;

    }

    /***************************************************************************************************
     * Facetec - Face scan
     */
    @Override
    public void processSessionWhileFaceTecSDKWaits(FaceTecSessionResult faceTecSessionResult,
            final FaceTecFaceScanResultCallback faceTecFaceScanResultCallback) {

        FacetecActivity.faceScanResultCallback = faceTecFaceScanResultCallback;

        if (faceTecSessionResult.getStatus() != FaceTecSessionStatus.SESSION_COMPLETED_SUCCESSFULLY) {
            faceTecFaceScanResultCallback.cancel();
            return;
        }

        try {
            parameters.put("XUserAgent",
                    FaceTecSDK.createFaceTecAPIUserAgentString(faceTecSessionResult.getSessionId()));
            parameters.put("FaceScan", faceTecSessionResult.getFaceScanBase64());
            parameters.put("AuditTrailImage", faceTecSessionResult.getAuditTrailCompressedBase64()[0]);
            parameters.put("LowQualityAuditTrailImage",
                    faceTecSessionResult.getLowQualityAuditTrailCompressedBase64()[0]);

            Shared.sharedPref("params", parameters.toString());

            FaceTecCustomization.overrideResultScreenSuccessMessage = "Continuemos...";
            isOKGettingFacialData = true;
            //faceTecFaceScanResultCallback.succeed();
			if (this.LineActivation) {
                faceTecFaceScanResultCallback.cancel();
            } else{
            	faceTecFaceScanResultCallback.succeed();
            }
          
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /***************************************************************************************************
     * Facetec - ID Scan
     */
    @Override
    public void processIDScanWhileFaceTecSDKWaits(FaceTecIDScanResult faceTecIDScanResult,
            final FaceTecIDScanResultCallback faceTecIDScanResultCallback) {

        FacetecActivity.idScanResultCallback = faceTecIDScanResultCallback;

        if (faceTecIDScanResult.getStatus() != FaceTecIDScanStatus.SUCCESS) {
            faceTecIDScanResultCallback.cancel();
            return;
        }

        try {
            parameters.put("IdScan", faceTecIDScanResult.getIDScanBase64());
            ArrayList<String> frontImagesCompressedBase64 = faceTecIDScanResult.getFrontImagesCompressedBase64();
            ArrayList<String> backImagesCompressedBase64 = faceTecIDScanResult.getBackImagesCompressedBase64();
            if (frontImagesCompressedBase64.size() > 0) {
                parameters.put("IdScanFrontImage", frontImagesCompressedBase64.get(0));
            }
            if (backImagesCompressedBase64.size() > 0) {
                parameters.put("IdScanBackImage", backImagesCompressedBase64.get(0));
            }

            Shared.sharedPref("params", parameters.toString());

            isOKGettingFacialIsScanData = true;
            faceTecIDScanResultCallback.cancel();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FaceTecSDKSampleApp", "Exception raised while attempting to create JSON payload for upload.");
        }
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public boolean isOKGettingFacialData() {
        return this.isOKGettingFacialData;
    }

    public boolean isOKGettingFacialIsScanData() {
        return this.isOKGettingFacialIsScanData;
    }
}
