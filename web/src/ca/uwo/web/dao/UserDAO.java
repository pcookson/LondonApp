package ca.uwo.web.dao;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class UserDAO
{
	SqlManager manager;
	String sql = "";
	ResultSet rs;
	
	public UserDAO() throws IOException, ClassNotFoundException, SQLException
	{
		manager = SqlManager.createInstance();
	}
	

	public boolean validate(String uname,String password) throws SQLException
	{
		boolean result = false;
		sql = "select count(uid) from t_user where uname=? and password=?";
		Object[] params = new Object[]{uname,password};
		manager.connectDB();
		rs = manager.executeQuery(sql, params);
		if (rs.next()&&rs.getLong(1)==1)
		{
			result = true;
		}
		manager.closeDB();
		return result;
	}
	

	public void update(String uname,String password)
	{
		throw new NotImplementedException();
	}
}
