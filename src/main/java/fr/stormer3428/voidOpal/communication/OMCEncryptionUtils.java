package fr.stormer3428.voidOpal.communication;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class OMCEncryptionUtils {

	public static final class RSA{
		
		public static KeyPair generateKeyPair() {
			KeyPairGenerator generator;
			KeyPair pair;
			try { 
				generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(2048);
				pair = generator.generateKeyPair();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				System.out.println();
				return null;
			}
			return pair;
		}
		
		public static PublicKey parsePublicKey(byte[] pubKey) {
			KeyFactory keyFactory;
			EncodedKeySpec publicKeySpec;
			PublicKey publicKey;
			try { 
				keyFactory = KeyFactory.getInstance("RSA");
				publicKeySpec = new X509EncodedKeySpec(pubKey);
				publicKey = keyFactory.generatePublic(publicKeySpec);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
				System.out.println();
				return null;
			}
			return publicKey;
		}
		
		public static PrivateKey parsePrivateKey(byte[] privKey) {
			KeyFactory keyFactory;
			EncodedKeySpec privateKeySpec;
			PrivateKey privateKey;
			try { 
				keyFactory = KeyFactory.getInstance("RSA");
				privateKeySpec = new X509EncodedKeySpec(privKey);
				privateKey = keyFactory.generatePrivate(privateKeySpec);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				e.printStackTrace();
				System.out.println();
				return null;
			}
			return privateKey;
		}

		public static byte[] encrypt(PublicKey key, byte[] message) {
			Cipher encryptCipher;
			byte[] encryptedMessage;
			try { 
				encryptCipher = Cipher.getInstance("RSA");
				encryptCipher.init(Cipher.ENCRYPT_MODE, key);
				encryptedMessage = encryptCipher.doFinal(message);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
				System.out.println();
				return null;
			}
			return encryptedMessage;
		}
		
		public static byte[] decrypt(PrivateKey key, byte[] encrypted) {
			Cipher encryptCipher;
			byte[] decryptedMessage;
			try { 
				encryptCipher = Cipher.getInstance("RSA");
				encryptCipher.init(Cipher.DECRYPT_MODE, key);
				decryptedMessage = encryptCipher.doFinal(encrypted);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
				System.out.println();
				return null;
			}
			return decryptedMessage;
		}
		
	}

	public static final class AES{
		public static SecretKey parseKey(byte[] key) {
			SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES"); 
			return originalKey;
		}
		
		public static byte[] encrypt(String algorithm, byte[] input, SecretKey key,
				IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
		InvalidAlgorithmParameterException, InvalidKeyException,
		BadPaddingException, IllegalBlockSizeException {
			
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			byte[] cipherText = cipher.doFinal(input);
			return cipherText;
		}
		
		public static byte[] decrypt(String algorithm, byte[] cipherText, SecretKey key,
				IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
		InvalidAlgorithmParameterException, InvalidKeyException,
		BadPaddingException, IllegalBlockSizeException {
			
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] plainText = cipher.doFinal(cipherText);
			return plainText;
		}

		public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(256);
			SecretKey key = keyGenerator.generateKey();
			return key;
		}

		public static IvParameterSpec generateIv() {
			byte[] iv = new byte[16];
			new SecureRandom().nextBytes(iv);
			return new IvParameterSpec(iv);
		}

	}
}
