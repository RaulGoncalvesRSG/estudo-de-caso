package com.raul.demo.services.exceptions;

//Exceção utilizada qnd houver tentativa de apagar um objeto q está associado com outro
public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
