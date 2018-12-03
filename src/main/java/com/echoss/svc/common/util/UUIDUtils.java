package com.echoss.svc.common.util;

import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UUIDUtils {
	static public String generateUUID() {
		return UUID.randomUUID().toString();
	}

	static public String generateSecret() {
//		KeyGenerator generator = KeyGenerator.getInstance("AES");
//		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//		generator.init(256, random); /* 256-bit AES */
//		SecretKey secretKey = generator.generateKey();

		PBEKeySpec keySpec = new PBEKeySpec(UUIDUtils.generateUUID().toCharArray());
		SecretKeyFactory keyFactory = null;
		try {
			keyFactory = SecretKeyFactory.getInstance("PBE");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		Key secretKey;
		try {
			secretKey = keyFactory.generateSecret( keySpec );
		} catch (InvalidKeySpecException e) {
			return null;
		}

		byte[] binary = secretKey.getEncoded();
		String secret = String.format("%032X", new BigInteger(+1, binary));

		return secret;
	}

	/**
	 * 랜덤숫자를 생성한다.
	 * @param length
	 * @return
	 */
	static public String createRandomNumber(int length) {
		Random rd = new Random();

		StringBuffer sb = new StringBuffer();

		for(int i = 0; i < length; i++) {
			sb.append(rd.nextInt(10));
		}

		return sb.toString();
	}

	public static void main(String[] args) {

		for (int i = 0; i < 1; i++) {
			System.out.println(UUIDUtils.generateUUID());
			System.out.println(UUIDUtils.generateSecret());
		}
		
	}
}
