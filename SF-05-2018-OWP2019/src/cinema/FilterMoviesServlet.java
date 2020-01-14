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
public class FilterMoviesServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String title = request.getParameter("title");
			List<String> genres = Arrays.asList(request.getParameterValues("genres"));
			int durationLow = 0;
			try {
				String stringDurationLow = request.getParameter("durationLow");
				durationLow = Integer.parseInt(stringDurationLow);
			} catch (Exception e) {}
			int durationHigh = Integer.MAX_VALUE;
			try {
				String stringDurationHigh = request.getParameter("durationHigh");
				durationHigh = Integer.parseInt(stringDurationHigh);
			} catch (Exception e) {}
			String distributor = request.getParameter("distributor");
			String country = request.getParameter("country");
			int yearLow = 0;
			try {
				String stringYearLow = request.getParameter("yearLow");
				yearLow = Integer.parseInt(stringYearLow);
			} catch (Exception e) {}
			int yearHigh = Integer.MAX_VALUE;
			try {
				String stringYearHigh = request.getParameter("yearHigh");
				yearHigh = Integer.parseInt(stringYearHigh);
			} catch (Exception e) {}
			
			MoviesSearchModel model = new MoviesSearchModel();
			model.title = title;
			model.durationLow = durationLow;
			model.durationHigh = durationHigh;
			model.distributor = distributor;
			model.country = country;
			model.yearLow = yearLow;
			model.yearHigh = yearHigh;
			
			List<Movie> filteredMovies = MovieDAO.searchMovies(model);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("filteredMovies", filteredMovies);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
