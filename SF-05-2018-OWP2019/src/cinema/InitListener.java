package cinema;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cinema.dao.ConnectionManager;

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
