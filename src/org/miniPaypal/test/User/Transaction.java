package org.miniPaypal.test.User;

import java.sql.Date;

public class Transaction {
	private String userid;
	private String destUserid;
	private Date date;
	private int credit = 0;
	private int debit = 0;
	
	public Transaction(String userid, String destUserid, Date date, int debit, int credit) {
		this.userid = userid;
		this.destUserid = destUserid;
		this.date = date;
		this.credit = credit;
		this.debit = debit;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getDestuserid() {
		return destUserid;
	}

	public void setDestuserid(String userid) {
		this.destUserid = userid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getDebit() {
		return debit;
	}

	public void setDebit(int debit) {
		this.debit = debit;
	}
	
}
