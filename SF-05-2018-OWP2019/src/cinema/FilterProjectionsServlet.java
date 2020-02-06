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
import cinema.model.Hall;
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.ProjectionType;
import cinema.searchModels.MoviesSearchModel;
import cinema.searchModels.ProjectionsSearchModel;
import cinema.util.DateTimeUtil;

@SuppressWarnings("serial")
public class FilterProjectionsServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String movie = request.getParameter("movie");
			List<ProjectionType> projectionTypes = new ArrayList<ProjectionType>();
			if (request.getParameterValues("projectionTypes[]") != null) {
				for (String s : request.getParameterValues("projectionTypes[]")) {
					projectionTypes.add(ProjectionTypeDAO.getByID(Integer.parseInt(s)));
				}
			}
			List<Hall> halls = new ArrayList<Hall>();
			if (request.getParameterValues("halls[]") != null) {
				for (String s : request.getParameterValues("halls[]")) {
					halls.add(HallDAO.getHallByID(Integer.parseInt(s)));
				}
			}
			double priceLow = 0;
			try {
				String stringPriceLow = request.getParameter("priceLow");
				priceLow = Double.parseDouble(stringPriceLow);
			} catch (Exception e) {}
			double priceHigh = 0;
			try {
				String stringPriceHigh = request.getParameter("priceHigh");
				priceHigh = Double.parseDouble(stringPriceHigh);
			} catch (Exception e) {}
			LocalDateTime timeLow = null;
			try {
				timeLow = DateTimeUtil.StringToLocalDateTime(request.getParameter("timeLow"));
				System.out.println(timeLow);
			} catch (Exception e) {}
			LocalDateTime timeHigh = null;
			try {
				timeHigh = DateTimeUtil.StringToLocalDateTime(request.getParameter("timeHigh"));
				System.out.println(timeHigh);
			} catch (Exception e) {}
			
			ProjectionsSearchModel model = new ProjectionsSearchModel();
			
			model.movie = movie;
			model.projectionTypes = projectionTypes;
			model.halls = halls;
			model.priceLow = priceLow;
			model.priceHigh = priceHigh;
			model.timeLow = timeLow;
			model.timeHigh = timeHigh;
			
			
			List<Projection> filteredProjections = ProjectionDAO.searchProjections(model);
			Map<Integer, List<Projection>> projectionsMap = new LinkedHashMap<Integer, List<Projection>>();
			
			for (Projection projection : filteredProjections) {
				if (projectionsMap.get(projection.getMovie().getId()) == null)
					projectionsMap.put(projection.getMovie().getId(), new ArrayList<Projection>());
				
				projectionsMap.get(projection.getMovie().getId()).add(projection);
			}
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("projectionsMap", projectionsMap);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
