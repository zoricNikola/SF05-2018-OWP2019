package cinema;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.MovieDAO;
import cinema.model.Movie;

@SuppressWarnings("serial")
public class MovieServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String stringID = request.getParameter("id");
		try {
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
		doGet(request, response);
	}

}
