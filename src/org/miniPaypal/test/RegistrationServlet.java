package org.miniPaypal.test;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.miniPaypal.test.Service.LoginService;
import org.miniPaypal.test.User.User;

@WebServlet("/userRegistration")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userId");
		String password = request.getParameter("password");
		String userName = request.getParameter("userName");
		
		User user = new User(userName, userid, password);
		
		LoginService service = new LoginService();
		
		try {
			service.registerUser(user);
		} catch (SQLException e) {
			throw new IOException("Error connecting db", e);
		}
		
		response.sendRedirect("login.jsp");
	}
}
