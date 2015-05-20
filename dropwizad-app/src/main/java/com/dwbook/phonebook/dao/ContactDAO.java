package com.dwbook.phonebook.dao;

import org.skife.jdbi.v2.sqlobject.*;
import com.dwbook.phonebook.representations.Contact;
import com.dwbook.phonebook.dao.mappers.ContactMapper;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface ContactDAO {

	@Mapper(ContactMapper.class)
	@SqlQuery("SELECT * FROM contact WHERE id = :id")
	Contact getContactById(@Bind("id") int id);

	@GetGeneratedKeys
	@SqlUpdate("INSERT INTO contact (id,firstName,lastName,phone) values (NULL,:firstName,:lastName,:phone)")
	int createContact(@Bind("firstName") String firstName,@Bind("lastName") String lastName,@Bind("phone")String phone);
	
	@SqlUpdate("UPDATE contact SET firstName=:firstName,lastName=:lastName,phone=:phone WHERE id =:id")
	void updateContact(@Bind("id") int id,@Bind("firstName") String firstName,@Bind("lastName") String lastName,@Bind("phone") String phone);
	
	@SqlUpdate("delete from contact where id = :id")
	void deleteContact(@Bind("id") int id);
	


}