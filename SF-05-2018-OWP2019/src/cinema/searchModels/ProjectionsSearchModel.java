package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import cinema.model.Hall;
import cinema.model.ProjectionType;
import cinema.util.DateTimeUtil;

public class ProjectionsSearchModel implements SearchModelInterface {
	public String movie;
	public ProjectionType projectionType;
	public Hall hall;
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
		if (projectionType != null)
			query.append("and P.ProjectionType = ? ");
		if (hall != null)
			query.append("and P.Hall = ? ");
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
		if (projectionType != null)
			pstmt.setInt(index++, projectionType.getId());
		if (hall != null)
			pstmt.setInt(index++, hall.getId());
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
