package cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.MovieDAO;
import cinema.dao.ProjectionDAO;
import cinema.dao.SeatDAO;
import cinema.dao.TicketDAO;
import cinema.dao.UserDAO;
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.Seat;
import cinema.model.Ticket;
import cinema.model.User;

@SuppressWarnings("serial")
public class TicketServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter("action");
			
			switch (action) {
				case "add": {
					Projection projection = ProjectionDAO.getProjectionByID(Integer.parseInt(request.getParameter("projectionID")));
					String[] stringSeatNumbers = request.getParameterValues("seatNumbers[]");
					int hallID = Integer.parseInt(request.getParameter("hallID"));
					User user = UserDAO.getUserByUsername((String)request.getSession().getAttribute("loggedInUsername"));
					
					for (String number : stringSeatNumbers) {
						int seatNumber = Integer.parseInt(number);
						Seat seat = SeatDAO.getByNumberAndHallID(seatNumber, hallID);
						
						Ticket ticket = new Ticket(0, projection, seat, LocalDateTime.now(), user, true);
						if (!TicketDAO.addTicket(ticket))
							throw new Exception("Greška prilikom dodavanja karte u bazu");
					}
					
					break;
				}
				case "delete": {
					Ticket ticket = TicketDAO.getTicketByID((Integer.parseInt(request.getParameter("id"))));
					if (ticket != null) {
						if (!TicketDAO.deleteTicket(ticket))
							throw new Exception("Greška prilikom brisanja karte iz baze");
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
