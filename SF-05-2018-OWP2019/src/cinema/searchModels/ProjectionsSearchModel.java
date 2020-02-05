package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

import cinema.model.Hall;
import cinema.model.ProjectionType;
import cinema.util.DateTimeUtil;

public class ProjectionsSearchModel implements SearchModelInterface {
	public String movie;
	public List<ProjectionType> projectionTypes;
	public List<Hall> halls;
	public Double priceLow;
	public Double priceHigh;
	public LocalDateTime timeLow;
	public LocalDateTime timeHigh;

	@Override
	public String CreateQuery() {
		StringBuilder query = new StringBuilder();
		query.append("select P.ID, P.Movie, P.ProjectionType, P.Hall, P.Time, P.Price, P.Admin, P.Active from Projections as P, Movies as M where P.Active = 1 and P.Movie = M.ID ");
		
		if (movie != null && !movie.equals(""))
			query.append("and M.Title like '%' || ? || '%' ");
		if (projectionTypes != null && projectionTypes.size() > 0) {
			query.append("and ( ");
			
			for (int i = 0; i < projectionTypes.size(); i++)
				if (i == 0)
					query.append("P.ProjectionType = ? ");
				else
					query.append("or P.ProjectionType = ? ");
			
			query.append(") ");
		}
		if (halls != null && halls.size() > 0) {
			query.append("and ( ");
			
			for (int i = 0; i < halls.size(); i++)
				if (i == 0)
					query.append("P.Hall = ? ");
				else
					query.append("or P.Hall = ? ");
			
			query.append(") ");
		}
		if (priceLow != null && priceLow > 0)
			query.append("and P.Price >= ? ");
		if (priceHigh != null && priceHigh > 0)
			query.append("and P.Price <= ? ");
		if (timeLow != null)
			query.append("and P.Time >= ? ");
		if (timeHigh != null)
			query.append("and P.Time <= ? ");
		
		query.append("order by P.ID");
		return query.toString();
	}

	@Override
	public PreparedStatement PrepareStatement(Connection connection) throws Exception {
		PreparedStatement pstmt = connection.prepareStatement(CreateQuery());
		
		int index = 1;
		if (movie != null && !movie.equals(""))
			pstmt.setString(index++, movie);
		if (projectionTypes != null && projectionTypes.size() > 0) {
			for (int i = 0; i < projectionTypes.size(); i++)
				pstmt.setInt(index++, projectionTypes.get(i).getId());
		}
		if (halls != null && halls.size() > 0) {
			for (int i = 0; i < halls.size(); i++)
				pstmt.setInt(index++, halls.get(i).getId());
		}
		if (priceLow != null && priceLow > 0)
			pstmt.setDouble(index++, priceLow);
		if (priceHigh != null && priceHigh > 0)
			pstmt.setDouble(index++, priceHigh);
		if (timeLow != null)
			pstmt.setInt(index++, DateTimeUtil.LocalDateTimeToUnixTimeStamp(timeLow));
		if (timeHigh != null)
			pstmt.setInt(index++, DateTimeUtil.LocalDateTimeToUnixTimeStamp(timeHigh));
		
		return pstmt;
	}

}
