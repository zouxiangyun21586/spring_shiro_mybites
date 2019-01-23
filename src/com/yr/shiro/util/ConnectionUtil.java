package com.yr.shiro.util;


import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionUtil {
	private static Connection conn = null;
	
	public static Connection getConn()
	{
		if(conn == null)
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = (Connection) DriverManager.getConnection("jdbc:mysql://192.168.1.86:3306/tzh?user=root&password=root");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
			
}
