const EntelFacetecPlugin = function() {}


EntelFacetecPlugin.prototype.getfaceresponse = function(
    sessionToken, 
    LineActivation, 
    deviceKeyIdentifier,
    productionKeyMobileEndDate,
    productionKeyMobile,
    publicFaceScanEncritptionKey,
    successCallback, 
    errorCallback) {

    var options = {};
    
    options.sessionToken = sessionToken;
	options.LineActivation = LineActivation;
    options.deviceKeyIdentifier = deviceKeyIdentifier;
    options.productionKeyMobileEndDate = productionKeyMobileEndDate;
    options.productionKeyMobile = productionKeyMobile;
    options.publicFaceScanEncritptionKey = publicFaceScanEncritptionKey;

    cordova.exec(successCallback, errorCallback, 'EntelFacetecPlugin', 'start', [options]);
}


EntelFacetecPlugin.install = function() {

    if(!window.plugins) { 
        window.plugins = {};
    }

    window.plugins.EntelFacetecPlugin = new EntelFacetecPlugin();
    return window.plugins.EntelFacetecPlugin;
}


cordova.addConstructor(EntelFacetecPlugin.install);
