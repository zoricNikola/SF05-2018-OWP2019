package cinema;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.SeatDAO;
import cinema.model.Seat;

@SuppressWarnings("serial")
public class SeatServlet extends HttpServlet {
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter("action");
			
			switch (action) {
				case "remainingSeats": {
					
					int projectionID = Integer.parseInt(request.getParameter("projectionID"));
					int hallID = Integer.parseInt(request.getParameter("hallID"));
					List<Seat> seats = SeatDAO.getRemainingSeatsByProjectionIdAndHallID(projectionID, hallID);
					
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("seats", seats);
					
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
