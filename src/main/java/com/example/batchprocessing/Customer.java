package com.example.batchprocessing;

import org.springframework.batch.item.ItemProcessor;

/**
 * Author: Charles Hoan Duong
 */

public class Customer {

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	Integer id;
	String name;

}
