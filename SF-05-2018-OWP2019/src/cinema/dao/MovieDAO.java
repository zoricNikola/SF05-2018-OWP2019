package cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;
import cinema.model.User;
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
	
	public static boolean addMovie (Movie movie) throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			connection.setAutoCommit(false);
			connection.commit();
			String query = "insert into Movies (Title, Director, Duration, Distributor, Country, "
					+ "Year, Description, Active) values (?, ?, ?, ?, ?, ?, ?, 1)";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, movie.getTitle());
			pstmt.setString(index++, movie.getDirector());
			pstmt.setInt(index++, movie.getDuration());
			pstmt.setString(index++, movie.getDistributor());
			pstmt.setString(index++, movie.getCountry());
			pstmt.setInt(index++, movie.getYear());
			pstmt.setString(index++, movie.getDescription());
			
			if (pstmt.executeUpdate() == 1) {
				movie.setId(getNextID());
				if (movie.getGenres() != null && movie.getGenres().size() > 0) {
					pstmt.close();
					query = "insert into Genres values ";
					for (int i = 0; i < movie.getGenres().size(); i++) {
						if (i == 0)
							query += "(?, ?)";
						else
							query += ", (?, ?)";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					for (String genre : movie.getGenres()) {
						pstmt.setInt(index++, movie.getId());
						pstmt.setString(index++, genre);
					}
					
					pstmt.executeUpdate();
				}
				if (movie.getActors() != null && movie.getActors().size() > 0) {
					pstmt.close();
					query = "insert into Actors values ";
					for (int i = 0; i < movie.getActors().size(); i++) {
						if (i == 0)
							query += "(?, ?)";
						else
							query += ", (?, ?)";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					for (String actor : movie.getActors()) {
						pstmt.setInt(index++, movie.getId());
						pstmt.setString(index++, actor);
					}
					
					pstmt.executeUpdate();
				}
				connection.commit();
				return true;
			}
			
		} 
		catch (Exception e) {
			try { connection.rollback(); } catch (Exception e1) { e1.printStackTrace(); }
			
			throw e;
		} 
		finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		return false;
	}
	
	public static int getNextID() throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select max(ID) + 1 from Movies";
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				return rset.getInt(1);
			}
			
					
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return 0;
	}
	
}
