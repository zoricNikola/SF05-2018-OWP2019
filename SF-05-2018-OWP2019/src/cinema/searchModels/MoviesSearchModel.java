package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoviesSearchModel {
	public String title;
	public ArrayList<String> genres;
	public Integer durationLow;
	public Integer durationHigh;
	public String distributor;
	public String country;
	public Integer yearLow;
	public Integer yearHigh;
	
	public String CreateQuery() {
		String query = "select * from Movies where Active = 1 ";
		
		
		
		query += "order by ID";
		return query;
	}
	
	public PreparedStatement PrepareStatement(Connection connection) throws Exception {
		PreparedStatement pstmt = connection.prepareStatement(CreateQuery());
		
//		int index = 1;
//		pstmt.setString(index++, "%" + title + "%");
		
		
		return pstmt;
	}
}
