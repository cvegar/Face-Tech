package com.outsystemsenterprise.entel.PEMiEntel.cordova.plugin;

import android.content.Context;

import com.facetec.sdk.FaceTecCustomization;
import com.facetec.sdk.FaceTecIDScanCustomization;
import com.facetec.sdk.FaceTecOverlayCustomization;
import com.entel.movil.autoactivacion.R;

public class Config {

    public static Context appContext;

    static FaceTecCustomization retrieveConfigurationWizardCustomization() {
        FaceTecCustomization customization = new FaceTecCustomization();
        FaceTecOverlayCustomization faceTecOverlayCustomization = new FaceTecOverlayCustomization();
        faceTecOverlayCustomization.brandingImage = R.drawable.entel_logo_pe;
        FaceTecIDScanCustomization faceTecIDScanCustomization = new FaceTecIDScanCustomization();
        faceTecIDScanCustomization.showSelectionScreenBrandingImage = true;
        customization.getFeedbackCustomization().enablePulsatingText = true;
        customization.setOverlayCustomization(faceTecOverlayCustomization);
        return customization;
    }

    static FaceTecCustomization currentCustomization = retrieveConfigurationWizardCustomization();
}
