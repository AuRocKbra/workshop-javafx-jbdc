package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	private static Connection conexao =null;
	
	public static Properties loadProperties() {
		try(FileInputStream file = new FileInputStream("db.properties")){
			Properties properties = new Properties();
			properties.load(file);
			return properties;
		}catch(IOException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static Connection getConnection() {
		if(conexao==null) {
			Properties properties = loadProperties();
			String url = properties.getProperty("dburl");
			try {
				conexao = DriverManager.getConnection(url, properties);
			}catch(SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
		return conexao;
	}
	
	public static void closeConection() {
		try {
			conexao.close();
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		try {
			st.close();
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		try {
			rs.close();
		}catch(SQLException e) {
			throw new DBException(e.getMessage());
		}
	}
}
