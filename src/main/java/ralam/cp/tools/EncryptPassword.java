/**
 * 
 */
package ralam.cp.tools;

import ralam.cp.crypto.CryptoException;
import ralam.cp.crypto.PasswordCrypto;

/**
 * @author ralam
 *
 */
public class EncryptPassword {

	/**
	 * @param args
	 * @throws CryptoException, Exception 
	 */
	public static void main(String[] args) throws CryptoException, Exception {
		if(args.length == 0 || args[0] == null) throw new Exception("Must provide password to encrypt!");
		else if(args.length == 1){
			System.out.println("Password ["+args[0]+"] encrypted [Default KEY and IV] value --> "+PasswordCrypto.encrypt(null, null, args[0]));
		}else if(args.length == 3){
			System.out.println("Password ["+args[0]+"] encrypted [KEY:"+args[1]+"& IV: "+args[2]+"] value --> "+PasswordCrypto.encrypt(args[1], args[2], args[0]));
		}else{
			throw new Exception("Input parameters must be of the form: <password> <key> <iv> where <key> and <iv> are optional.");
		}
	}
}
