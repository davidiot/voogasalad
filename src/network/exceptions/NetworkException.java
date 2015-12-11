package network.exceptions;

/**
 * Documents exceptions related to Server-Client connectivity
 * @author Austin
 *
 */
public abstract class NetworkException extends Exception {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -4276026795305272738L;
	
	public NetworkException () { super(); }
	public NetworkException(String message) { super(message); }
	public NetworkException(String message, Throwable cause) { super(message, cause); }
	public NetworkException(Throwable cause) { super(cause); }
}
