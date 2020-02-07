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
			String action = request.getParameter("action");
			
			String title = request.getParameter("title");
			String director = request.getParameter("director");
			List<String> genres = new ArrayList<String>();
			if (request.getParameterValues("genres[]") != null)
				genres = new ArrayList<String>(Arrays.asList(request.getParameterValues("genres[]")));
			List<String> actors = new ArrayList<String>();
			if (request.getParameterValues("actors[]") != null)
				actors = new ArrayList<String>(Arrays.asList(request.getParameterValues("actors[]")));
			int duration = 0;
			try {
				String stringDuration = request.getParameter("duration");
				duration = Integer.parseInt(stringDuration);
			} catch (Exception e) {}
			int year = 0;
			try {
				String stringYear = request.getParameter("year");
				year = Integer.parseInt(stringYear);
			} catch (Exception e) {}
			String country = request.getParameter("country");
			String distributor = request.getParameter("distributor");
			String description = request.getParameter("description");
			
			
			switch (action) {
				case "add": {
					
					int movieID = Integer.parseInt(request.getParameter("movieID"));
					Movie movie = MovieDAO.getMovieByID(movieID);
					LocalDateTime time = DateTimeUtil.StringToLocalDateTime(request.getParameter("time"));
					double priceHigh = 0;
					double price = Double.parseDouble(request.getParameter("price"));
					int projectionTypeID = Integer.parseInt(request.getParameter("projectionTypeID"));
					ProjectionType projectionType = ProjectionTypeDAO.getByID(projectionTypeID);
					int hallID = Integer.parseInt(request.getParameter("hallID"));
					Hall hall = HallDAO.getHallByID(hallID);
					User admin = UserDAO.getUserByUsername((String) request.getSession().getAttribute("loggedInUsername"));
					
					if (admin.getUserRole() == UserRole.ADMIN) {
						Projection projection = new Projection(0, movie, projectionType, hall, time, price, admin, true);
						
						if (projection.getTime().isAfter(LocalDateTime.now()) && !Projection.timeOverlaps(projection)) {
							if (!ProjectionDAO.addProjection(projection))
								throw new Exception("Greška prilikom dodavanja projekcije u bazu");
						}
						else {
							Map<String, Object> data = (Map<String, Object>) request.getAttribute("data");
							if (data == null) {
								data = new LinkedHashMap<String, Object>();
								request.setAttribute("data", data);
							}
							if (!projection.getTime().isAfter(LocalDateTime.now()))
								data.put("message", "timeInPast");
							if (Projection.timeOverlaps(projection))
								data.put("message", "timeOverlaps");
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
