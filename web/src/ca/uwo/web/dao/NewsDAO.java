package ca.uwo.web.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.uwo.web.model.News;
import ca.uwo.web.util.TextUtility;

public class NewsDAO
{
	SqlManager manager;
	String sql = "";
	ResultSet rs;

	public NewsDAO() throws IOException, ClassNotFoundException
	{
		manager = SqlManager.createInstance();
	}


	public News getNews(String urlTitle) throws SQLException
	{
		sql = "SELECT title, description, photo, url_title FROM Event WHERE url_title=?";
		Object[] params = new Object[]{ urlTitle };
		System.out.print("sql=" + sql + "url_title =" + urlTitle);
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		News news = new News();
		if (rs.next())
		{

			news.setTitle(rs.getString("title"));
			news.setBody(rs.getString("description"));
			news.setImgSrc(rs.getString("photo"));
			news.setUrlTitle(rs.getString("url_title"));
		}
		manager.closeDB();
		return news;
	}


	public ArrayList<News> getSpecifyCategoryNews(String fromDate, String cid, int startid, int count)

			throws SQLException
	{
		ArrayList<News> list = new ArrayList<News>();
		
		String currentYearStr = fromDate.substring(0, 4);
		String currentMonthStr = fromDate.substring(4, 6);

		int currentYear = TextUtility.String2Int(currentYearStr);
		int currentMonth = TextUtility.String2Int(currentMonthStr);
		int cidInt = TextUtility.String2Int(cid);
		
		int categoryYear=0;

		
		if (cidInt < currentMonth)
		    categoryYear = currentYear + 1;
		else 
			categoryYear = currentYear;
		
		String cateogryDateFrom = String.valueOf(categoryYear) + cid + "00";
		String categoryDateTo = String.valueOf(categoryYear) + cid + "32";	
		
		
		
		sql = "SELECT title, phone_number, from_date,to_date,address,url_title FROM Event WHERE from_date > ? and from_date < ? ORDER BY from_date LIMIT ?,?";
		
		Object[] params = new Object[]{cateogryDateFrom, categoryDateTo, startid, count};
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		while (rs.next())
		{
			News news = new News();

			news.setTitle(rs.getString("title"));
			news.setPhone(rs.getString("phone_number"));

			news.setFromDate(rs.getString("from_date"));
			news.setToDate(rs.getString("to_date"));
			news.setAddress(rs.getString("address"));
			news.setUrlTitle(rs.getString("url_title"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
}
