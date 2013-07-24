package welfare.security;

public class AuthicationException extends Exception {
	public AuthicationException(Throwable cause){
		super(cause);
	}
	
	public AuthicationException(String message) {
		super(message);
	}
}
