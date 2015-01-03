package ca.uwo.CityLondon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EventParser {

	// public static void parseEventDetail(String url){
	public void parseEventDetail(String url) {

		try {
			Document doc = Jsoup.connect(url)
					.maxBodySize(0)
	        		.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
	        		.get();

			EventDataStructure ds = new EventDataStructure();
			MySql ms = new MySql();
			String sqlInsertEvent = "";

			// Event Title
			Element title = doc.select("div.details_container.bigger_nav").select("h2").first();
			String eventTitle = title.text();
			if (eventTitle.indexOf("'") != -1) {// check if there is '
				ds.title = eventTitle.replace("'", "''");
			} else
				ds.title = eventTitle;


			//Contact Information
			String phoneNumber = "";
			// Event Phone
			Elements item = doc.select("div.details_container.bigger_nav").select("div.information").select("ul.contact").select("li");
			for(Element el : item){
				String telephone = el.childNode(1).toString();
				if(telephone.equals("<span>Telephone:</span>")){
					phoneNumber = el.nextElementSibling().childNode(2).toString();
					break;
				}
				//System.out.println();
				
			}
			//Element item = doc.select(".phone").first();
			
			ds.phoneNumber = phoneNumber;



			// Event Date
			Element date = doc.select("div.details_container.bigger_nav").select("div.date_information").select("p").first();
			String eventDate = date.text();
			// System.out.println(eventDate);

			// event year
			String eventYear = eventDate.substring(eventDate.length() - 4,
					eventDate.length());
			String eventMonthFrom = null;
			String eventMonthTo = null;
			String fromDay = null;
			String toDay = null;
			String hyphen = "-";
			String zero = "0";
			String comma = ",";
			String[] arr = eventDate.split(" ");

			int a = eventDate.indexOf(hyphen);
			if (a >= 0) {

				// same month
				if (Character.isDigit(eventDate.charAt(a + 2))) {
					String eventMonthEng = arr[0];

					// Convert month from english to number
					String eventMonth = MonthConv(eventMonthEng);
					eventMonthFrom = eventMonth;
					eventMonthTo = eventMonth;

					// format event from day
					String fromDayTemp = arr[1];
					if (fromDayTemp.length() == 1) {
						fromDay = zero.concat(fromDayTemp);
					} else {
						fromDay = fromDayTemp;
					}

					// format event to day
					String toDayTemp = eventDate.substring(a + 2,
							eventDate.indexOf(comma));
					if (toDayTemp.length() == 1) {
						toDay = zero.concat(toDayTemp);
					} else {
						toDay = toDayTemp;
					}

				}

				// Different months
				else {
					String eventMonthFromEng = arr[0];
					String eventMonthToEng = arr[4];
					eventMonthFrom = MonthConv(eventMonthFromEng);
					eventMonthTo = MonthConv(eventMonthToEng);

					// format event from day
					String fromDayTemp = arr[1];
					if (fromDayTemp.length() == 1) {
						fromDay = zero.concat(fromDayTemp);
					} else {
						fromDay = fromDayTemp;
					}

					// format event to day
					String toDayTemp = arr[5];
					if (toDayTemp.length() == 1) {
						toDay = zero.concat(toDayTemp);
					} else {
						toDay = toDayTemp;
					}


				}

			}
			// same date
			else {
				String eventMonthEng = arr[0];
				String eventMonth = MonthConv(eventMonthEng);
				eventMonthFrom = eventMonth;
				eventMonthTo = eventMonth;
				// event day
				String dayTemp = arr[1].substring(0, arr[1].length() - 1);
				if (dayTemp.length() == 1) {
					fromDay = zero.concat(dayTemp);
					toDay = fromDay;
				} else {
					fromDay = dayTemp;
					toDay = fromDay;
				}


			}

			ds.fromDate = eventYear + eventMonthFrom + fromDay;
			ds.toDate = eventYear + eventMonthTo + toDay;



			// Event Address
			Element location = doc.select("div.information").select("p.address").select("p").first();
			String addr1 = location.childNode(2).toString();
			String addr2 = location.childNode(4).toString();
			String eventAddress = addr1 + addr2;
			String eventLocation = location.childNode(0).toString();


			ds.address = eventLocation + "\n" + eventAddress;
//Added on 13Nov.
			//if (ds.address.indexOf("'") != -1) // check if '
				//ds.description = ds.address.replace("'", "''");
//13Nov
			
			// Event Description
			Element description = doc.select("div.description").select("p").first();
			
			String eventDescription = description.text();
			// test code
			// System.out.println(eventDescription);
			if (eventDescription.indexOf("'") != -1) {// check if '
				ds.description = eventDescription.replace("'", "''");
			} else
				ds.description = eventDescription;

			// Event Photo
			Element image = doc.select("ul#mediashow").select("li").select("img").first();
			String imgUrl = image.absUrl("src");
			ds.photo = imgUrl;

			// Event url title
			ds.eventUrlTitle = url.substring(url.lastIndexOf("/") + 1);
			// test code
			//System.out.println(imgUrl);

			// initiate connection
			ms.intMySql();

			// check if event already existed
			int found = 0;

			found = ms.searchMySql(ds.title);

			if (found == 0) {

				// initiate connection
				ms.intMySql();
				// MySql insert
				sqlInsertEvent = "INSERT INTO Event values('" + ds.title + "',"
						+ "\"" + ds.phoneNumber + "\"," + "\"" + ds.fromDate
						+ "\"," + "\"" + ds.toDate + "\"," + "\"" + ds.address
						+ "\"," + "'" + ds.description + "'," + "\"" + ds.photo
						+ "\"," + "\"" + ds.eventUrlTitle + "\")";


				ms.datatoMySql(sqlInsertEvent);

			}
		} catch (Exception e) {
			// error handling
		}
	}

	public static String MonthConv(String eventMonthEng) {
		// Convert month from english to number
		String eventMonth = null;
		switch (eventMonthEng) {
		case "January":
			eventMonth = "01";
			break;
		case "February":
			eventMonth = "02";
			break;
		case "March":
			eventMonth = "03";
			break;
		case "April":
			eventMonth = "04";
			break;
		case "May":
			eventMonth = "05";
			break;
		case "June":
			eventMonth = "06";
			break;
		case "July":
			eventMonth = "07";
			break;
		case "August":
			eventMonth = "08";
			break;
		case "September":
			eventMonth = "09";
			break;
		case "October":
			eventMonth = "10";
			break;
		case "November":
			eventMonth = "11";
			break;
		case "December":
			eventMonth = "12";
			break;
		}
		return eventMonth;
	}

}
