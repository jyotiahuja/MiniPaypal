package org.miniPaypal.test.User;

public class User {
	private String userName;
	private String userId;
	private String password;
	private int balance;
	
	public User(String name, String id, String password) {
		this.userName = name;
		this.userId = id;
		this.password = password;
		this.balance = 100;
	}
	
	public User(String name, String id, String password, int balance) {
		this.userName = name;
		this.userId = id;
		this.password = password;
		this.balance = balance;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public void setBalance(int bal) {
		this.balance = bal;
	}
	
}
