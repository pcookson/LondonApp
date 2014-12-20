/*
 * Parse city london open data page, get all the excel file urls (with category names)
 */

package ca.uwo.CityLondon;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class FacilityUrl {

	public static ArrayList<FacilityKeyValuePair> getFacilityUrl() {

		// define "category name - excel file url" array list
		ArrayList<FacilityKeyValuePair> facilityList = new ArrayList<FacilityKeyValuePair>();
		String fileName = null;
		String fileUrl = null;

		try {
			// city london open data page for facility
			Document doc = Jsoup
					.connect(
							"http://www.london.ca/city-hall/open-data/Pages/Open-Data-Data-Catalogue.aspx")
					.get();

			// parse web page
			Elements items = doc.select("table").select("tr").select("table")
					.select("tr");
			for (Element item : items) {
				// find out all the xls or xlsx (href includes .xls)
				Elements links = item.select("[href*=.xls]");

				// for each excel file element
				for (Element link : links) {
					// get parent node
					Element title = link.parent();
					// use parent node to get the excel file category name
					fileName = title.firstElementSibling().text();
					fileUrl = link.attr("href");

					// change relative url to absolute url
					URI base = new URI(
							"http://www.london.ca/d.aspx?s=/Open_Data/Data_Catalogue.htm");// Base URI
					URI abs = base.resolve(fileUrl);// 解析于上述网页的相对URL，得到绝对URI
					URL absUrl = abs.toURL();// 转成URL
					
					/* test code
					 * System.out.println(fileName); 
					 * System.out.println(absUrl);
					 */
					
					//add "category name - facility excel file url" to array list
					facilityList
							.add(new FacilityKeyValuePair(fileName, absUrl));
				}

			}

		} catch (Exception e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
		return facilityList;
	}

}
