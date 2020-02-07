package cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.MovieDAO;
import cinema.dao.ProjectionDAO;
import cinema.model.Movie;
import cinema.model.Projection;

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
