/**
 * 
 */
package ralam.cp.crypto;

/**
 * @author ralam
 *
 */
public class CryptoException extends Exception {
	private static final long serialVersionUID = -6650558748752202782L;
	
	public CryptoException(Throwable t){
		super(t);
	}
	
	public CryptoException(String message, Throwable t){
		super(message, t);
	}
}
