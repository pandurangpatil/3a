package com.agnie.useradmin.service.client;

import javax.ws.rs.core.Response.Status;

import com.agnie.useradmin.service.client.entity.Error;

/**
 * @author Pandurang Patil 08-Feb-2014
 * 
 */
public class ServerFault extends RuntimeException {

	private static final long	serialVersionUID	= 1L;

	private Status				status;
	private Error				error;

	public ServerFault(Status status, Error error) {
		this.status = status;
		this.error = error;
	}

	public ServerFault(Status status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	@Override
	public String getMessage() {
		return error.getCode();
	}

}
