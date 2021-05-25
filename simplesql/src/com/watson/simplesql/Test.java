package com.watson.simplesql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Test {
	private final static String PROPS_FILE = "e:/dev/database.properties";
	
	public static void main(String[] args) {
		Properties props = getProps();
		String driver = props.getProperty("driver");
		String url = props.getProperty("url");
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		SimpleConnection conn = new SimpleConnection(driver, url, user, password);
		StringBuilder stmt = new StringBuilder();
        stmt.append("SELECT *\n")
            .append("FROM fantasybaseball.teams");
		String result = DBUtils.getQueryResultAsJSON(conn, stmt.toString(), ";");
		System.out.println(result);
	}
	
    private static Properties getProps() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPS_FILE));
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return properties;
    }

}
