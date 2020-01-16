package cinema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.MovieDAO;
import cinema.model.Movie;
import cinema.searchModels.MoviesSearchModel;

@SuppressWarnings("serial")
public class MovieServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String stringID = request.getParameter("id");
			int id = Integer.parseInt(stringID);
			Movie movie = MovieDAO.getMovieByID(id);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("movie", movie);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
				genres = new ArrayList<String>(Arrays.asList(request.getParameterValues("actors[]")));
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
					Movie movie = new Movie(MovieDAO.getNextID(), title, director, actors, genres, 
							duration, distributor, country, year, description, true);
					
					if (!MovieDAO.addMovie(movie))
						throw new Exception("Greška prilikom dodavanja filma u bazu");
					
					break;
				}
				case "update": {
					
					break;
				}
				case "delete": {
					
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
