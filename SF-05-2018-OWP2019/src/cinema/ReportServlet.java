package cinema;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import cinema.dao.TicketDAO;
import cinema.model.Hall;
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.ProjectionType;
import cinema.model.Ticket;
import cinema.searchModels.ProjectionsSearchModel;
import cinema.util.DateTimeUtil;

@SuppressWarnings("serial")
public class ReportServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			ProjectionsSearchModel model = new ProjectionsSearchModel();
			LocalDate timeLow = LocalDate.now();
			try {
				timeLow = DateTimeUtil.StringToLocalDate(request.getParameter("timeLow"));
				System.out.println(timeLow);
			} catch (Exception e) {}
			model.timeLow = timeLow.atStartOfDay();
			
			LocalDate timeHigh = LocalDate.now();
			try {
				timeHigh = DateTimeUtil.StringToLocalDate(request.getParameter("timeHigh"));
				System.out.println(timeHigh);
			} catch (Exception e) {}
			model.timeHigh = timeHigh.atTime(23, 59);
			
			Map<Integer, Object> reports = new LinkedHashMap<Integer, Object>();
			
			List<Movie> movies = MovieDAO.getAll();
			
			for (Movie movie : movies) {
				model.movieID = movie.getId();
				List<Projection> projections = ProjectionDAO.searchProjections(model);
				Map<String, Object> movieData = new LinkedHashMap<String, Object>();
				movieData.put("movieID", movie.getId());
				movieData.put("movieTitle", movie.getTitle());
				movieData.put("numberOfProjections", projections.size());
				
				int numberOfTickets = 0;
				double totalPrice = 0;
				for (Projection projection : projections) {
					List<Ticket> tickets = TicketDAO.getByProjectionID(projection.getId());
					numberOfTickets += tickets.size();
					totalPrice += projection.getPrice()*tickets.size();
				}
				movieData.put("numberOfTickets", numberOfTickets);
				movieData.put("totalPrice", totalPrice);
				
				reports.put(movie.getId(), movieData);
			}
			
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("reports", reports);
			
			request.setAttribute("data", data);
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
