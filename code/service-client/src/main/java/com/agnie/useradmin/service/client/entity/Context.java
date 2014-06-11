package com.agnie.useradmin.service.client.entity;

/**
 * 
 * @author Pandurang Patil 08-Feb-2014
 * 
 */
public class Context {
	private String	id;
	private String	name;
	private String	description;

	public Context() {
	}

	public Context(String name, String description) {
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Context [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
