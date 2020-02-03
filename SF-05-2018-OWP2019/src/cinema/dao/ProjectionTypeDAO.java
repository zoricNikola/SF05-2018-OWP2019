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
	
}
