package br.com.comprasa2.apicore.exception;

public class DuplicatedException extends Exception{

	private static final long serialVersionUID = -1904589210423311918L;

	public DuplicatedException(String message) {
		super(message);
	}
	
}
