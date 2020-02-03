package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Hall;
import cinema.model.ProjectionType;
import cinema.model.Seat;

public class SeatDAO {
	
	public static List<Seat> getAll() throws Exception {
		List<Seat> seats = new ArrayList<Seat>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Seats";
			
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int number = rset.getInt(1);
				seats.add(new Seat(number, HallDAO.getHallByID(rset.getInt(2))));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return seats;
	}
	
}
