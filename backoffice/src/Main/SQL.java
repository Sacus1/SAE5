package Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class SQL {
	public Connection con;

	public SQL(String url, String user, String password) {
		try {
			// chargement de la classe par son nom lié au driver
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// levée d'exception si le driver n'est pas trouvé
			System.err.println("Class not found : " + e.getMessage());
		}
		try {
			// on se connecte à la base de données
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
	}

	public boolean updatePreparedStatement(String table, String[] colName, Object[] attr, String[] whereCond) {
		StringBuilder query = new StringBuilder("UPDATE " + table + " SET ");
		for (int i = 0; i < colName.length; i++) {
			query.append(colName[i]).append(" = ?");
			if (i != colName.length - 1) query.append(", ");
		}
		query.append(" WHERE ");
		for (int i = 0; i < whereCond.length; i++) {
			query.append(whereCond[i]).append(" = ?");
			if (i != whereCond.length - 1) query.append(" AND ");
		}
		query.append(";");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			for (int i = 0; i < attr.length; i++)
				if (attr[i] instanceof String) stmt.setString(i + 1, (String) attr[i]);
				else if (attr[i] instanceof Integer) stmt.setInt(i + 1, (Integer) attr[i]);
				else if (attr[i] instanceof Double) stmt.setDouble(i + 1, (Double) attr[i]);
				else if (attr[i] instanceof File file) {
					InputStream inputStream = new FileInputStream(file);
					stmt.setBinaryStream(i + 1, inputStream, (int) file.length());
					if (file.delete()) System.out.println("File deleted successfully");
					else System.out.println("Failed to delete the file");
				}
			return stmt.executeUpdate() != 0;
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public boolean createPrepareStatement(String table, String[] colName, Object[] attr) {
		StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");
		for (int i = 0; i < colName.length; i++) {
			query.append(colName[i]);
			if (i != colName.length - 1) query.append(", ");
		}
		query.append(") VALUES (");
		for (int i = 0; i < colName.length; i++) {
			query.append("?");
			if (i != colName.length - 1) query.append(", ");
		}
		query.append(");");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			for (int i = 0; i < attr.length; i++)
				if (attr[i] instanceof String) stmt.setString(i + 1, (String) attr[i]);
				else if (attr[i] instanceof Integer) stmt.setInt(i + 1, (Integer) attr[i]);
				else if (attr[i] instanceof Double) stmt.setDouble(i + 1, (Double) attr[i]);
				else if (attr[i] instanceof File file) {
					InputStream inputStream = new FileInputStream(file);
					stmt.setBinaryStream(i + 1, inputStream, (int) file.length());
					if (file.delete()) System.out.println("File deleted successfully");
					else System.out.println("Failed to delete the file");
				}
			return stmt.executeUpdate() != 0;
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public boolean deletePrepareStatement(String table, String[] whereCond) {
		StringBuilder query = new StringBuilder("DELETE FROM " + table + " WHERE ");
		for (int i = 0; i < whereCond.length; i++) {
			query.append(whereCond[i]);
			if (i != whereCond.length - 1) query.append(" AND ");
		}
		query.append(";");
		try {
			PreparedStatement stmt = con.prepareStatement(query.toString());
			return stmt.executeUpdate() != 0;
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
		return false;
	}

	public ResultSet select(String table, String[] whereCond) {
		StringBuilder query = new StringBuilder("SELECT * FROM " + table + " WHERE ");
		for (int i = 0; i < whereCond.length; i++) {
			query.append(whereCond[i]);
			if (i != whereCond.length - 1) query.append(" AND ");
		}
		query.append(";");
		try {
			return con.prepareStatement(query.toString()).executeQuery();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
		return null;
	}

	public ResultSet selectRaw(String rawQuery) {
		try {
			return con.prepareStatement(rawQuery).executeQuery();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
		return null;
	}

	public ResultSet select(String table) {
		return select(table, new String[]{"1 = 1"});
	}

	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.err.println("Main.SQL Exception : " + e.getMessage());
		}
	}
}
