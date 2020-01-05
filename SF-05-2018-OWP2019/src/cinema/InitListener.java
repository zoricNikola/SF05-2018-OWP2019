package cinema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cinema.dao.ConnectionManager;
import cinema.dao.MovieDAO;
import cinema.dao.UserDAO;
import cinema.model.Movie;
import cinema.model.User;

public class InitListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

    public void contextInitialized(ServletContextEvent sce)  { 
    	
    	System.out.println("Initilizing...");
    	
    	ConnectionManager.open();
    	
		System.out.println("Done");
    }
	
}
