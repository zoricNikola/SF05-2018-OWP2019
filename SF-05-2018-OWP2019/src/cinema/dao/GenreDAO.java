package cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cinema.model.Movie;

public class GenreDAO {
	
	public static List<String> getAll() throws Exception {
		List<String> genres = new ArrayList<String>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select distinct Genre from Genres";
			
			pstmt = connection.prepareStatement(query);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				String genre = rset.getString(1);
				genres.add(genre);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return genres;
	}
	
	public static List<String> getByMovieID(int id) throws Exception {
		List<String> genres = new ArrayList<String>();
		
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select * from Genres where Movie = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, id);
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				String genre = rset.getString(2);
				genres.add(genre);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return genres;
	}
	
	public static boolean addGenres (Movie movie) throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			if (movie.getGenres() != null && movie.getGenres().size() > 0) {
				String query = "insert into Genres values ";
				for (int i = 0; i < movie.getGenres().size(); i++) {
					if (i == 0)
						query += "(?, ?)";
					else
						query += ", (?, ?)";
				}
				pstmt = connection.prepareStatement(query);
				int index = 1;
				for (String genre : movie.getGenres()) {
					pstmt.setInt(index++, movie.getId());
					pstmt.setString(index++, genre);
				}
				
				return pstmt.executeUpdate() == 1;
			}
			else
				return true;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
}
