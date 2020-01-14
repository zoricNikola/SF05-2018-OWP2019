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
			String query = "select * from Movies where ID = ? ";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, mID);
			
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
	
	public static boolean addMovie (Movie movie) throws Exception {
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
	
	public static int getNextID() throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select ifnull(max(ID), 0) + 1 from Movies";
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
	
	public static boolean deleteMovie (Movie movie) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Movies set Active = 0 where ID = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, movie.getId());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean updateMovie (Movie movie) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			connection.setAutoCommit(false);
			connection.commit();
			String query = "update Movies set Title = ?, Director = ?, Duration = ?, Distributor = ?, "
					+ "Country = ?, Year = ?, Description = ?";
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
				List<String> currentGenres = GenreDAO.getByMovieID(movie.getId());
				
				List<String> toDeleteGenres = new ArrayList<String>(currentGenres);
				toDeleteGenres.removeAll(movie.getGenres());
				
				List<String> toAddGenres = new ArrayList<String>(movie.getGenres());
				toAddGenres.removeAll(currentGenres);
				
				if (toDeleteGenres.size() > 0) {
					pstmt.close();
					query = "delete from Genres where Movie = ? and Genre = ? ";
					for (int i = 1; i < toDeleteGenres.size(); i++) {
						query += "or Genre = ? ";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					pstmt.setInt(index++, movie.getId());
					for (String genre : toDeleteGenres) {
						pstmt.setString(index++, genre);
					}
					
					pstmt.executeUpdate();
				}
				
				if (toAddGenres.size() > 0) {
					pstmt.close();
					query = "insert into Genres values ";
					for (int i = 0; i < toAddGenres.size(); i++) {
						if (i == 0)
							query += "(?, ?)";
						else
							query += ", (?, ?)";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					for (String genre : toAddGenres) {
						pstmt.setInt(index++, movie.getId());
						pstmt.setString(index++, genre);
					}
					
					pstmt.executeUpdate();
				}
				
				List<String> currentActors = ActorDAO.getByMovieID(movie.getId());
				
				List<String> toDeleteActors = new ArrayList<String>(currentActors);
				toDeleteActors.removeAll(movie.getActors());
				
				List<String> toAddActors = new ArrayList<String>(movie.getActors());
				toAddActors.removeAll(currentActors);
				
				if (toDeleteActors.size() > 0) {
					pstmt.close();
					query = "delete from Actors where Movie = ? and Actor = ? ";
					for (int i = 1; i < toDeleteActors.size(); i++) {
						query += "or Actor = ? ";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					pstmt.setInt(index++, movie.getId());
					for (String actor : toDeleteActors) {
						pstmt.setString(index++, actor);
					}
					
					pstmt.executeUpdate();
				}
				
				if (toAddActors.size() > 0) {
					pstmt.close();
					query = "insert into Actors values ";
					for (int i = 0; i < toAddActors.size(); i++) {
						if (i == 0)
							query += "(?, ?)";
						else
							query += ", (?, ?)";
					}
					pstmt = connection.prepareStatement(query);
					index = 1;
					for (String actor : toAddActors) {
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
}
