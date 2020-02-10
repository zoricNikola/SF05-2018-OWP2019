package cinema;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.UserDAO;
import cinema.model.User;
import cinema.model.User.UserRole;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String loggedInUsername = (String) request.getSession().getAttribute("loggedInUsername");
			if (loggedInUsername == null) {
				request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
				return;
			}
			User loggedInUser = UserDAO.getUserByUsername(loggedInUsername);
			if (loggedInUser == null) {
				request.getRequestDispatcher("./LogoutServlet").forward(request, response);
				return;
			}
			
			if (!loggedInUser.isLoggedIn()) {
				request.getRequestDispatcher("./LogoutServlet").forward(request, response);
				return;
			}
			
			if (loggedInUser.getUserRole() != UserRole.ADMIN && !loggedInUser.getUsername().equals(request.getParameter("username"))) {
				request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
				return;
			}
			
			String username = request.getParameter("username");
			User user = UserDAO.getUserByUsername(username);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("user", user);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String loggedInUsername = (String) request.getSession().getAttribute("loggedInUsername");
			if (loggedInUsername == null) {
				request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
				return;
			}
			User loggedInUser = UserDAO.getUserByUsername(loggedInUsername);
			if (loggedInUser == null) {
				request.getRequestDispatcher("./LogoutServlet").forward(request, response);
				return;
			}
			
			if (!loggedInUser.isLoggedIn()) {
				request.getRequestDispatcher("./LogoutServlet").forward(request, response);
				return;
			}
			
			
			String action = request.getParameter("action");
			
			switch (action) {
				case "update": {
					if (loggedInUser.getUserRole() != UserRole.ADMIN && !loggedInUser.getUsername().equals(request.getParameter("username"))) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					User user = UserDAO.getUserByUsername(request.getParameter("username"));
					String realPassword = UserDAO.getPasswordByUsername(request.getParameter("username"));
					String newPassword = request.getParameter("newPassword");
					if (!realPassword.equals(newPassword))
						user.setPassword(newPassword);
					else
						user.setPassword(realPassword);
					
					User.UserRole newUserRole = User.UserRole.valueOf(request.getParameter("newUserRole"));
					if (user.getUserRole() != newUserRole)
						user.setUserRole(newUserRole);
					
					if (user != null) {
						if (!UserDAO.updateUser(user))
							throw new Exception("Greška prilikom izmene korisnika u bazi");
						else
							UserDAO.logoutUser(user);
					}
					break;
				}
				case "delete": {
					if (loggedInUser.getUserRole() != UserRole.ADMIN) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					User user = UserDAO.getUserByUsername(request.getParameter("username"));
					
					if (user != null) {
						if (!UserDAO.deleteUser(user))
							throw new Exception("Greška prilikom brisanja korisnika iz baze");
						else
							UserDAO.logoutUser(user);
					}
					break;
				}
			}
			
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

}
