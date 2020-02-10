package cinema.status;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.UserDAO;
import cinema.model.User;

@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loggedInUsername = (String) request.getSession().getAttribute("loggedInUsername");
		if (loggedInUsername == null) {
			request.getRequestDispatcher("./UnauthenticatedServlet").forward(request, response);
			return;
		}
		try {
			
			User user = UserDAO.getUserByUsername(loggedInUsername);
			if (user == null) {
				request.getSession().invalidate();
				request.getRequestDispatcher("./UnauthenticatedServlet").forward(request, response);
				return;
			}
			
			UserDAO.logoutUser(user);
			request.getSession().invalidate();
			request.getRequestDispatcher("./UnauthenticatedServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().invalidate();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
