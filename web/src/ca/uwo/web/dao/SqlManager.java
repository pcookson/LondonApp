package ca.uwo.web.dao;


import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.PropertyResourceBundle;

public class SqlManager
{
	private static SqlManager manager = null; 
	private PropertyResourceBundle bundle; 
	private static String jdbcDrive = null; 
	private String DBhost = ""; 
	private String DBname = ""; 
	private String DBprot = ""; 
	private String DBuser = ""; 
	private String DBpasswd = ""; 
	private String strcon = null; 

	private Connection conn = null; 
	private PreparedStatement pstm = null;
	private CallableStatement cstm = null;


	private SqlManager() throws IOException
	{
		bundle = new PropertyResourceBundle(SqlManager.class
				.getResourceAsStream("Config.properties"));
		this.DBhost = getString("DBhost"); 
		this.DBname = getString("DBname"); 
		this.DBprot = getString("DBport"); 
		this.DBuser = getString("DBuser"); 
		this.DBpasswd = getString("DBpassword"); 
		jdbcDrive = "com.mysql.jdbc.Driver";
		strcon = "jdbc:mysql://" + DBhost + ":" + DBprot + "/" + DBname;
	}


	private String getString(String key)
	{
		return this.bundle.getString(key);
	}


	public static SqlManager createInstance() throws IOException, ClassNotFoundException
	{
		if (manager == null)
		{
			manager = new SqlManager();
			manager.initDB();
		}
		return manager;
	}


	public void initDB() throws ClassNotFoundException
	{
		Class.forName(jdbcDrive);
	}


	public void connectDB() throws SQLException
	{
		conn = DriverManager.getConnection(strcon, DBuser, DBpasswd); 
		conn.setAutoCommit(false); 
	}


	public void closeDB() throws SQLException
	{
		if (pstm != null)
		{
			pstm.close();
		}
		if (cstm != null)
		{
			cstm.close();
		}
		if (conn != null)
		{
			conn.close();
		}
	}


	private void setPrepareStatementParams(String sql, Object[] params)
			throws SQLException
	{
		pstm = conn.prepareStatement(sql); 
		if (params != null)
		{	
			for (int i = 0; i < params.length; i++) 
			{
				pstm.setObject(i + 1, params[i]);
			}
		}
	}


	public ResultSet executeQuery(String sql, Object[] params)
			throws SQLException
	{ 
		ResultSet rs = null;
		manager.setPrepareStatementParams(sql, params); 
		rs = pstm.executeQuery(); 
		return rs;
	}


	public boolean executeUpdate(String sql, Object[] params)
			throws SQLException 
	{
		boolean result = false;
		manager.setPrepareStatementParams(sql, params); 
		pstm.executeUpdate(); 
		manager.commitChange();
		result = true;
		return result;
	}


	private void commitChange() throws SQLException
	{
		conn.commit();
	}
}
