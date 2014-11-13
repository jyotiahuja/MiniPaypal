package org.miniPaypal.test.Service;
import java.sql.*;

import org.miniPaypal.test.User.User;

/*
 * Business logic class for loginservice
 */
public class LoginService {
	static Connection conn = null;  
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new AssertionError("Error loading JDBC driver" + e);
		}
	}
	
	public LoginService() {
		// create a database connection
	    try {
			conn = DriverManager.getConnection("jdbc:sqlite:sample1.db");
			
			conn.setAutoCommit(true);
			createTables();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AssertionError("Error getting DB connection"+e);
		}
		
	}
	
	private void createTables() throws SQLException {
		PreparedStatement smt = conn.prepareStatement(
				"create table if not exists users (userid varchar(255), username varchar(255), password varchar(255), balance int)");
		
		smt.execute();
	}
	
	public boolean authenticate(String loginId, String password) throws SQLException {
		//Make JDBC connection here and compare user id and password
		if ((password == null) || (password.trim()).isEmpty()) {
			return false;
		}
		
		PreparedStatement smt = conn.prepareStatement(
				"select password from users where userid=?");
		
		smt.setString(1, loginId);
		
		ResultSet r = smt.executeQuery();
		String dbPassword = null;
		
		if (r.next()) {
			dbPassword = r.getString(1); 	
			smt.close();
		}
		
		if (dbPassword != null) {
			return dbPassword.equals(password);
		}
		
		return false;
	}
	
	public void registerUser(User user) throws SQLException {
		PreparedStatement smt = conn.prepareStatement(
				"insert into users(userid, username, password, balance) values(?, ?, ?, ?)");
		
		smt.setString(1, user.getUserId());
		smt.setString(2, user.getUserName());
		smt.setString(3, user.getPassword());
		smt.setInt(4, user.getBalance());
		
		smt.executeUpdate();
		smt.close();
	}
	
	public User getUser(String loginId) throws SQLException {
		//todo close resources
		PreparedStatement smt = conn.prepareStatement(
				"select username, password, balance from users where userid=?");
		
		smt.setString(1, loginId);
		
		ResultSet r = smt.executeQuery();
		
		if (r.next()) {
			return new User(r.getString(1), loginId, r.getString(2), r.getInt(3));
		}
		return null;
	}
	
}
