package es.um.guestbook;

import com.googlecode.objectify.ObjectifyService;

import es.um.guestbook.Greeting;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OfyHelper implements ServletContextListener {
	
@Override
	public void contextInitialized(ServletContextEvent sce) {
	ObjectifyService.init();
	ObjectifyService.register(Guestbook.class);
	ObjectifyService.register(Greeting.class);
}

@Override
public void contextDestroyed(ServletContextEvent sce) {
	// App engine does not currently invoke this method
}
}
