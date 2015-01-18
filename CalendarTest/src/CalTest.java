import java.util.Calendar;



public class CalTest {

	public static void main(String args[]){
		Calendar cal = Calendar.getInstance();
		cal.clear();
		String date = "20150103";
		String dateNull = "2015null03";
		
		String year = date.substring(0, 4);
		String month = date.substring(4, 6);
		String day = date.substring(6);
		
		
		
		cal.clear();
		
		cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
		
		long time = cal.getTimeInMillis();
		int yeari = cal.get(Calendar.YEAR);
		System.out.println(year);
		
		System.out.println(cal.toString());
		
		
		
		
		
	}
}
