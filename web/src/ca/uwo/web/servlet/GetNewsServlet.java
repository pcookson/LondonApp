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


public class GetNewsServlet extends HttpServlet
{
	private static final long serialVersionUID = -7715894432269979527L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		response.setContentType("text/html;charset=UTF-8");
//		String eventTitle= request.getParameter("eventTitle");
		String urltitle = request.getParameter("urltitle");
		JSONObject jObject = new JSONObject();
		try
		{
//			CommentDAO commentDAO = new CommentDAO();
//			long commentCount = commentDAO.getSpecifyNewsCommentsCount(nid);
			NewsDAO newsDAO = new NewsDAO();
//			News news = newsDAO.getNews(eventTitle);
			System.out.println("before database, urltitle=" + urltitle);
			News news = newsDAO.getNews(urltitle);
			JSONObject jObject2 = new JSONObject();
			if (!TextUtility.isNull(news.getTitle()))
			{
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				
				
				/***************TextView with photo********************/
				String newsbody = news.getBody();
				ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
				HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
				hashMap1.put("index", 0);
				hashMap1.put("type", "image");
				hashMap1.put("value", news.getImgSrc());
				
				HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
				hashMap2.put("index", 1);
				hashMap2.put("type", "text");
				hashMap2.put("value", newsbody);
				
				list.add(hashMap1);
				list.add(hashMap2);
			/********************************************************/
				
				hashMap.put("title", news.getTitle());

				hashMap.put("body", list);

				jObject2.put("news", hashMap);
			}
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
