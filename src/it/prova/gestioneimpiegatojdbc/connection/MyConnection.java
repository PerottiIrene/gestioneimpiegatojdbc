package it.prova.gestioneimpiegatojdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyConnection {

	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECT_STR = "jdbc:mysql://localhost:3306/gestioneimpiegato?user=root&password=root&allowPublicKeyRetrieval=true&useSSL=FALSE&serverTimezone=UTC";

	// per oracle XE (occhio che nelle insert servirebbero le sequence)
	// private static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	// private static final String CONNECT_STR =
	// "jdbc:oracle:thin:@localhost:1521:XE";

	public static Connection getConnection() {

		Connection connection = null;
		try {
			//questa riga di codice post java 8 risulta opzionale!!!
			Class.forName(DRIVER_NAME);
			
			connection = DriverManager.getConnection(CONNECT_STR);
			// per oracle
			// connection =
			// DriverManager.getConnection(CONNECT_STR,"vostrausername","vostrapassword");

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return connection;
	}
}
