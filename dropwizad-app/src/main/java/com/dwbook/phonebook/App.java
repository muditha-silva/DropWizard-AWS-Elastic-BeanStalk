package com.dwbook.phonebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import com.dwbook.phonebook.resources.ContactResource;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;
import org.skife.jdbi.v2.DBI;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.db.DataSourceFactory;



public class App extends Application <PhonebookConfiguration>
{
	private static final Logger LOGGER =LoggerFactory.getLogger(App.class);
	private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

	@Override
	public void initialize(Bootstrap<PhonebookConfiguration> bootstrap) {
		swaggerDropwizard.onInitialize(bootstrap);
	}

	public void run(PhonebookConfiguration configuration, Environment env) throws Exception {		
		// Create a DBI factory and build a JDBI instance
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(env, configuration.getDataSourceFactory(), "mysql");		
		// Add the resource to the environment
		env.jersey().register(new ContactResource(jdbi, env.getValidator()));	
		/*
		 * Swagger API
		 */
		swaggerDropwizard.onRun(configuration,env,configuration.getHost(),configuration.getPort());
	}

	public static void main( String[] args )
	{
		try{
			new App().run(args);
		}
		catch(Exception e){

		}
	}
}
