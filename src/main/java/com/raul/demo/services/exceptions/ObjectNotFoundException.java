package com.raul.demo.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	//Throwable - Exceção q é a causa de algo q aconteceu antes
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
