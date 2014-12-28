package ca.uwo.web.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ca.uwo.web.model.News;


public class BusDAO {
	SqlManager manager;
	String sql = "";
	ResultSet rs;
	double disParm = 0.01;// distance between bus stops and target facility

	public BusDAO() throws IOException, ClassNotFoundException {
		manager = SqlManager.createInstance();
	}

	public ArrayList<News> getBus(String facilityXcoord, String facilityYcoord)

	throws SQLException {
		ArrayList<News> list = new ArrayList<News>();
		double facilityXcoordNum = Double.parseDouble(facilityXcoord);
		double facilityYcoordNum = Double.parseDouble(facilityYcoord);

		double facilityXcoordMin = facilityXcoordNum - disParm;
		double facilityXcoordMax = facilityXcoordNum + disParm;
		double facilityYcoordMin = facilityYcoordNum - disParm;
		double facilityYcoordMax = facilityYcoordNum + disParm;

		sql = "SELECT * FROM bus WHERE xcoord<? AND xcoord>? AND ycoord<? AND ycoord>?";
		
		System.out.println("sql=" + sql + "facilityXcoordMin=" + facilityXcoordMin + "facilityXcoordMax=" + facilityXcoordMax + "facilityYcoordMin=" + facilityYcoordMin +"facilityYcoordNum="+ facilityYcoordNum);

		Object[] params = new Object[] { facilityXcoordMax, facilityXcoordMin,
				facilityYcoordMax, facilityYcoordMin };
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		while (rs.next()) {
			News news = new News();
			String stopNumberStr = String.valueOf(rs.getDouble("stop_number"));
			news.setStopNumber(stopNumberStr);
			news.setStopName(rs.getString("stop_name"));
			news.setBus(rs.getString("bus"));
			news.setXcoord(rs.getDouble("xcoord"));
			news.setYcoord(rs.getDouble("ycoord"));
			list.add(news);
		}
		manager.closeDB();
		return list;
	}
}
