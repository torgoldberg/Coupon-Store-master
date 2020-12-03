package dao.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import Exceptions.CouponServerException;

/**
 * make single instance to DB (Access)
 * @author איתי מרצבך
 */

public class ConnectionPool {

	private static ConnectionPool connectionPool = new ConnectionPool();
	private static Connection[] connections = new Connection[5];
	private boolean use[] = new boolean[5];
	private ConnectionPool() {
	}

	/**
	 * 	 GET INSTANCE SINGELTON METHOD
	 * @return connectionPool
	 * @throws CouponServerException
	 */
	 
	public static ConnectionPool getConnectionPool() throws CouponServerException {
		if (connections[0] == null)
			setConnection();
		return connectionPool;
	}

	/**
	 * CONNECTS TO THE ACCESS DATA BASE AND ADDS CONNECTIONS
	 * @throws CouponServerException
	 */
	private static void setConnection() throws CouponServerException {

		try {
			System.out.println("Class");

			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream propertiesFile = classLoader.getResourceAsStream("application.properties");
			Properties properties = new Properties();
			String accessFileName = "c://CouponProject/CouponDB.accdb";

			try {
				
				properties.load(propertiesFile);
				propertiesFile.close();
				accessFileName = properties.getProperty("application.dbAccess");
			} catch (IOException e) {
				throw new CouponServerException("eror in connction", e);
			}
	
			System.out.println(accessFileName);
			String dbConnectionString = "jdbc:ucanaccess://" + accessFileName;

			for (int i = 0; i < connections.length; i++) {

				connections[i] = DriverManager.getConnection(dbConnectionString, "", "");
			}
		} catch (SQLException | ClassNotFoundException e) {

			throw new CouponServerException("eror in connction", e);
		}
	}

	/**
	 * GIVE CONNECTION IF ISNT USE AND WAIT WHEN ALL CONNECTIONS IS USE 
	 * @return connection
	 * @throws CouponServerException
	 */

	public synchronized Connection getConnection() throws CouponServerException {
		
		while(true){
			for (int i = 0; i < connections.length; i++) {
				if (!use[i]) {
					use[i] = true;
					return connections[i];
				}
			}
			try {
				this.wait();
			} catch (InterruptedException e) {
				throw new CouponServerException("problem in method 'wait'", e);
			}
		}
	}

	/**
	 * CREATING A METHOD THAT RETURNS CONNECTION TO A CONNECTION POOL
	 * @param connection
	 * @throws CouponServerException
	 */
	public synchronized void returnConnection(Connection connection) throws CouponServerException {
		for (int i = 0; i < connections.length; i++) {
			if (connections[i] == connection) {
				use[i] = false;
				try {
					this.notify();
				} catch (IllegalMonitorStateException a) {
					throw new CouponServerException("notify", a);
				}
			}
		}
	}

	/**
	 * CLOSING ALL THE CONNECTIONS METHOD SHUTTING DOWN THE SYSTEM
	 * @throws CouponServerException
	 */
	public void closeAllConnections() throws CouponServerException {

		try {
			for (Connection a : connections) {
				a.close();
			}
		} catch (SQLException e) {
			throw new CouponServerException("eror closing connctions", e);
		}
	}
}
