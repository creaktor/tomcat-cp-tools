package com.mozido.cp.hikari;

import static org.junit.Assert.*;

import org.junit.Test;

import ralam.cp.crypto.CryptoException;
import ralam.cp.crypto.PasswordCrypto;

public class PasswordCryptoTest {

	@Test
	public void testEncryptCustomKeys() throws CryptoException {
		String key1 = "Bar12345Bar12345"; 
		String key2 = "ThisIsASecretKet";
		assertEquals("K7RoMVpxpwTl11iFOrQV/g==", PasswordCrypto.encrypt(key1, key2, "Hello World"));
	}

	@Test
	public void testDecryptCustomKeys() throws CryptoException {
		String key1 = "Bar12345Bar12345"; 
		String key2 = "ThisIsASecretKet";
		assertEquals("Hello World", PasswordCrypto.decrypt(key1, key2, PasswordCrypto.encrypt(key1, key2, "Hello World")));
	}
	
	@Test
	public void testEncryptDefauktKeys() throws CryptoException {
		assertEquals("Bv7dbIob2AD8EPKrnC7oNg==", PasswordCrypto.encrypt(null, null, "Hello World"));
	}

	@Test
	public void testDecryptDefaultKeys() throws CryptoException {
		assertEquals("Hello World", PasswordCrypto.decrypt(null, null, PasswordCrypto.encrypt(null, null, "Hello World")));
	}
}