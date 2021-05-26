package com.watson.simplesql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class DBUtils {
	
	public static String getQueryResultAsString(SimpleConnection conn, String query, String separator) {
		ResultSet results = conn.getResultSet(query);
		ResultSetMetaData rsmd;
		try {
			rsmd = results.getMetaData();
			int count = rsmd.getColumnCount();
			StringBuilder builder = new StringBuilder();
			while (results.next()) {
				for (int x = 1; x <=count; x++) {
					builder.append(results.getString(x));
					if(x < count) {
						builder.append(separator);
					} else {
						builder.append("\n");
					}
				}
			}
			results.close();
			conn.close();
			String result = builder.toString();
			if(null == result) {
				return "";
			} else if (result.isEmpty()) {
				return "";
			}
			return result.substring(0, result.lastIndexOf("\n")).trim();
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String[] getQueryResultAsArray(SimpleConnection conn, String query, String separator) {
		return getQueryResultAsString(conn, query, separator).split("\n");
	}
	
	public static List<String> getQueryResultAsList(SimpleConnection conn, String query, String separator) {
		ResultSet results = conn.getResultSet(query);
		ResultSetMetaData rsmd;
		try {
			rsmd = results.getMetaData();
			int count = rsmd.getColumnCount();
			List<String> list = new ArrayList<String>();
			while (results.next()) {
				StringBuilder builder = new StringBuilder();
				for (int x = 1; x <=count; x++) {
					builder.append(results.getString(x));
					if(x < count) {
						builder.append(separator);
					}
				}
				list.add(builder.toString());
			}
			results.close();
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getQueryResultAsJSON(SimpleConnection conn, String query, String separator) {
		ResultSet results = conn.getResultSet(query);
		ResultSetMetaData rsmd;
		StringBuilder sb = new StringBuilder();
		try {
			rsmd = results.getMetaData();
			int count = rsmd.getColumnCount();
			String columns[] = new String[count];
			for (int y = 1; y <= count; y++) {
				String colName = rsmd.getColumnName(y);
				columns[y-1] = colName;
			}
			ArrayList<String> rows = new ArrayList<>();
			while (results.next()) {
				StringBuilder builder = new StringBuilder();
				for (int x = 1; x <=count; x++) {
					builder.append(results.getString(x));
					if(x < count) {
						builder.append(separator);
					} else {
						builder.append("\n");
					}
				}
				rows.add(builder.toString());
			}
			results.close();
			conn.close();
			sb.append("[").append("\n");
			for(int k = 0; k < rows.size(); k++) {
				String[] split = rows.get(k).split(separator);
				sb.append("	{\n");
				for(int x = 0; x < split.length; x++) {
					String str = split[x].trim();
					if(Util.tryParse(str) != null) {
						
					}
					sb.append("		\"").append(columns[x]).append("\": ");
					if(Util.tryParse(str) != null) {
						sb.append(Util.tryParse(split[x].trim()));
					} else if(str.equalsIgnoreCase("null")) {
						sb.append("null");
					} else {
						sb.append("\"").append(str).append("\"");
					}
					if((x + 1) == split.length) {
						sb.append("\n")
							.append("	}");
						if((k + 1) != rows.size()) {
							sb.append(",\n");
						} else {
							sb.append("\n");
						}
					} else {
						sb.append(",\n");
					}
				}
			}
			sb.append("]");
			String result = sb.toString();
			if(null == result) {
				return "";
			} else if (result.isEmpty()) {
				return "";
			}
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
    public static int getQueryResultAsInt(SimpleConnection conn, String query) {
    	if(!query.toUpperCase().contains("COUNT(*)")) {
	    	String column = query.toUpperCase().replace("SELECT ", "").substring(0, query.indexOf("FROM") - 8).trim();
	    	query += " ORDER BY " + column + " ASC LIMIT 1";
    	}
        String result = getQueryResultAsString(conn, query, "");
        if(result == null) {
            return 0;
        } else if (result.toUpperCase().contains("NULL")) {
            return 0;
        } else if (result.length() == 0) {
            return 0;
        }
        return Integer.parseInt(result.trim());
    }
    
    public static char getQueryResultAsChar(SimpleConnection conn, String query) {
        return getQueryResultAsString(conn, query, "").trim().charAt(0);
    }
    
    public static void writeStatementToDb(SimpleConnection conn, String statement) {
        conn.executeUpdate(statement);
        conn.close();
    }
    
    public static String writeStatementToDbWithResult(SimpleConnection conn, String statement) {
        String result = conn.executeUpdateWithResult(statement);
        conn.close();
        return result;
    }

}
