package cinema.dao;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class ConnectionManager {
	
	private static final String DATABASE_NAME = "cinema.db";
	
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String WINDOWS_PATH = "D:" + FILE_SEPARATOR + "SQL files" + FILE_SEPARATOR
											+ "Cinema" + FILE_SEPARATOR;
	private static final String LINUX_PATH = "";
	
	private static final String PATH = WINDOWS_PATH + DATABASE_NAME;
	
	private static DataSource dataSource;
	
	public static void open() {
		try {
			Properties dataSourceProperties = new Properties();
			dataSourceProperties.setProperty("driverClassName", "org.sqlite.JDBC");
			dataSourceProperties.setProperty("url", "jdbc:sqlite:" + PATH);
			
			dataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
