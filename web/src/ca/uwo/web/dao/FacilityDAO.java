package ca.uwo.web.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.uwo.web.model.News;

public class FacilityDAO
{
	SqlManager manager;
	String sql = "";
	ResultSet rs;

	public FacilityDAO() throws IOException, ClassNotFoundException
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


	public ArrayList<News> getSpecifyCategoryFacility(String cid, int startid, int count)

			throws SQLException
	{
		ArrayList<News> list = new ArrayList<News>();
		String category = null;
		switch (cid){
		case "01": category = "Arenas";
		break;
		case "02": category = "Baseball diamonds";
		break;
		case "03": category = "Basketball courts";
		break;
		case "04": category = "Community centres";
		break;
		case "05": category ="Community gardens";
		break;
		case "06": category ="Community pools";
		break;
		case "07": category ="Football fields";
		break;
		case "08": category ="Multi-use pads";
		break;
		case "09": category ="Outdoor ice rinks";
		break;
		case "10": category ="Skateboard parks";
		break;
		case "11": category ="Soccer fields";
		break;
		case "12": category ="Swing sets";
		break;
		case "13": category ="Tennis courts";
		break;
		case "14": category ="Wading pools";
		break;
		}
		
		sql = "SELECT * FROM facility WHERE category = ? LIMIT ?,?";
		
		Object[] params = new Object[]{category, startid, count};
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		while (rs.next())
		{
			News news = new News();

			news.setCategory(rs.getString("category"));
			news.setName(rs.getString("name"));
			news.setAddress(rs.getString("address"));
			news.setXcoord(rs.getDouble("xcoord"));
			news.setYcoord(rs.getDouble("ycoord"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
}
