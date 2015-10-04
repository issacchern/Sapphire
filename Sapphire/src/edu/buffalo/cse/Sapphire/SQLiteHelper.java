/**

 * This file is part of Sapphire.
 * 
 * Sapphire is a free plugin software, licensed under the terms of the 
 * Eclipse Public License, version 1.0.  The license is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Sapphire © 2015 University at Buffalo. All rights reserved.  
 */

package edu.buffalo.cse.Sapphire;

import java.sql.*;

/**
 * This class handles all the SQLite database query data by the types
 * of event.
 * 
 * @author Chern Yee Chua
 */

public class SQLiteHelper {

	
	public void sqlMain(String location, String event, String description)
			throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		String str, dbLocation;
		Connection connection = null;
		try {
			dbLocation = "jdbc:sqlite:" + location + "/.sqlite.db";
			connection = DriverManager.getConnection(dbLocation);
			Statement statement = connection.createStatement();
			statement.executeUpdate("create table if not exists main_event ("
					+ "id INTEGER primary key autoincrement, "
					+ "time_stamp TEXT, "
					+ "event TEXT, "
					+ "description TEXT)");

			str = "insert into main_event (time_stamp, event,description) values( datetime('now','localtime'), '" + event
					+ "' , '" + description + "')";

			statement.executeUpdate(str);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}

	public void sqlError(String location, String className, String error_message)
			throws ClassNotFoundException {
		
		Class.forName("org.sqlite.JDBC");
		String str, dbLocation;
		Connection connection = null;
		try {
			dbLocation = "jdbc:sqlite:" + location + "/.sqlite.db";
			connection = DriverManager.getConnection(dbLocation);
			Statement statement = connection.createStatement();

			statement.executeUpdate("create table if not exists error ("
					+ "error_id INTEGER primary key autoincrement, "
					+ "time_stamp TEXT, "
					+ "class_name TEXT, "
					+ "error_message TEXT)");

			str = "insert into error (time_stamp,class_name,error_message) values(datetime('now','localtime'), '" + className 
					+ "' , '" + error_message + "')";

			statement.executeUpdate(str);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}

	}

	public void sqlSource(String location, String className, String source_file)
			throws ClassNotFoundException {
		
		Class.forName("org.sqlite.JDBC");
		String str, dbLocation;
		Connection connection = null;
		try {

			dbLocation = "jdbc:sqlite:" + location + "/.sqlite.db";
			connection = DriverManager.getConnection(dbLocation);
			Statement statement = connection.createStatement();

			statement.executeUpdate("create table if not exists source_file ("
					+ "source_id INTEGER primary key autoincrement, "
					+ "time_stamp TEXT, "
					+ "class_name TEXT, "
					+ "source TEXT)");

			str = "insert into source_file (time_stamp,class_name,source) values( datetime('now','localtime'), '" + className 
					+ "' , '" + source_file + "')";

			statement.executeUpdate(str);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}

	}

	public void sqlEdit(String location, String className, String line_diff, String description)
			throws ClassNotFoundException {
		
		Class.forName("org.sqlite.JDBC");
		String str, dbLocation;
		Connection connection = null;
		try {

			dbLocation = "jdbc:sqlite:" + location + "/.sqlite.db";
			connection = DriverManager.getConnection(dbLocation);
			Statement statement = connection.createStatement();

			statement.executeUpdate("create table if not exists compilation_unit ("
					+ "cu_id INTEGER primary key autoincrement, "
					+ "time_stamp TEXT, "
					+ "class_name TEXT, "
					+ "line_diff TEXT, "
					+ "description TEXT)");

			str = "insert into compilation_unit (time_stamp, class_name,line_diff,description) values( datetime('now','localtime'), '" + className +
					"' , '" + line_diff + "' , '" + description + "')";

			statement.executeUpdate(str);

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}

	}


}
