package ca.uwo.CityLondon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 

public class MySql {

	    public String driver = "com.mysql.jdbc.Driver";
	    public String url = "jdbc:mysql://127.0.0.1:3306/testdb";
	    public String user = "root";
	    public String password = "nintendo"; //patricks password
	    public Statement stmt = null;
	    public Connection conn = null;
	    
	    public void intMySql() {
	        try {
	            try {
	                Class.forName(driver).newInstance();
	            } catch (Exception e) {
	                System.out.println("Unable to find the local driver");
	                e.printStackTrace();
	            }
	            conn = DriverManager.getConnection(url, user, password);
	            stmt = conn.createStatement();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }    	
	    }
	    
	    public void datatoMySql(String insertSQl) {

	        try {
	            stmt.executeUpdate(insertSQl);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        try {
	            stmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    //update
	    public void updateMySql(String updateSQl) {

	        try {
	            stmt.executeUpdate(updateSQl);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        try {
	            stmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public int searchMySql(String title) {
        	int found = 0;
        	String sql1 = "select * from Event where title= '"+title+"'; ";
	        try {	        	
	        	ResultSet rs = stmt.executeQuery(sql1);     
	            if(rs.next()){   
	            	found = 1;
	            }
	            else
	            	found = 0;
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        try {
	            stmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return found;
	    }
	    
	    public int searchMySqlFacility(String facilityCategory, String facilityName) {
        	int found = 0;
        	String sql2 = "select * from facility where category = '"+facilityCategory+"' and name = '"+facilityName+"'; ";

	        try {	        	
	        	ResultSet rs = stmt.executeQuery(sql2);     
	            if(rs.next()){   
	            	found = 1;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        try {
	            stmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return found;
	    }
	    
	    public int searchBus(double stopNumberNum) {
        	int found = 0;
        	String stopNumber = String.valueOf(stopNumberNum);
        	String sql2 = "select * from bus where stop_number = '"+stopNumber+"'; ";

	        try {	        	
	        	ResultSet rs = stmt.executeQuery(sql2);     
	            if(rs.next()){   
	            	found = 1;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        try {
	            stmt.close();
	            conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return found;
	    }
	    
	}

