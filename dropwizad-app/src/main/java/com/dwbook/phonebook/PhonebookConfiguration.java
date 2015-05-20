package com.dwbook.phonebook;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import io.dropwizard.db.DataSourceFactory;

public class PhonebookConfiguration extends Configuration {

	@JsonProperty
	private DataSourceFactory database = new DataSourceFactory();
	
	@JsonProperty
	private String host;
	
	@JsonProperty
	private int port;
	
	
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}
	
	public String getHost(){
		return host;
	}
	
	public int getPort(){
		return port;
	}
	
}