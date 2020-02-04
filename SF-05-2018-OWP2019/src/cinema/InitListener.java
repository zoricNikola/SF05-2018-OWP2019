package cinema;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cinema.dao.ConnectionManager;
import cinema.dao.MovieDAO;
import cinema.dao.ProjectionDAO;
import cinema.dao.ProjectionTypeDAO;
import cinema.dao.UserDAO;
import cinema.model.Movie;
import cinema.model.Projection;
import cinema.model.User;
import cinema.util.DateTimeUtil;

public class InitListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	System.out.println("Initilizing...");
    	
    	ConnectionManager.open();
    	
    	try {
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
		System.out.println("Done");
    }
	
}
