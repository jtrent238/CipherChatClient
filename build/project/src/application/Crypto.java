/*
Copyright (C) 2014 Andrew Shay shayConcepts

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software") for free and 
non commercial purposes, to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright 
notice and this permission notice shall be included in all copies and substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR 
A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package application;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Controls most the encryption/decryption functions
 * @author Andrew Shay - http://shayConcepts.com
 *
 */
@SuppressWarnings("restriction")
public class Crypto {

	private static String algo = "AES";
	private static String algoExt = "AES/CBC/PKCS5Padding";
	private static byte[] aesKey = null;
	private static Key publicKey = null;
	private static Key privateKey = null;
	
	/**
	 * Encrypts the message to send to the server and other clients
	 * @param message - Message to encrypt
	 * @return encrypted message
	 */
	protected static String encryptMessage(String message, byte[] iv){
		SecretKeySpec skeySpec = new SecretKeySpec(getAesKey(), getAlgo());
        Cipher cipher;
        String encryptedMessage = null;
        try {
			cipher = Cipher.getInstance(getAlgoExt());
			IvParameterSpec spec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, spec);
			byte[] encrypted = cipher.doFinal(message.getBytes());
			BASE64Encoder bASE64Encoder = new BASE64Encoder();
	        encryptedMessage = bASE64Encoder.encodeBuffer(encrypted); 
		} 
        catch (Exception e) {
        	Methods.loadPopup("Error encrypting message", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}
  
        return encryptedMessage;
	}
	
	/**
	 * Decrypts the message
	 * @param encryptedData
	 * @return the encrypted message in plain text
	 * @throws Exception
	 */
	protected static String decryptMessage(String encryptedMessage, byte[] iv) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(getAesKey(), getAlgo());
        Cipher cipher = Cipher.getInstance(getAlgoExt());
        
        BASE64Decoder bASE64Decoder = new BASE64Decoder();
        byte decrytByt[] = bASE64Decoder.decodeBuffer(encryptedMessage);
        IvParameterSpec spec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,spec);
        
        byte decrypted[] = cipher.doFinal(decrytByt);
        
        String plainText = new String(decrypted).trim();
        
        return plainText;
    }
	
	/**
	 * Generates the RSA keys
	 */
	protected static void generateRSAKeys(){
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	    SecureRandom random = new SecureRandom();
	    KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance("RSA", "BC");
		} 
		catch (Exception e) {
			Methods.loadPopup("Failed to generate RSA keys", PopupController.ERROR, Main.getPopX(), Main.getPopY());
			e.printStackTrace();
		}

	    generator.initialize(4096, random);
	    KeyPair pair = generator.generateKeyPair();
	    setPublicKey(pair.getPublic());
	    setPrivateKey(pair.getPrivate());
	}

	/**
	 * Returns the server's public RSA key
	 * @return publicKey
	 */
	protected static Key getPublicKey() {
		return publicKey;
	}

	/**
	 * Sets the server's public RSA key
	 * @param publicKey
	 */
	private static void setPublicKey(Key publicKey) {
		Crypto.publicKey = publicKey;
	}

	/**
	 * Returns the server's private RSA key
	 * @return privateKey
	 */
	protected static Key getPrivateKey() {
		return privateKey;
	}

	/**
	 * 
	 * @param privateKey
	 */
	private static void setPrivateKey(Key privateKey) {
		Crypto.privateKey = privateKey;
	}

	/**
	 * Returns the AES key assigned by the server
	 * @return aesKey
	 */
	protected static byte[] getAesKey() {
		return aesKey;
	}

	
	/**
	 * 
	 * Sets the AES key assigned by the server
	 * @param aesKey
	 */
	protected static void setAesKey(byte[] aesKey) {
		Crypto.aesKey = aesKey;
	}
	
	/**
	 * Returns the AES algo name
	 * @return algo - The name of the algorithm to use
	 */
	private static String getAlgo() {
		return algo;
	}
	
	/**
	 * Returns a new IV in a byte array
	 * @return IV byte array
	 */
	protected static byte[] getIV(){
		SecureRandom rnd = null;
		try {
			rnd = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        final byte[] iv = new byte[16];
        rnd.nextBytes(iv);
        
        return iv;
	}
	
	/**
	 * Returns the extended version of algo
	 * @return algoExt
	 */
	protected static String getAlgoExt(){
		return algoExt;
	}
}
