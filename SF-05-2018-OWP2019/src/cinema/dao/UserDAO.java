package cinema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				
				User user = new User(username, password, registrationDate, userRole, active);
				
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
			String query = "select * from Users where Active = 1 and Username = ?";
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, UUsername);
			System.out.println(pstmt);
			
			rset = pstmt.executeQuery();
			
			if (rset.next()) {
				int index = 1;
				String username = rset.getString(index++);
				String password = rset.getString(index++);
				LocalDate registrationDate = LocalDate.parse(rset.getString(index++));
				User.UserRole userRole = User.UserRole.valueOf(rset.getString(index++));
				boolean active = rset.getInt(index++) == 1;
				
				return new User(username, password, registrationDate, userRole, active);
			}
		} finally {
			try { pstmt.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { rset.close(); } catch (Exception e1) { e1.printStackTrace(); }
			try { connection.close(); } catch (Exception e1) { e1.printStackTrace(); }
		}
		
		return null;
	}
}
