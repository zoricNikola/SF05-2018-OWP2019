package cinema;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.ProjectionDAO;
import cinema.dao.SeatDAO;
import cinema.dao.TicketDAO;
import cinema.dao.UserDAO;
import cinema.model.Projection;
import cinema.model.Seat;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.model.User.UserRole;

@SuppressWarnings("serial")
public class TicketServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			
			String action = request.getParameter("action");
			
			switch (action) {
				case "ticketID": {
					Ticket ticket = TicketDAO.getTicketByID(Integer.parseInt(request.getParameter("id")));
					
					if (loggedInUser.getUserRole() != UserRole.ADMIN && !ticket.getUser().getUsername().equals(loggedInUsername)) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("ticket", ticket);
					
					if (ticket.getProjection().getTime().isBefore(LocalDateTime.now()))
						data.put("inPast", true);
					else
						data.put("inPast", false);
					
					request.setAttribute("data", data);
					break;
				}
				case "projectionID": {
					if (loggedInUser.getUserRole() != UserRole.ADMIN) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					int projectionID = Integer.parseInt(request.getParameter("projectionID"));
					List<Ticket> tickets = TicketDAO.getByProjectionID(projectionID);
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("tickets", tickets);
					
					request.setAttribute("data", data);
					break; 
				}
				case "user": {
					if (loggedInUser.getUserRole() != UserRole.ADMIN && !request.getParameter("username").equals(loggedInUsername)) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					List<Ticket> tickets = TicketDAO.getByUserUsername(request.getParameter("username"));
					
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("tickets", tickets);
					
					request.setAttribute("data", data);
					break; 
				}
			}			
			request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
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
			
			String action = request.getParameter("action");
			
			switch (action) {
				case "add": {
					if (loggedInUser.getUserRole() != UserRole.USER) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
					Projection projection = ProjectionDAO.getProjectionByID(Integer.parseInt(request.getParameter("projectionID")));
					String[] stringSeatNumbers = request.getParameterValues("seatNumbers[]");
					int hallID = Integer.parseInt(request.getParameter("hallID"));
					User user = loggedInUser;
					
					List<Seat> remainingSeats = SeatDAO.getRemainingSeatsByProjectionIdAndHallID(projection.getId(), hallID);
					List<Integer> remainingSeatsNumbers = new ArrayList<Integer>();
					for (Seat seat : remainingSeats) {
						remainingSeatsNumbers.add(seat.getNumber());
					}
					
					List<Integer> seatNumbers = new ArrayList<Integer>();
					for (String number : stringSeatNumbers) {
						seatNumbers.add(Integer.parseInt(number));
					}
					
					List<Integer> reservedSeats = new ArrayList<Integer>(seatNumbers);
					reservedSeats.removeAll(remainingSeatsNumbers);
					
					if (reservedSeats.size() > 0) {
						Map<String, Object> data = (Map<String, Object>) request.getAttribute("data");
						if (data == null) {
							data = new LinkedHashMap<String, Object>();
							request.setAttribute("data", data);
						}
						
						data.put("message", "seatReserved");
						
						throw new Exception("Jedno ili više izabranih sedišta je već rezervisano");
					}
					
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
					if (loggedInUser.getUserRole() != UserRole.ADMIN) {
						request.getRequestDispatcher("./UnauthorizedServlet").forward(request, response);
						return;
					}
					
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
