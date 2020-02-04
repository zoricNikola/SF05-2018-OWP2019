package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cinema.model.ProjectionType;

public class ProjectionTypeDAO {
	
	public static List<ProjectionType> getAll() throws Exception {
		List<ProjectionType> projectionTypes = new ArrayList<ProjectionType>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from ProjectionTypes";
			
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int id = rset.getInt(1);
				String name = rset.getString(2);
				ProjectionType type = new ProjectionType(id, name);
				projectionTypes.add(type);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return projectionTypes;
	}
	
	public static List<ProjectionType> getByHallID(int hId) throws Exception {
		List<ProjectionType> projectionTypes = new ArrayList<ProjectionType>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select P.ID, P.Name from ProjectionTypes P, HallTypes H "
					+ "where P.ID = H.ProjectionType and H.Hall = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, hId);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int id = rset.getInt(1);
				String name = rset.getString(2);
				ProjectionType type = new ProjectionType(id, name);
				projectionTypes.add(type);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return projectionTypes;
	}
	
	public static ProjectionType getByID(int ptId) throws Exception {
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from ProjectionTypes where ID = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, ptId);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int id = rset.getInt(1);
				String name = rset.getString(2);
				return new ProjectionType(id, name);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
}
