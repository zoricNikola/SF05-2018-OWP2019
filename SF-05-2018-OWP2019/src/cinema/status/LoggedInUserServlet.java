package cinema.status;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.UserDAO;
import cinema.model.User;
import cinema.model.User.UserRole;
import cinema.searchModels.UsersSearchModel;

@SuppressWarnings("serial")
public class LoggedInUserServlet extends HttpServlet {

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
			
			if (!user.isLoggedIn()) {
				request.getSession().invalidate();
				request.getRequestDispatcher("./UnauthenticatedServlet").forward(request, response);
				return;
			}
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			Map<String, Object> loggedInUser = new LinkedHashMap<String, Object>();
			
			loggedInUser.put("username", user.getUsername());
			loggedInUser.put("userRole", user.getUserRole().toString());
			
			data.put("loggedInUser", loggedInUser);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
