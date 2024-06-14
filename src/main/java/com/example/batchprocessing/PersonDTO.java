package com.example.batchprocessing;

/**
 * Author: Charles Hoan Duong
 */
public class PersonDTO {
	private Integer person_ID;
	private String name;
	private String firstName;
	private String lastName;
	private String middle;
	private String email;
	private String phone;
	private String fax;
	private String title;

	public Integer getPerson_ID() {
		return person_ID;
	}

	public void setPerson_ID(Integer person_ID) {
		this.person_ID = person_ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddle() {
		return middle;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
