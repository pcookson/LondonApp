/* main function of events
 * 
 */
package ca.uwo.CityLondon;

import java.util.ArrayList;

public class Event {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> urlList = new ArrayList<String>(); 
		urlList = EventUrl.getEventUrl();
		EventParser event = new EventParser();
		
		for (int i = 0; i < urlList.size(); i++){
		    try{
		    	String u = urlList.get(i);
		    	System.out.println(u);
		    	event.parseEventDetail(u);
		    }catch(Exception e){
		        System.err.println("Caught IOException: " + e.getMessage());
		    }
		}

	}

}
