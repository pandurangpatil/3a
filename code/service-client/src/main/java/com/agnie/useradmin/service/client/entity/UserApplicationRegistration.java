package com.agnie.useradmin.service.client.entity;

/**
 * 
 * @author Pandurang Patil 06-Feb-2014
 * 
 */
public class UserApplicationRegistration {
	private String			id;
	private User			user;
	private RequestStatus	status;
	private Boolean			owner	= false;

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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the status
	 */
	public RequestStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	/**
	 * @return the owner
	 */
	public Boolean getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Boolean owner) {
		this.owner = owner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserApplicationRegistration [user=" + user + ", status=" + status + ", owner=" + owner + "]";
	}

}
