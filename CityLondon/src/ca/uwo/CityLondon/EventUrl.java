/*  
 * Get all the event detail pages' url 
*/
package ca.uwo.CityLondon;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EventUrl{
		
	
	//get event category urls (Art, Craft Shows & Markets, etc.) and store them into array list urlList
	public static ArrayList<String> getCategoryUrl(){
		
		ArrayList<String> urlList = new ArrayList<String>();  //define String type array list
		
	    try{
	        //Seed URL
	        Document doc = Jsoup.connect("http://www.londontourism.ca/Events").get();
	 
	        //Find out all the child elements of ul#type_menu
	        Elements items = doc.select("ul#type_menu");
	          //for each element, locate to tag "h3" elements
	        for (Element item : items){
	        	Elements links = item.select("li").select("h3");
	        	//get URL of each category
	        	for (Element link : links){
	        		String linkHref = link.select("a").attr("href");
	        		//add category url
	        		urlList.add(linkHref);
	        		}	
	        }

	    
	    }catch(Exception e){
	        System.err.println("Caught IOException: " + e.getMessage());
	    }
	    return urlList;
	}

	
	
	//Get all all event detail pages' urls and return urls in array list
	public static ArrayList<String> getEventUrl(){
		
		ArrayList<String> eventUrlFullList = new ArrayList<String>(); //testing
		ArrayList<String> eventUrlListPerCat = new ArrayList<String>();  //String type array list
		ArrayList<String> urlList = new ArrayList<String>();
		
		//get event category then get event detail pages' url by event type
		urlList = getCategoryUrl();		
		
		// for each event type
		for (int i = 0; i < urlList.size(); i++){
	    try{
	        //page 1 event list in specific event type
	    	String u = urlList.get(i);
	        Document doc = Jsoup.connect(u).get();
	        
	        //get event detail pages' url in event list page 1
	        eventUrlListPerCat = getEventUrl(doc);
	        	                         
	        int flag = 0;             	                       
	        while (flag == 0) {  
	        	// check if there is next page  
	        	boolean isExist = true;  
	            isExist = isExistsNextPage(doc);  
	            
	            // connect to next page
	            int k = 2;  
	            while (isExist) {  
	            	//Format next event list page url
	            	String urlNext = u + "/Page/" + k + ".html"; 
	            	// parse next event list page
	            	doc = Jsoup.connect(urlNext).get();
	            	
	            	//get all the event detail pages' url and add to event detail url list
	            	ArrayList<String> nextUrlList = new ArrayList<String>();      
	            	nextUrlList = getEventUrl(doc);
	            	eventUrlListPerCat.addAll(nextUrlList);
	                
	            	//check if another next page
	                isExist = isExistsNextPage(doc);  	                                
	                k++;  
	            }  
	            flag = 1;  	                        
	        }  
	        
        	eventUrlFullList.addAll(eventUrlListPerCat);

	        /*Test code
	        for (int j = 0; j < eventUrlListPerCat.size(); j++) {
	        	  System.out.println(eventUrlListPerCat.get(j));
	        	}*/
	    	   
	        
	    }catch(Exception e){
	        System.err.println("Caught IOException: " + e.getMessage());
	    }
		
	}
	    return eventUrlFullList;
	    	
	}

	
	
	//check if next page exist
	public static boolean isExistsNextPage(Document doc) {  
   
		         Elements e = doc.select(":has(.next)");  
		         if (e.size() > 0) {  
		           return true;  
		         } else  
		           return false;  
		     }  
	
	
	
	//get event detail page url from event list page
	public static ArrayList<String> getEventUrl(Document doc) {
		ArrayList<String> eventUrlListPerCat = new ArrayList<String>(); 
        Elements items = doc.select("ul.listings");
          //get url
        String linkHref = null;
        for (Element item : items){
        	Elements links = item.select("div").select("h4");
        	for (Element link : links){
        		linkHref = link.select("a").attr("href");
        		//add event detial url into event url list
        		eventUrlListPerCat.add(linkHref);
        	}
        }
        return eventUrlListPerCat;	   
	}

}

