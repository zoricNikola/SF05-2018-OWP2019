package cinema;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.UserDAO;
import cinema.model.User;

@SuppressWarnings("serial")
public class RegistrationServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			if (username == null || username.equals(""))
				throw new Exception("Morate uneti korisničko ime");
			if (password == null || password.equals(""))
				throw new Exception("Morate uneti lozinku");
			
			
			if (UserDAO.getUserByUsername(username) != null) {
				throw new Exception("Korisnicko ime je zauzeto!");
			}
			User user = new User(username, password, LocalDate.now(), User.UserRole.USER, true, false);
			
			if (UserDAO.addUser(user)) {
				request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			} else throw new Exception("Greska prilikom dodavanja korisnika u bazu");
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
