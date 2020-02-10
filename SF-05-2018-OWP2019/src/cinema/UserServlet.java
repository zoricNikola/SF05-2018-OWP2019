package cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.ProjectionDAO;
import cinema.dao.SeatDAO;
import cinema.dao.TicketDAO;
import cinema.dao.UserDAO;
import cinema.model.Projection;
import cinema.model.Seat;
import cinema.model.Ticket;
import cinema.model.User;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
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
			String action = request.getParameter("action");
			
			switch (action) {
				case "update": {
					User user = UserDAO.getUserByUsername(request.getParameter("username"));
					String newPassword = request.getParameter("newPassword");
					if (!user.getPassword().equals(newPassword))
						user.setPassword(newPassword);
					
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
