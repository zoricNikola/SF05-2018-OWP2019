package cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.HallDAO;
import cinema.dao.MovieDAO;
import cinema.dao.ProjectionDAO;
import cinema.dao.ProjectionTypeDAO;
import cinema.dao.UserDAO;
import cinema.model.Hall;
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.ProjectionType;
import cinema.model.User;
import cinema.model.User.UserRole;
import cinema.util.DateTimeUtil;

@SuppressWarnings("serial")
public class ProjectionServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String stringID = request.getParameter("id");
			int id = Integer.parseInt(stringID);
			Projection projection = ProjectionDAO.getProjectionByID(id);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("projection", projection);
			
			if (projection.getTime().isBefore(LocalDateTime.now()))
				data.put("inPast", true);
			else
				data.put("inPast", false);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
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
			
			if (loggedInUser.getUserRole() != UserRole.ADMIN) {
				request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
				return;
			}
			
			String action = request.getParameter("action");
			
			switch (action) {
				case "add": {
					
					int movieID = Integer.parseInt(request.getParameter("movieID"));
					Movie movie = MovieDAO.getMovieByID(movieID);
					LocalDateTime time = DateTimeUtil.StringToLocalDateTime(request.getParameter("time"));
					double price = Double.parseDouble(request.getParameter("price"));
					int projectionTypeID = Integer.parseInt(request.getParameter("projectionTypeID"));
					ProjectionType projectionType = ProjectionTypeDAO.getByID(projectionTypeID);
					int hallID = Integer.parseInt(request.getParameter("hallID"));
					Hall hall = HallDAO.getHallByID(hallID);
					User admin = loggedInUser;
					
					if (admin.getUserRole() == UserRole.ADMIN) {
						Projection projection = new Projection(0, movie, projectionType, hall, time, price, admin, true);
						
						if (projection.getTime().isAfter(LocalDateTime.now()) && !Projection.timeOverlaps(projection)) {
							if (!ProjectionDAO.addProjection(projection))
								throw new Exception("Greška prilikom dodavanja projekcije u bazu");
						}
						else {
							Map<String, Object> data = new LinkedHashMap<String, Object>();
							
							if (!projection.getTime().isAfter(LocalDateTime.now()))
								data.put("message", "timeInPast");
							if (Projection.timeOverlaps(projection))
								data.put("message", "timeOverlaps");
							
							request.setAttribute("data", data);
							
							throw new Exception("Nije moguće dodati projekciju u ovo vreme!");
						}
					}
					
					break;
				}
				case "delete": {
					Projection projection = ProjectionDAO.getProjectionByID(Integer.parseInt(request.getParameter("id")));
					if (projection != null) {
						if (!ProjectionDAO.deleteProjection(projection))
							throw new Exception("Greška prilikom brisanja projekcije iz baze");
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
