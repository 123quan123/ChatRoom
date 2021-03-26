package com.me.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

public class RSAUtil {
	private static Encoder encoder = Base64.getEncoder();
	private static Decoder decoder = Base64.getDecoder();
	private static Map<Integer, String> keyMap = new HashMap<Integer, String>();
	
	public static void getKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048, new SecureRandom());
		KeyPair keyPair = kpg.generateKeyPair();
		
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		
		byte[] pukString = encoder.encode(publicKey.getEncoded());
		byte[] prkString = encoder.encode(privateKey.getEncoded());
		
		keyMap.put(0, ByteString.bytesToString(pukString));
		keyMap.put(1, ByteString.bytesToString(prkString));
	}
	
	/**
	 * 公钥加密
	 * @param str 加密字符串
	 * @param publicKey 公钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String puEncrypt(String str, String publicKey) throws Exception {
		//base64编码的公钥
		byte[] puk = decoder.decode(publicKey);
		RSAPublicKey pubkey = (RSAPublicKey)KeyFactory.getInstance("RSA")
		.generatePublic(new X509EncodedKeySpec(puk));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubkey);
		String outString = encoder.encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outString;
	}
	
	/**
	 * 私钥加密
	 * @param str 加密字符串
	 * @param publicKey 公钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String prEncrypt(String str, String privateKey) throws Exception {
		//base64编码的公钥
		byte[] prk = decoder.decode(privateKey);
		RSAPrivateKey prkey = (RSAPrivateKey)KeyFactory.getInstance("RSA")
		.generatePrivate(new PKCS8EncodedKeySpec(prk));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, prkey);
		String outString = encoder.encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outString;
	}
	
	/**
	 * 私钥解密
	 * @param str 加密字符串
	 * @param privateKey 私钥
	 * @return 明文
	 * @throws Exception
	 */
	public static String prDecrypt(String str, String privateKey) throws Exception {
		
		//64位编码加密后的字符串
		byte[] inputByte = decoder.decode(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] prk = decoder.decode(privateKey);
		RSAPrivateKey prkey = (RSAPrivateKey)KeyFactory.getInstance("RSA")
		.generatePrivate(new PKCS8EncodedKeySpec(prk));
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prkey);
		String outString = new String(cipher.doFinal(inputByte));
		return outString;
	}
	
	/**
	 * 公钥解密
	 * @param str 加密字符串
	 * @param privateKey 私钥
	 * @return 明文
	 * @throws Exception
	 */
	public static String puDecrypt(String str, String publicKey) throws Exception {
		
		//64位编码加密后的字符串
		byte[] inputByte = decoder.decode(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] puk = decoder.decode(publicKey);
		RSAPublicKey pukey = (RSAPublicKey)KeyFactory.getInstance("RSA")
		.generatePublic(new X509EncodedKeySpec(puk));
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, pukey);
		String outString = new String(cipher.doFinal(inputByte));
		return outString;
	}
	
	public static String getPublicKey() {
		return keyMap.get(0);
	}
	
	public static String getprivateKey() {
		return keyMap.get(1);
	}
	
	public static void main(String[] args) {
		try {
			//私钥加密 公钥解密
//			getKeyPair();
//			System.out.println("公钥 ： " + getPublicKey());
//			System.out.println("私钥 ： " + getprivateKey());
//			
//			String encode = Base64Coded.encode("你好".getBytes());
//			System.out.println("encode : " + encode);
//			String encrypt = puEncrypt(encode, getPublicKey());
//			System.out.println("encrypt : " + encrypt);
//			String decrypt = prDecrypt(encrypt, getprivateKey());
//			System.out.println("decrypt : " + decrypt);
//			String decode = Base64Coded.decode(decrypt.getBytes());
//			System.out.println("decode : " + decode);
			
			//公钥解密 私钥加密
			getKeyPair();
			System.out.println("公钥 ： " + getPublicKey());
			System.out.println("私钥 ： " + getprivateKey());
			
			String encrypt = prEncrypt("你好", getprivateKey());
			System.out.println("encrypt : " + encrypt);
			String decrypt = puDecrypt(encrypt , getPublicKey());
			System.out.println("decrypt : " + decrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
