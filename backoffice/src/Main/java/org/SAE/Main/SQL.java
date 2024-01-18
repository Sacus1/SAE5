package org.SAE.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Arrays;
/**
 * This class provides methods to interact with a SQL database.
 */
public class SQL {
	public Connection con;

	/**
	 * Constructor for the SQL class.
	 * It initializes the connection to the database.
	 *
	 * @param url      the URL of the database
	 * @param user     the username for the database
	 * @param password the password for the database
	 */
	public SQL(String url, String user, String password) {
		try {
			// chargement de la classe par son nom lié au driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// levée d’exception si le driver n’est pas trouvé
			System.err.println("Class not found : " + e.getMessage());
			// close the application
			System.exit(1);
		}
		try {
			// on se connecte à la base de données
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
			// try to connect again after 5 seconds and close the application if it fails
			new Thread(() -> {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
				try {
					con = DriverManager.getConnection(url, user, password);
				} catch (SQLException sqlException) {
					System.err.println("Main.SQL Exception : " + sqlException.getMessage());
					System.exit(1);
				}
			}).start();
		}
	}

	/**
	 * This method updates a table in the database using a prepared statement.
	 *
	 * @param table     the name of the table to update
	 * @param colName   an array of column names to update
	 * @param attr      an array of new values for the columns
	 * @param whereCond an array of conditions for the WHERE clause
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updatePreparedStatement(String table, String[] colName, Object[] attr, String[] whereCond) {
		StringBuilder query = new StringBuilder("UPDATE " + table + " SET ");
		for (int i = 0; i < colName.length; i++) {
			query.append(colName[i]).append(" = ?");
			if (i != colName.length - 1) query.append(", ");
		}
		query.append(" WHERE ");
		for (int i = 0; i < whereCond.length; i++) {
			query.append(whereCond[i]);
			if (i != whereCond.length - 1) query.append(" AND ");
		}
		query.append(";");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			for (int i = 0; i < attr.length; i++) {
				switch (attr[i]) {
					case String s -> stmt.setString(i + 1, s);
					case Integer integer -> stmt.setInt(i + 1, integer);
					case Double v -> stmt.setDouble(i + 1, v);
					case File file -> {
						InputStream inputStream = new FileInputStream(file);
						stmt.setBinaryStream(i + 1, inputStream, (int) file.length());
						if (file.delete()) System.out.println("File deleted successfully");
						else System.out.println("Failed to delete the file");
					}
					case Date date -> stmt.setDate(i + 1, date);
					case Boolean bool -> stmt.setBoolean(i + 1, bool);
					case null, default -> stmt.setNull(i + 1, Types.BLOB);
				}
			}
			return stmt.executeUpdate() != 0;
		} catch (SQLException | FileNotFoundException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage() + "\n" + query + "\n" + Arrays.toString(attr));
		}
		return false;
	}

	/**
	 * This method inserts a new row into a table in the database using a prepared statement.
	 *
	 * @param table   the name of the table to insert into
	 * @param colName an array of column names for the new row
	 * @param attr    an array of values for the new row
	 * @return true if the insertion was successful, false otherwise
	 */
	public boolean createPrepareStatement(String table, String[] colName, Object[] attr) {
		StringBuilder query = buildPartialQuery(table, colName, "INSERT INTO ", " (", ", ", ") VALUES (");
		for (int i = 0; i < colName.length; i++) {
			query.append("?");
			if (i != colName.length - 1) query.append(", ");
		}
		query.append(");");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			for (int i = 0; i < attr.length; i++) {
				switch (attr[i]) {
					case String s -> stmt.setString(i + 1, s);
					case Integer integer -> stmt.setInt(i + 1, integer);
					case Double v -> stmt.setDouble(i + 1, v);
					case File file -> {
						if (file.exists()) {
							InputStream inputStream = new FileInputStream(file);
							stmt.setBinaryStream(i + 1, inputStream, (int) file.length());
						} else stmt.setNull(i + 1, Types.BLOB);
					}
					case null -> stmt.setNull(i + 1, Types.BLOB);
					case Float aFloat -> stmt.setFloat(i + 1, aFloat);
					case Date date -> stmt.setDate(i + 1, date);
					case Boolean bool -> stmt.setBoolean(i + 1, bool);
					default -> {
						System.err.println("Main.SQL Exception : " + attr[i].getClass() + " is not supported");
						stmt.setNull(i + 1, Types.BLOB);
					}
				}
			}
			int executeUpdate = stmt.executeUpdate();
			return executeUpdate != 0;
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage() + "\n" + query + "\n" + Arrays.toString(attr));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	private StringBuilder buildPartialQuery(String tableName, String[] columnNames, String initialQuery,
	                                        String preColumns, String betweenColumns, String postColumns){
		StringBuilder query = new StringBuilder(initialQuery + tableName + preColumns);
		for (int i = 0; i < columnNames.length; i++) {
			query.append(columnNames[i]);
			if (i != columnNames.length - 1) query.append(betweenColumns);
		}
		query.append(postColumns);
		return query;
	}

	/**
	 * This method deletes rows from a table in the database using a prepared statement.
	 *
	 * @param table     the name of the table to delete from
	 * @param whereCond an array of conditions for the WHERE clause
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deletePrepareStatement(String table, String[] whereCond) {
		StringBuilder query = buildPartialQuery(table, whereCond, "DELETE FROM ", " WHERE ", " AND ", ";");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			int r = stmt.executeUpdate();
			return r != 0;
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage() + "\n" + query);
		}
		return false;
	}

	/**
	 * This method selects rows from a table in the database.
	 *
	 * @param table     the name of the table to select from
	 * @param whereCond an array of conditions for the WHERE clause
	 * @return a ResultSet object containing the selected rows
	 */
	public ResultSet select(String table, String[] whereCond) {
		StringBuilder query = buildPartialQuery(table, whereCond, "SELECT * FROM ", " WHERE ", " AND ", ";");
		try {
			return con.prepareStatement(query.toString()).executeQuery();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage() + "\n" + query);
		}
		return null;
	}

	/**
	 * This method executes a raw SQL query.
	 *
	 * @param rawQuery the raw SQL query to execute
	 * @return a ResultSet object containing the results of the query
	 */
	public ResultSet selectRaw(String rawQuery) {
		try {
			return con.prepareStatement(rawQuery).executeQuery();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage() + "\n" + rawQuery);
		}
		return null;
	}

	/**
	 * This method selects all rows from a table in the database.
	 *
	 * @param table the name of the table to select from
	 * @return a ResultSet object containing the selected rows
	 */
	public ResultSet select(String table) {
		return select(table, new String[]{"1 = 1"});
	}
	/**
	 * This method closes the connection to the database.
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
	}
	public int getNextId(String tableName) {
		try {
			ResultSet res = selectRaw("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'SAE' AND TABLE_NAME = '" + tableName + "';");
			if (res.next()) return res.getInt("AUTO_INCREMENT");
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
		return 0;
	}
}
