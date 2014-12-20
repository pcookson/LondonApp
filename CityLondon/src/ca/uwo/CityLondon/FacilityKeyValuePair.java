package ca.uwo.CityLondon;

import java.net.URL;

public class FacilityKeyValuePair {

	public String key;
	public URL value;

	public FacilityKeyValuePair(String k, URL absUrl) {
		key = k;
		value = absUrl;
	}
}
