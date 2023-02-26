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
		Account account = new Account().total(5F).tags(List.of(
				new Tag().name("Project").status(ObjectInputFilter.Status.ALLOWED),
				new Tag().name("Topic").status(ObjectInputFilter.Status.UNDECIDED).type("Help")
		));

		personRepository.save(new Person()
				.ssn(1)
				.firstName("Ivan")
				.lastName("Ivanov")
				.bornDate(LocalDate.of(1990, Month.AUGUST, 1))
				.accounts(List.of(account))
				.address(new Address("Russia", "Moscow"))
				.userData(Map.of("key1", "value1", "key2", "value2"))
				.roles(List.of(1, 2, 3)));
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
		Assertions.assertEquals("Ivan", person.firstName());
		Assertions.assertEquals("Ivanov", person.lastName());

		List<Account> personAccounts = person.accounts();

		Assertions.assertEquals(1, personAccounts.size());
		Assertions.assertNull(personAccounts.get(0));
		Assertions.assertTrue(personAccounts instanceof LazyLoadingProxy);
	}
}
