package org.miniPaypal.test.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.miniPaypal.test.User.*;
/*
 * Transaction Service contains business logic for transaction action
 */

public class TransactionService {
	
	//transaction table here, use DB connection created in Login service
	public TransactionService() {
	    try {
			createTables();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AssertionError("Error getting DB connection"+e);
		}
	}
	
	private void createTables() throws SQLException {
		PreparedStatement smt = LoginService.conn.prepareStatement(
				"create table if not exists transaction2 (userid varchar(255), destUserid varchar(255), date datetime,debit int, credit int)");
		
		smt.execute();
	}
	
	/*
	 * public function to return list of transactions, reads the table for curr user id
	 * Need it to show on loginSuccess page
	 * TODO - return last few transactions by time instead of all
	 */
	
	public List<Transaction> getTransactions(String userid) throws SQLException{
		PreparedStatement smt = LoginService.conn.prepareStatement(
					"select destUserid,date,debit,credit from transaction2 where userid=?");
		smt.setString(1, userid);
		
		ResultSet r = smt.executeQuery();
		List<Transaction> transactions = new ArrayList<Transaction>();
		while (r.next()) {
			Transaction t = new Transaction(userid, r.getString(1), r.getDate(2), r.getInt(3), r.getInt(4));
			transactions.add(t);
		}
		return transactions;
	}
	
	/*
	 * Perform the transaction function, updates the user records in user table and add transaction log 
	 * in transaction table
	 * TODO - refactor, too long
	 */
	public User doTransaction(User currUserInfo, String destUser, int amount ) throws SQLException {
		if (currUserInfo.getBalance() >= amount) {
			//conn.setAutoCommit(false);
			
			try {
				//gets the current user's balance
				PreparedStatement smt1 = LoginService.conn.prepareStatement(
						"select balance from users where userid=?");
				
				smt1.setString(1, currUserInfo.getUserId());
				ResultSet r = smt1.executeQuery();
				int currUserBal = 0;
				if (r.next()) {
					currUserBal = r.getInt(1);
				}
				smt1.close();
				
				//checks existence of destination user and reads its balance if present
				PreparedStatement smt2 = LoginService.conn.prepareStatement(
						"select balance from users where userid=?");
				smt2.setString(1, destUser);
				ResultSet r1 = smt2.executeQuery();
				
				if ((r1.next()) && (currUserBal >= amount )) {
					int existingBal = r1.getInt(1);
					smt2.close();
					//decrease current user's balance by amount
					int newBalance = currUserBal - amount;
					PreparedStatement smt3 = LoginService.conn.prepareStatement(
							"update users set balance = ? where userid=?");
					smt3.setInt(1,newBalance);
					smt3.setString(2, currUserInfo.getUserId());
					smt3.executeUpdate();
					
					currUserInfo.setBalance(newBalance);
					
					//increase destination user's balance by amount
					PreparedStatement smt4 = LoginService.conn.prepareStatement(
							"update users set balance = ? where userid=?");
					int destUserBalance = amount + existingBal;
					smt4.setInt(1,destUserBalance);
					smt4.setString(2, destUser);
					smt4.executeUpdate();
					
					//update the transactions table now, for destination user
					PreparedStatement smt5 = LoginService.conn.prepareStatement(
							"insert into transaction2(userid, destUserid, date, debit, credit) values(?, ?, ?, ?, ?)");
					smt5.setString(1, currUserInfo.getUserId());
					smt5.setString(2, destUser);
					//insert current time
					Date time = new Date(System.currentTimeMillis());
					smt5.setDate(3, time);
					smt5.setInt(4, amount);
					
					smt5.executeUpdate();
					smt5.close();
					
					//update the transactions table now, for current user
					PreparedStatement smt6 = LoginService.conn.prepareStatement(
							"insert into transaction2(userid, destUserid, date, debit, credit) values(?, ?, ?, ?, ?)");
					smt6.setString(1, destUser);
					smt6.setString(2, currUserInfo.getUserId());
					//insert current time
					smt6.setDate(3, time );
					smt6.setInt(5, amount);
					
					smt6.executeUpdate();
					smt6.close();
				
					return currUserInfo;
				}
			}
			finally {
				//conn.rollback();
				//conn.setAutoCommit(true);
			}
		}
		return null;
	}
}
