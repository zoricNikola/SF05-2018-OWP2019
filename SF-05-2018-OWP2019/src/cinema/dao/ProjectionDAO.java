package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.User;
import cinema.searchModels.ProjectionsSearchModel;
import cinema.util.DateTimeUtil;

public class ProjectionDAO {
	
	public static List<Projection> getAll() throws Exception {
		ProjectionsSearchModel model = new ProjectionsSearchModel();
		
		return searchProjections(model);
	}
	
	public static List<Projection> searchProjections(ProjectionsSearchModel model) throws Exception {
		List<Projection> projections = new ArrayList<Projection>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			pstmt = model.PrepareStatement(connection);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				int movieID = rset.getInt(index++);
				int projectionTypeID = rset.getInt(index++);
				int hallID = rset.getInt(index++);
				int timeStamp = rset.getInt(index++);
				double price = rset.getDouble(index++);
				String adminUsername = rset.getString(index++);
				boolean active = rset.getInt(index++) == 1;
				Projection projection = new Projection(id, MovieDAO.getMovieByID(movieID), 
						ProjectionTypeDAO.getByID(projectionTypeID), HallDAO.getHallByID(hallID), 
						DateTimeUtil.UnixTimeStampToLocalDateTime(timeStamp), price, 
						UserDAO.getUserByUsername(adminUsername), active);
				
				projections.add(projection);
				
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return projections;
	}
	
	public static Projection getProjectionByID(int ptId) throws Exception {
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Projections where ID = ?";
			
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, ptId);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				int movieID = rset.getInt(index++);
				int projectionTypeID = rset.getInt(index++);
				int hallID = rset.getInt(index++);
				int timeStamp = rset.getInt(index++);
				double price = rset.getDouble(index++);
				String adminUsername = rset.getString(index++);
				boolean active = rset.getInt(index++) == 1;
				Projection projection = new Projection(id, MovieDAO.getMovieByID(movieID), 
						ProjectionTypeDAO.getByID(projectionTypeID), HallDAO.getHallByID(hallID), 
						DateTimeUtil.UnixTimeStampToLocalDateTime(timeStamp), price, 
						UserDAO.getUserByUsername(adminUsername), active);
				
				return projection;
				
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static boolean addProjection (Projection projection) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "insert into Projections (Movie, ProjectionType, Hall, Time, Price, Admin, Active) values (?, ?, ?, ?, ?, ?, 1) ";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, projection.getMovie().getId());
			pstmt.setInt(index++, projection.getProjectionType().getId());
			pstmt.setInt(index++, projection.getHall().getId());
			pstmt.setInt(index++, DateTimeUtil.LocalDateTimeToUnixTimeStamp(projection.getTime()));
			pstmt.setDouble(index++, projection.getPrice());
			pstmt.setString(index++, projection.getAdmin().getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean deleteProjection (Projection projection) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Projections set Active = 0 where ID = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, projection.getId());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}

}
