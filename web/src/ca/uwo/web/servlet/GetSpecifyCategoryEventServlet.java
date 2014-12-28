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

import ca.uwo.web.dao.NewsDAO;
import ca.uwo.web.model.News;
import ca.uwo.web.util.TextUtility;


/**
 *@author Shunwei Zhu
 *@date Sep 27, 2013
 *http://localhost:8080/web/getSpecifyCategoryNews?fromDate=20130901&count=5
 */
public class GetSpecifyCategoryEventServlet extends HttpServlet
{
	private static final long serialVersionUID = -1255068904857986077L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		response.setContentType("text/html;charset=UTF-8");

		String fromDate = request.getParameter("fromDate");
		String cid = request.getParameter("cid");
		String startidStr = request.getParameter("startid");
		String countStr = request.getParameter("count");
		
		
		int startid = 0;
		int count = 0;
		startid = TextUtility.String2Int(startidStr);
		count = TextUtility.String2Int(countStr);
		JSONObject jObject = new JSONObject();
		try
		{
			NewsDAO newsDAO = new NewsDAO();
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			ArrayList<News> retList = new ArrayList<News>();

			retList = newsDAO.getSpecifyCategoryNews(fromDate, cid, startid, count);

		
			for (News news : retList)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("title", news.getTitle());

				map.put("fromDate", news.getFromDate());
				map.put("toDate", news.getToDate());
				map.put("phone", news.getPhone());
				map.put("address", news.getAddress());
				map.put("urlTitle", news.getUrlTitle());
				
				list.add(map);
			}
			JSONObject jObject2 = new JSONObject();
			jObject2.put("totalnum", retList.size());
			jObject2.put("newslist", list);

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
			throws ServletException, IOException
	{
		throw new NotImplementedException();
	}
}
