package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UsersSearchModel implements SearchModelInterface{
	public String username;
	public String userRole;
	
	public String CreateQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select * from Users where Active = 1 ");
		
		if (username != null && !username.equals(""))
			query.append("and Username like '%' || ? || '%' ");
		if (userRole != null && !userRole.equals(""))
			query.append("and Role like '%' || ? || '%' ");
		
		return query.toString();
	}
	
	public PreparedStatement PrepareStatement(Connection connection) throws Exception {
		PreparedStatement pstmt = connection.prepareStatement(CreateQuery());
		
		int index = 1;
		if (username != null && !username.equals(""))
			pstmt.setString(index++, username);
		if (userRole != null && !userRole.equals(""))
			pstmt.setString(index++, userRole);
		
		return pstmt;
	}

}
