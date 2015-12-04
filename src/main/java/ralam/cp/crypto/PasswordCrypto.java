/**
 * 
 */
package ralam.cp.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

/**
 * @author ralam
 *
 */
public class PasswordCrypto {
	private static final String DEFAULT_ALG = "AES";
	private static final String DEFAULT_ENC = "UTF-8";
	private static final String DEFAULT_CIP ="AES/CBC/PKCS5PADDING";
	private static final String DEFAULT_IV = "D93EF465A223A53B";
	private static final String DEFAULT_KEY = "D3D9EF4DF96DBC5F";
	
    public static String encrypt(String privateKey, String ivVal, String value) throws CryptoException{
        try {
            IvParameterSpec iv = new IvParameterSpec(ivVal==null ? DEFAULT_IV.getBytes(DEFAULT_ENC) : ivVal.getBytes(DEFAULT_ENC));

            SecretKeySpec skeySpec = new SecretKeySpec(privateKey==null ? DEFAULT_KEY.getBytes(DEFAULT_ENC) : privateKey.getBytes(DEFAULT_ENC), DEFAULT_ALG);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIP);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            throw new CryptoException("", ex);
        }
    }

    public static String decrypt(String privateKey, String ivVal, String encrypted) throws CryptoException{
        try {
            IvParameterSpec iv = new IvParameterSpec(ivVal==null ? DEFAULT_IV.getBytes(DEFAULT_ENC) : ivVal.getBytes(DEFAULT_ENC));

            SecretKeySpec skeySpec = new SecretKeySpec(privateKey==null ? DEFAULT_KEY.getBytes(DEFAULT_ENC) : privateKey.getBytes(DEFAULT_ENC), DEFAULT_ALG);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIP);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            throw new CryptoException("", ex);
        }
    }
    
    public static PrivateKey loadPrivateKey(String fileName) 
            throws IOException, GeneralSecurityException {
        PrivateKey key = null;
        InputStream is = null;

            is = fileName.getClass().getResourceAsStream("/" + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            boolean inKey = false;
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (!inKey) {
                    if (line.startsWith("-----BEGIN ") && 
                            line.endsWith(" PRIVATE KEY-----")) {
                        inKey = true;
                    }
                    continue;
                }
                else {
                    if (line.startsWith("-----END ") && 
                            line.endsWith(" PRIVATE KEY-----")) {
                        inKey = false;
                        break;
                    }
                    builder.append(line);
                }
            }
            //
            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePrivate(keySpec);
        
        return key;
    }

 
}
