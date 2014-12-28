package ca.uwo.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import ca.uwo.web.dao.BusDAO;
import ca.uwo.web.model.News;
import ca.uwo.web.util.DistanceUtility;



public class GetBusServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		response.setContentType("text/html;charset=UTF-8");

		String facilityXcoord = request.getParameter("facilityx");
		String facilityYcoord = request.getParameter("facilityy"); 
		JSONObject jObject = new JSONObject();
		try
		{

			BusDAO busDAO = new BusDAO();
			
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			ArrayList<News> retList = new ArrayList<News>();
			
			System.out.println("getBus=" + facilityXcoord + "\n" + facilityYcoord);
			retList = busDAO.getBus(facilityXcoord,facilityYcoord);
			
			
			for (News news : retList)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("stopNumber", news.getStopNumber());
				map.put("stopName", news.getStopName());
				map.put("bus", news.getBus());
				map.put("stopXcoord", news.getXcoord());
				map.put("stopYcoord", news.getYcoord());
				
				//System.out.println("location = " + Double.parseDouble(facilityYcoord) + "\n" + Double.parseDouble(facilityXcoord) + "\n" + news.getYcoord() + "\n" + news.getXcoord());
				//è®¡ç®—ç«™ç‚¹ä¸¾ä¾‹ lat = y, lng = x
				double distance = DistanceUtility.getDistance(Double.parseDouble(facilityYcoord), Double.parseDouble(facilityXcoord), news.getYcoord(), news.getXcoord());
				//System.out.println("distance=" + distance);
				map.put("distance", distance);
				
				list.add(map);
			}
			
				//é‡�æ–°æŽ’åº�
			ArrayList<HashMap<String, Object>> sortList = new ArrayList<HashMap<String, Object>>();
			sortList = sort(list);
			
			JSONObject jObject2 = new JSONObject();
			jObject2.put("totalnum", retList.size());
			jObject2.put("newslist", sortList);

			jObject.put("ret", 0);
			jObject.put("msg", "ok");
			jObject.put("data", jObject2);
		} catch (Exception e)
		{
			e.printStackTrace();
			try
			{
				jObject.put("ret", 1);
				jObject.put("msg", e.getMessage());
				jObject.put("data", "");
			} catch (JSONException ex)
			{
				ex.printStackTrace();
			}
		}
		PrintWriter out = response.getWriter();
		out.println(jObject);
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		throw new NotImplementedException();
	}
	
	//sort hashmap array list from small to big
	private static ArrayList<HashMap<String, Object>> sort(ArrayList<HashMap<String, Object>> list) { 
		for (int i = 0; i < list.size(); i++) { 
			for (int j = i + 1; j < list.size(); j++) { 
				double iDistance = (double)list.get(i).get("distance");
				double jDistance = (double)list.get(j).get("distance");
				if (iDistance > jDistance) { 
					HashMap<String, Object> temp = list.get(i); 
					list.set(i, list.get(j)); 
					list.set(j, temp); 
					} 
				} 
			} 
		return list; 
		} 

}
