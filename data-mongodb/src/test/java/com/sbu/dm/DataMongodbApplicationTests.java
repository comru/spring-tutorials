package com.sbu.dm;

import com.sbu.dm.domain.Account;
import com.sbu.dm.domain.Address;
import com.sbu.dm.domain.Person;
import com.sbu.dm.domain.Tag;
import com.sbu.dm.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;

import java.io.ObjectInputFilter;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DataMongodbApplicationTests {

	@Autowired
	private PersonRepository personRepository;

	@BeforeEach
	void setUp() {
		Account account = new Account().setTotal(5F).setTags(List.of(
				new Tag().setName("Project").setStatus(ObjectInputFilter.Status.ALLOWED),
				new Tag().setName("Topic").setStatus(ObjectInputFilter.Status.UNDECIDED).setType("Help")
		));

		personRepository.save(new Person()
				.setSsn(1)
				.setFirstName("Ivan")
				.setLastName("Ivanov")
				.setBornDate(LocalDate.of(1990, Month.AUGUST, 1))
				.setAccounts(List.of(account))
				.setAddress(new Address("Russia", "Moscow"))
				.setUserData(Map.of("key1", "value1", "key2", "value2"))
				.setRoles(List.of(1, 2, 3)));
	}

	@AfterEach
	void tearDown() {
		personRepository.deleteAll();
	}

	@Test
	void contextLoads() {
		List<Person> persons = personRepository.findAll();
		Assertions.assertEquals(1, persons.size());

		Person person = persons.get(0);
		Assertions.assertEquals("Ivan", person.getFirstName());
		Assertions.assertEquals("Ivanov", person.getLastName());

		List<Account> personAccounts = person.getAccounts();

		Assertions.assertEquals(1, personAccounts.size());
		Assertions.assertNull(personAccounts.get(0));
		Assertions.assertTrue(personAccounts instanceof LazyLoadingProxy);
	}
}
