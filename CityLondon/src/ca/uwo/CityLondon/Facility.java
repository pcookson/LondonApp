/* Main entry for facility information collecting
 * Facility information will be collected from excel files and stored into table "facility"
 * 
 */


package ca.uwo.CityLondon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Facility {

	public static void main(String[] args) throws IOException {
		
		//Create array list to store the facility category name and corresponding excel file url
		ArrayList<FacilityKeyValuePair> facilityList = new ArrayList<FacilityKeyValuePair>();
		
		//get array list which include the category name and corresponding excel file url
		facilityList = FacilityUrl.getFacilityUrl();
		
		//for each excel file
		for (int i = 0; i < facilityList.size(); i++) {
			try {
				// get the category and url
				String n = facilityList.get(i).key;
				URL u = facilityList.get(i).value;
				//***obsolete***String u = facilityList.get(i).value.toString();
				

				// capture only the below category
				switch (n) {
				case "Arenas":
				case "Baseball diamonds":
				case "Basketball courts":
				case "Community centres":
				case "Community gardens":
				case "Community pools":
				case "Football fields":
				case "Multi-use pads":
				case "Outdoor ice rinks":
				case "Skateboard parks":
				case "Soccer fields":
				case "Swing sets":
				case "Tennis courts":
				case "Wading pools":
				case "London Transit Bus Stops (Sept 2011)":
					
					System.out.println(n);//reference code
					System.out.println(u);//reference code
					
					//download the excel file to PC and get file object
					File f = DownloadExcel.downLoadFile(n, u);
					//parse the excel file object and store into Mysql
					ExcelReader.parseFacilityDetail(n, f);

					break;
				}

			} catch (Exception e) {
				System.err.println("Caught IOException: " + e.getMessage());
			}
		}
	}
}
