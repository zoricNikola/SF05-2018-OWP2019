package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Hall;
import cinema.model.Projection;
import cinema.model.Seat;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.util.DateTimeUtil;

public class TicketDAO {
	
	public static List<Ticket> getAll() throws Exception {
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Tickets where Active = 1";
			
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			int index = 1;
			while (rset.next()) {
				int id = rset.getInt(index++);
				Projection projection = ProjectionDAO.getProjectionByID(rset.getInt(index++));
				Seat seat = SeatDAO.getByNumberAndHallID(rset.getInt(index++), rset.getInt(index++));
				LocalDateTime time = DateTimeUtil.UnixTimeStampToLocalDateTime(rset.getInt(index++));
				User user = UserDAO.getUserByUsername(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				tickets.add(new Ticket(id, projection, seat, time, user, active));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return tickets;
	}
	
	public static Ticket getTicketByID(int tId) throws Exception {
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Tickets where ID = ?";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, tId);
			
			rset = pstmt.executeQuery();
			
			
			int index = 1;
			if (rset.next()) {
				int id = rset.getInt(index++);
				Projection projection = ProjectionDAO.getProjectionByID(rset.getInt(index++));
				Seat seat = SeatDAO.getByNumberAndHallID(rset.getInt(index++), rset.getInt(index++));
				LocalDateTime time = DateTimeUtil.UnixTimeStampToLocalDateTime(rset.getInt(index++));
				User user = UserDAO.getUserByUsername(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				return new Ticket(id, projection, seat, time, user, active);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static List<Ticket> getByProjectionID(int pID) throws Exception {
		List<Ticket> tickets = new ArrayList<Ticket>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Tickets where Projection = ? ";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, pID);
			
			rset = pstmt.executeQuery();
			
			int index = 1;
			while (rset.next()) {
				int id = rset.getInt(index++);
				Projection projection = ProjectionDAO.getProjectionByID(rset.getInt(index++));
				Seat seat = SeatDAO.getByNumberAndHallID(rset.getInt(index++), rset.getInt(index++));
				LocalDateTime time = DateTimeUtil.UnixTimeStampToLocalDateTime(rset.getInt(index++));
				User user = UserDAO.getUserByUsername(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				tickets.add(new Ticket(id, projection, seat, time, user, active));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return tickets;
	}
	
	public static Ticket getByProjectionIdAndSeatNumberAndHallID(int pID, int sNumber, int hID) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Tickets where Projection = ? and SeatNumber = ? and Hall = ? ";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, pID);
			
			rset = pstmt.executeQuery();
			
			int index = 1;
			if (rset.next()) {
				int id = rset.getInt(index++);
				Projection projection = ProjectionDAO.getProjectionByID(rset.getInt(index++));
				Seat seat = SeatDAO.getByNumberAndHallID(rset.getInt(index++), rset.getInt(index++));
				LocalDateTime time = DateTimeUtil.UnixTimeStampToLocalDateTime(rset.getInt(index++));
				User user = UserDAO.getUserByUsername(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				return new Ticket(id, projection, seat, time, user, active);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static boolean addTicket (Ticket ticket) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "insert into Tickets (Projection, SeatNumber, Hall, Time, User, Active) values (?, ?, ?, ?, ?, 1) ";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, ticket.getProjection().getId());
			pstmt.setInt(index++, ticket.getSeat().getNumber());
			pstmt.setInt(index++, ticket.getHall().getId());
			pstmt.setInt(index++, DateTimeUtil.LocalDateTimeToUnixTimeStamp(ticket.getTime()));
			pstmt.setString(index++, ticket.getUser().getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean deleteTicket (Ticket ticket) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Tickets set Active = 0 where ID = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, ticket.getId());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}

}
