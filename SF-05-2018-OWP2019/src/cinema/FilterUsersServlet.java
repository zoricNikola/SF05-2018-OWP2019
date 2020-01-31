package cinema;

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
public class FilterUsersServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loggedInUsername = (String) request.getSession().getAttribute("loggedInUsername");
		if (loggedInUsername == null) {
			request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
			return;
		}
		try {
			
			User loggedInUser = UserDAO.getUserByUsername(loggedInUsername);
			if (loggedInUser == null) {
				request.getRequestDispatcher("./LogoutServlet").forward(request, response);
				return;
			}
			else if (loggedInUser.getUserRole() != UserRole.ADMIN) {
				request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
				return;
			}
			
			String username = request.getParameter("username");
			String userRole = request.getParameter("userRole");
			
			UsersSearchModel model = new UsersSearchModel();
			model.username = username;
			model.userRole = userRole;
			
			List<User> filteredUsers = UserDAO.searchUsers(model);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("filteredUsers", filteredUsers);
			
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
