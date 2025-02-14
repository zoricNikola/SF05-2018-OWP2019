package cinema;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cinema.dao.HallDAO;
import cinema.model.Hall;

@SuppressWarnings("serial")
public class HallServlet extends HttpServlet {
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String searchType = request.getParameter("searchType");
			Map<String, Object> data = null;
			
			if (searchType.equals("all")) {
				List<Hall> allHalls = HallDAO.getAll();
				data = new LinkedHashMap<String, Object>();
				data.put("allHalls", allHalls);
				request.setAttribute("data", data);
				request.getRequestDispatcher("./SuccessServlet").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			request.getRequestDispatcher("./FailureServlet").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
