package com.echoss.svc.common.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

public class LocationUtil {
	private static final Logger logger = LoggerFactory.getLogger(LocationUtil.class);

	public static TAData getCoordinateByGoogleAPI(String address, String lang) {

		final Geocoder geocoder = new Geocoder();

		GeocodeResponse geocoderResponse = null;
		try {
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage(lang).getGeocoderRequest();
			geocoderResponse = geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			logger.error("", e);
			return null;
		}

		String latitude = "";
		String longitude = "";

		List<GeocoderResult> geocoderResults = geocoderResponse.getResults();
		for(GeocoderResult result : geocoderResults) {
			BigDecimal lat = result.getGeometry().getLocation().getLat();
			BigDecimal lng = result.getGeometry().getLocation().getLng();

			latitude = lat.toString();
			longitude = lng.toString();
			break;
		}

		TAData results = new TAData();

		results.set("latitude", latitude);
		results.set("longitude", longitude);

		return results;
	}

	public static void main(String [] args) {
		String address = "경기도 성남시 분당구 삼평동 613";
		String lang = "ko";
		
		address = "東京都文京区後楽2-6-1 飯田橋ファーストタワー";
		lang = "ja";
		
		TAData results = getCoordinateByGoogleAPI(address, lang);
		logger.debug(results.toString());
    }
}
