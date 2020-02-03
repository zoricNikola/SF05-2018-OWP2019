package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Hall;
import cinema.model.ProjectionType;

public class HallDAO {
	
	public static List<Hall> getAll() throws Exception {
		List<Hall> halls = new ArrayList<Hall>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Halls";
			
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int id = rset.getInt(1);
				String name = rset.getString(2);
				halls.add(new Hall(id, name, ProjectionTypeDAO.getByHallID(id)));
				
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return halls;
	}
	
	public static Hall getHallByID(int hId) throws Exception {
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Halls where ID = ?";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, hId);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int id = rset.getInt(1);
				String name = rset.getString(2);
				return new Hall(id, name, ProjectionTypeDAO.getByHallID(id));
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}

}
