package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoviesSearchModel implements SearchModelInterface{
	public String title;
	public ArrayList<String> genres;
	public Integer durationLow;
	public Integer durationHigh;
	public String distributor;
	public String country;
	public Integer yearLow;
	public Integer yearHigh;
	
	public String CreateQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select * from Movies M where Active = 1 ");
		
		if (title != null && !title.equals(""))
			query.append("and Title like ? ");
		if (genres != null && genres.size() > 0) {
			query.append("and exists (select * from Genres where Movie = M.ID and ");
			
			for (int i = 0; i < genres.size(); i++)
				if (i == 0)
					query.append("Genre = ? ");
				else
					query.append("or Genre = ? ");
			
			query.append(") ");
		}
		
		if (durationLow != null && durationLow > 0)
			query.append("and Duration > ? ");
		if (durationHigh != null && durationHigh > 0)
			query.append("and Duration < ? ");
		if (distributor != null && !distributor.equals(""))
			query.append("and Distributor like ? ");
		if (country != null && !country.equals(""))
			query.append("and Country like ? ");
		if (yearLow != null && yearLow > 0)
			query.append("and Year > ? ");
		if (yearHigh != null && yearHigh > 0)
			query.append("and Year < ? ");
		
		query.append("order by ID");
		return query.toString();
	}
	
	public PreparedStatement PrepareStatement(Connection connection) throws Exception {
		PreparedStatement pstmt = connection.prepareStatement(CreateQuery());
		
		int index = 1;
		if (title != null && !title.equals(""))
			pstmt.setString(index++, "%" + title + "%");
		if (genres != null && genres.size() > 0) {
			for (int i = 0; i < genres.size(); i++)
				pstmt.setString(index++, genres.get(i));
		}
		
		if (durationLow != null && durationLow > 0)
			pstmt.setInt(index++, durationLow);
		if (durationHigh != null && durationHigh > 0)
			pstmt.setInt(index++, durationHigh);
		if (distributor != null && !distributor.equals(""))
			pstmt.setString(index++, "%" + distributor + "%");
		if (country != null && !country.equals(""))
			pstmt.setString(index++, "%" + country + "%");
		if (yearLow != null && yearLow > 0)
			pstmt.setInt(index++, yearLow);
		if (yearHigh != null && yearHigh > 0)
			pstmt.setInt(index++, yearHigh);
		
		
		return pstmt;
	}
}
