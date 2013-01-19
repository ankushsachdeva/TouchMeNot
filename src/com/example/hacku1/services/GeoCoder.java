package com.example.hacku1.services;

import com.example.hacku1.model.GeoCodeResult;

public class GeoCoder {
    
    private static final String YAHOO_API_BASE_URL = "http://nominatim.openstreetmap.org/reverse?format=xml&lat=%1$s&lon=%2$s&zoom=18&addressdetails=2";
    
    private HttpRetriever httpRetriever = new HttpRetriever();
    private XmlParser xmlParser = new XmlParser();
    
    public GeoCodeResult reverseGeoCode(double latitude, double longitude) {
        
        String url = String.format(YAHOO_API_BASE_URL, String.valueOf(latitude), String.valueOf(longitude));        
        String response = httpRetriever.retrieve(url);
        //return response;
        return xmlParser.parseXmlResponse(response);
        
    }

}
