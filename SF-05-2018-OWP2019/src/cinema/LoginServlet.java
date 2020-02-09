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

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			User user = UserDAO.getUserByUsername(username);
			
			if (user == null) {
				throw new Exception("username");
			}
			if (!user.getPassword().equals(password)) {
				throw new Exception("password");
			}
			if (!user.isActive())
				throw new Exception("deactivated");
			
			request.getSession().setAttribute("loggedInUsername", user.getUsername());
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Nepredvidjena greska!";
				e.printStackTrace();
			}
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("message", message);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
			
		}
	}

}
