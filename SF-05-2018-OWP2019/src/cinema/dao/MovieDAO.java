package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;
import cinema.searchModels.MoviesSearchModel;

public class MovieDAO {
	
	public static List<Movie> getAll () throws Exception {
		MoviesSearchModel model = new MoviesSearchModel();
		
		return searchMovies(model);
		
	}
	
	public static List<Movie> searchMovies (MoviesSearchModel model) throws Exception {
		List<Movie> movies = new ArrayList<Movie>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			pstmt = model.PrepareStatement(connection);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				String title = rset.getString(index++);
				String director = rset.getString(index++);
				int duration = rset.getInt(index++);
				String distributor = rset.getString(index++);
				String country = rset.getString(index++);
				int year = rset.getInt(index++);
				String description = rset.getString(index++);
				boolean active = rset.getInt(index++) == 1;
				
				List<String> actors = ActorDAO.getByMovieID(id);
				List<String> genres = GenreDAO.getByMovieID(id);
				
				Movie movie = new Movie(id, title, director, actors, genres, duration, 
						distributor, country, year, description, active);
				movies.add(movie);
			}
			
					
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return movies;
	}
	
	public static Movie getMovieByID (int mID) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Movies where Active = 1 and ID = ? ";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, mID);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				String title = rset.getString(index++);
				String director = rset.getString(index++);
				int duration = rset.getInt(index++);
				String distributor = rset.getString(index++);
				String country = rset.getString(index++);
				int year = rset.getInt(index++);
				String description = rset.getString(index++);
				boolean active = rset.getInt(index++) == 1;
				
				List<String> actors = ActorDAO.getByMovieID(id);
				List<String> genres = GenreDAO.getByMovieID(id);
				
				return new Movie(id, title, director, actors, genres, duration, 
						distributor, country, year, description, active);
			}
			
					
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
}
