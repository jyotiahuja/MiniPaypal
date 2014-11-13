package org.miniPaypal.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.miniPaypal.test.Service.LoginService;
import org.miniPaypal.test.Service.TransactionService;
import org.miniPaypal.test.User.Transaction;
import org.miniPaypal.test.User.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/userLogin")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userId");
		String password = request.getParameter("password");
		
		LoginService service = new LoginService();
		TransactionService ts = new TransactionService();
		
		try {
			if (service.authenticate(userid, password)) {
				//LoginSuccess needs current user info and list of transactions so set those in attr
				User userInfo = service.getUser(userid);
				request.setAttribute("user", userInfo);
				
				List<Transaction> tnList = ts.getTransactions(userid);
				request.setAttribute("transactions", tnList);
				
				RequestDispatcher rd = request.getRequestDispatcher("loginSuccess.jsp");
				rd.forward(request, response);
				return;
			}
			else {
				response.sendRedirect("login.jsp");
				return;
			}
		} catch (SQLException e) {
			throw new IOException("Error connecting db", e);
		}
	}
}
