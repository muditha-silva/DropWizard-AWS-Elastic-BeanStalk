package com.dwbook.phonebook.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import com.dwbook.phonebook.representations.Contact;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.*;
import javax.validation.executable.ValidateOnExecution;
import com.codahale.metrics.annotation.Timed;
import javax.validation.Valid;
import org.skife.jdbi.v2.DBI;
import com.dwbook.phonebook.dao.ContactDAO;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import javax.validation.Validator;
import javax.ws.rs.core.Response.Status;


@Path("/contact")
@Api(value = "/contact", description = "This service handle CRUD operation of user contacts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContactResource {

	private final ContactDAO contactDao;
	private final Validator validator;

	public ContactResource(DBI jdbi, Validator validator) {
		this.contactDao = jdbi.onDemand(ContactDAO.class); 
		this.validator = validator;
	}

	@GET
	@Path("/{id}")	
	@ApiOperation(
			value = "Get the contact by given the id",
			notes = "API for return the contact by given the id",
			response = Contact.class
			)	
	public Response getContact(@ApiParam(value = "Id of contact to fetch", required = true) @PathParam("id") int id)  {
		Contact contact = contactDao.getContactById(id);
		return Response.ok(contact).build();
	}

	@POST
	@ValidateOnExecution
	@Timed
	@ApiOperation(
			value = "Add a new contact",
			notes = "API for add a new contact ",
			response = Contact.class
			)
	@ApiResponses(value = {        
			@ApiResponse(code = 201, message = "Successfully created the contact"),
			@ApiResponse(code = 400, message = "Bad Request firstName,lastName,phone required")
	})
	public Response createContact(@ApiParam(value = "Contact to store", required = true) Contact contact)throws URISyntaxException {
		// Validate the contact's data
		Set<ConstraintViolation<Contact>> violations =validator.validate(contact);
		// Are there any constraint violations?
		if (violations.size() > 0) {
			// Validation errors occurred
			ArrayList<String> validationMessages = new ArrayList<String>();
			for (ConstraintViolation<Contact> violation :violations) {
				validationMessages.add(violation.getPropertyPath().toString() +":" + violation.getMessage());
			}
			return Response
					.status(Status.BAD_REQUEST)
					.entity(validationMessages)
					.build();
		}
		else {
			// OK, no validation errors
			// Store the new contact
			int newContactId =contactDao.createContact(contact.getFirstName(),contact.getLastName(), contact.getPhone());
			return Response.created(new URI(String.valueOf(newContactId))).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(
			value = "Delete a contact by using a given id",
			notes = "API for delete a contact by using a id "			
			)
	public Response deleteContact(@ApiParam(value = "Id of contact to delete", required = true)@PathParam("id") int id)  {
		contactDao.deleteContact(id);
		return Response.noContent().build();
	}

	@PUT
	@Path("/{id}")
	@ValidateOnExecution
	@Timed
	@ApiOperation(
			value = "Update a contact by using a given id",
			notes = "API for update a contact by using a id ",
			response = Contact.class
			)
	@ApiResponses(value = {        
			@ApiResponse(code = 200, message = "Successfully updated the contact"),
			@ApiResponse(code = 400, message = "Bad Request firstName,lastName,phone required")
			})
	public Response updateContact(@ApiParam(value = "Id of contact to update", required = true)@PathParam("id") int id,
			@ApiParam(value = "Contact to store", required = true) Contact contact) {
		// update the contact with the provided ID
		// Validate the updated data
		Set<ConstraintViolation<Contact>> violations =validator.validate(contact);
		// Are there any constraint violations?
		if (violations.size() > 0) {
			// Validation errors occurred
			ArrayList<String> validationMessages = new ArrayList<String>();
			for (ConstraintViolation<Contact> violation :violations) {
				validationMessages.add(violation.getPropertyPath().toString() +":" + violation.getMessage());
			}
			return Response
					.status(Status.BAD_REQUEST)
					.entity(validationMessages)
					.build();
		}
		else {
			// No errors
			// update the contact with the provided ID
			contactDao.updateContact(id, contact.getFirstName(),contact.getLastName(), contact.getPhone());
			return Response.ok(
					new Contact(id, contact.getFirstName(),
							contact.getLastName(),
							contact.getPhone())).build();
		}
	}

}