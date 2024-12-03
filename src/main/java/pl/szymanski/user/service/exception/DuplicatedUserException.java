package pl.szymanski.user.service.exception;

public class DuplicatedUserException extends RuntimeException {

	public DuplicatedUserException(String message) {
		super(message);
	}
}
