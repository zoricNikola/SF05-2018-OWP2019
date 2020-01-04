package cinema.searchModels;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface SearchModelInterface {
	public String CreateQuery();
	public PreparedStatement PrepareStatement(Connection connection) throws Exception;
}
