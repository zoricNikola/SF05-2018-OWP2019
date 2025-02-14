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
	
	public static List<Seat> getByHallID(int hId) throws Exception {
		List<Seat> seats = new ArrayList<Seat>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Seats where Hall = ?";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, hId);
			
			rset = pstmt.executeQuery();
			
			Hall hall = HallDAO.getHallByID(hId);
			
			while (rset.next() && hall != null) {
				int number = rset.getInt(1);
				seats.add(new Seat(number, hall));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return seats;
	}
	
	public static Seat getByNumberAndHallID(int sNumber, int hID) throws Exception {
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Seats where Number = ? and Hall = ?";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, sNumber);
			pstmt.setInt(2, hID);
			
			rset = pstmt.executeQuery();
			
			Hall hall = HallDAO.getHallByID(hID);
			
			if (rset.next()) {
				int number = rset.getInt(1);
				return new Seat(number, hall);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static List<Seat> getRemainingSeatsByProjectionIdAndHallID(int pID, int hID) throws Exception {
		List<Seat> seats = new ArrayList<Seat>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Seats where Hall = ? and Number not in ( select S.Number from Tickets as T, Projections as P, Seats as S where T.Active = 1 and P.ID = ? and T.Projection = P.ID and T.SeatNumber = S.Number and T.Hall = S.Hall ) ";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, hID);
			pstmt.setInt(2, pID);
			
			rset = pstmt.executeQuery();
			
			Hall hall = HallDAO.getHallByID(hID);
			
			while (rset.next() && hall != null) {
				int number = rset.getInt(1);
				seats.add(new Seat(number, hall));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return seats;
	}
	
}
