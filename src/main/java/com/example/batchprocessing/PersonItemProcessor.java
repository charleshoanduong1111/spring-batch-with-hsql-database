package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

/**
 * Author: Charles Hoan Duong
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(final Person person) {
		final Integer person_ID = person.person_ID();
		final String name = person.name().toUpperCase();
		final String firstName = person.firstName().toUpperCase();
		final String lastName = person.lastName().toUpperCase();
		final String middle = person.middle().toUpperCase();
		final String email = person.email().toUpperCase();
		final String phone = person.phone().toUpperCase();
		final String fax = person.fax().toUpperCase();
		final String title = person.title().toUpperCase();

		final Person transformedPerson = new Person(person_ID, name, firstName, lastName, middle, email, phone, fax, title);

		log.info("Converting (" + person + ") into (" + transformedPerson + ")");

		return transformedPerson;
	}

}
