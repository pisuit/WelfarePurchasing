/*
 * Created on 7 ¡.¤. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package welfare.security.ldap;
import javax.naming.AuthenticationException;

public class InvalidLoginException extends AuthenticationException{
		private Throwable ex;
		
		/**
		 * 
		 */
		public InvalidLoginException() {
			super();
		}

		/**
		 * @param s
		 */
		public InvalidLoginException(String s) {
			super(s);
	
		}

		public InvalidLoginException(Exception ex) {
			this.ex = ex;
		}
	
		public void printStackTrace() { 
			printStackTrace(System.err);
		}
}


