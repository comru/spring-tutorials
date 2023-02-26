package com.sbu.dj;

import com.sbu.dj.domain.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DataJdbcApplicationTests {

	@Autowired
	private OwnerRepository ownerRepository;
	@Autowired
	private DogRepository dogRepository;

	@BeforeEach
	void setUp() {
		Owner savedIvan = ownerRepository.save(new Owner("Ivan", new Address("Russia", "Moscow")));
		Owner savedMichael = ownerRepository.save(new Owner("Michael", new Address("USA", "Washington")));

		dogRepository.save(new Dog("Pretty", savedIvan.getOwnerId()));
		dogRepository.save(new Dog("Bella", savedIvan.getOwnerId()));
		dogRepository.save(new Dog("Buddy", savedMichael.getOwnerId()));
	}

	@AfterEach
	void tearDown() {
		dogRepository.deleteAll();
		ownerRepository.deleteAll();
	}

	@Test
	void allLoad() {
		List<Owner> all = ownerRepository.findAll();
		assertEquals(2, all.size());
	}

	@Test
	void loadToOne() {
		Dog buddy = dogRepository.findByName("Buddy");
		assertNotNull(buddy);
		Optional<Owner> buddyOwnerOp = ownerRepository.findById(buddy.getOwnerId());
		Assertions.assertTrue(buddyOwnerOp.isPresent());
		Owner buddyOwner = buddyOwnerOp.get();
		assertEquals("Michael", buddyOwner.getName());
	}

	@Test
	void loadToMany() {
		Owner owner = ownerRepository.findByName("Ivan");
		assertNotNull(owner);
		List<Dog> dogs = dogRepository.findAllByOwnerId(owner.getOwnerId());
		assertEquals(2, dogs.size());
	}

	@Test
	void beanValidation() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<Owner>> validations = validator.validate(new Owner(null, null));
		assertEquals(1, validations.size());
		ConstraintViolation<Owner> constraintViolation = validations.iterator().next();
		assertEquals("must not be null", constraintViolation.getMessage());

		validations = validator.validate(new Owner("A", null));
		assertEquals(1, validations.size());
		constraintViolation = validations.iterator().next();
		assertEquals("size must be between 2 and 2147483647", constraintViolation.getMessage());
	}
}
