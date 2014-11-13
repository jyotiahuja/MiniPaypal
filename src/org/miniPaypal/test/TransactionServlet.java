package org.miniPaypal.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.miniPaypal.test.Service.TransactionService;
import org.miniPaypal.test.User.Transaction;
import org.miniPaypal.test.User.User;

/**
 * Servlet implementation class TransactionServlet
 * Takes care of transactions initiated from loginSuccess page
 */
@WebServlet("/secure/userTransaction")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//gather user inputs first
		String userid = request.getParameter("destUserId");
		int amount = Integer.parseInt(request.getParameter("amount"));
		User currUserInfo = (User) request.getSession().getAttribute("user");
	
		
		TransactionService ts = new TransactionService();
		//New user object is needed since balance could be updated for current user post transaction
		User currUserUpdated = null;
		try {
			currUserUpdated = ts.doTransaction(currUserInfo, userid, amount);
		} catch (SQLException e) {
			throw new IOException("Error connecting db", e);
		}
		if (currUserUpdated != null) {
			//set transactions in attribute
			List<Transaction> transactionList = new ArrayList<Transaction>();
			try {
				transactionList = ts.getTransactions(currUserInfo.getUserId());
			} catch (SQLException e) {
				throw new IOException("Error connecting db", e);
			}
			
			//Need transactionsList and current user info in the jsp so set those as attributes
			request.setAttribute("transactions", transactionList);
			request.setAttribute("user", currUserUpdated);
			//System.out.println("firstURL "+ (String) request.getSession().getAttribute("firstURL"));
			RequestDispatcher rd = request.getRequestDispatcher("/loginSuccess.jsp");
			rd.forward(request, response);
			
			return;
		}
		else {
			throw new IOException("Invalid request - Make sure userid is valid and there are sufficuent funds to make a transfer");
		}
	}
}
