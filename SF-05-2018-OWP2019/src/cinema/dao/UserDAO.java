package cinema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import cinema.model.User;
import cinema.searchModels.UsersSearchModel;
import cinema.util.DateTimeUtil;

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
			
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				int index = 1;
				String username = rset.getString(index++);
				String password = rset.getString(index++);

//				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				int timeStamp = rset.getInt(index++);
				LocalDate registrationDate = DateTimeUtil.UnixTimeStampToLocalDate(timeStamp);
				
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				boolean loggedIn = rset.getInt(index++) == 1;
				
				User user = new User(username, password, registrationDate, userRole, active, loggedIn);
				
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
			String query = "select * from Users where Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, UUsername);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				
//				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				int timeStamp = rset.getInt(index++);
				LocalDate registrationDate = DateTimeUtil.UnixTimeStampToLocalDate(timeStamp);
				
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				boolean loggedIn = rset.getInt(index++) == 1;
				
				return new User(username, password, registrationDate, userRole, active, loggedIn);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
	
	public static boolean addUser (User user) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "insert into Users values (?, ?, ?, ?, 1) ";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getUsername());
			pstmt.setString(index++, user.getPassword());
			pstmt.setInt(index++, DateTimeUtil.LocalDateToUnixTimeStamp(user.getRegistrationDate()));
			pstmt.setString(index++, user.getUserRole().toString());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean updateUser (User user) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set Password = ?, RegistrationDate = ?, "
					+ "Role = ? where Username = ?";
			pstmt = connection.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, user.getPassword());
			pstmt.setInt(index++, DateTimeUtil.LocalDateToUnixTimeStamp(user.getRegistrationDate()));
			pstmt.setString(index++, user.getUserRole().toString());
			pstmt.setString(index++, user.getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean deleteUser (User user) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set Active = 0 where Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, user.getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean loginUser (User user) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set LoggedIn = 1 where Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, user.getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
	
	public static boolean logoutUser (User user) throws Exception {
		Connection connection = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		try {
			String query = "update Users set LoggedIn = 0 where Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, user.getUsername());
			
			return pstmt.executeUpdate() == 1;
			
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
	}
}
