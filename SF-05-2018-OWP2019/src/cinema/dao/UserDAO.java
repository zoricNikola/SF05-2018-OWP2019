package cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cinema.model.User;
import cinema.searchModels.UsersSearchModel;

public class UserDAO {
	
	public static List<User> getAll () throws Exception {
		UsersSearchModel model = new UsersSearchModel();
		
		return searchUsers(model);
	}
	
	public static List<User> searchUsers (UsersSearchModel model) throws Exception {
		List<User> users = new ArrayList<User>();
		
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
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				
				User user = new User(id, username, password, registrationDate, userRole, active);
				
				users.add(user);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return users;
	}
	
	public static User getUserByUsername (String UUsername) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "select rowid, * from Users where Active = 1 and Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, UUsername);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				int id = rset.getInt(index++);
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				
				return new User(id, username, password, registrationDate, userRole, active);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static boolean addUser (User user) throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "insert into Users values (?, ?, ?, ?, 1) ";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getUsername());
			pstmt.setString(index++, user.getPassword());
			pstmt.setDate(index++, Date.valueOf(user.getRegistrationDate()));
			pstmt.setString(index++, user.getUserRole().toString());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean updateUser (User user) throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set Username = ?, Password = ?, RegistrationDate = ?, "
					+ "Role = ? where rowid = ?";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getUsername());
			pstmt.setString(index++, user.getPassword());
			pstmt.setDate(index++, Date.valueOf(user.getRegistrationDate()));
			pstmt.setString(index++, user.getUserRole().toString());
			pstmt.setInt(index++, user.getId());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean deleteUser (User user) throws SQLException {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set Active = 0 where rowid = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, user.getId());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
}
