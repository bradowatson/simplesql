package com.watson.simplesql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SimpleConnection {
	
	private Connection connection;
	
	public SimpleConnection() {
		
	}
	
	public SimpleConnection(String driver, String url, String name, String password) {
		try {
//			Class.forName(driver).newInstance();
			this.connection = DriverManager.getConnection(url, name, password);
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SimpleConnection(String driver, String url) {
		try {
//			Class.forName(driver).newInstance();
			this.connection = DriverManager.getConnection(url);
//		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SimpleConnection(String context) {
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(context);
			this.connection = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public Connection getConnection() {
		return connection;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public ResultSet getResultSet(String query) {
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet result = stmt.executeQuery(query);
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public void executeUpdate(String sql) {
            Statement statement;
			try {
				statement = this.connection.createStatement();
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    public String executeUpdateWithResult(String sql) {
        Statement statement;
		try {
			statement = this.connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FALSE";
		}
		return "SUCCESS";
}
	
	public void close() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
