package com.dwbook.phonebook.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dropwizard.validation.ValidationMethod;


@ApiModel(value = "A Contact is associated to an id")
public class Contact {
	private final int id;
	@NotBlank
	@Length(min=2, max=255)
	private final String firstName;
	@NotBlank
	@Length(min=2, max=255)
	private final String lastName;
	@NotBlank
	@Length(min=2, max=30)
	private final String phone;

	public Contact() {	
		this.id = 0;
		this.firstName = "";	
		this.lastName="";		
		this.phone = "";	
	}

	public Contact(int id, String firstName, String lastName,String phone) {
		this.id = id;
		this.firstName = firstName;	
		this.lastName=lastName;		
		this.phone = phone;
	}	

	@JsonProperty   
	@ApiModelProperty(value = "Id")
	public int getId() {
		return id;
	}

	@JsonProperty   
	@ApiModelProperty(value = "First Name", required=true)
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty   
	@ApiModelProperty(value = "Last Name", required=true)
	public String getLastName() {
		return lastName;
	}

	@JsonProperty   
	@ApiModelProperty(value = "Phone Number",required=true)
	public String getPhone() {
		return phone;
	}	

	@JsonIgnore
	@ValidationMethod(message="John Doe is not a valid person!")
	public boolean isValidPerson() {
		if (firstName.equals("John") && lastName.equals("Doe")) {
			return false;
		}
		else {
			return true;
		}
	}

}