package com.echoss.svc.common.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

	private static String defaultKey = "d6h)9ft3ro$f%1jk";

	public static String encrypt(String org) throws Exception {
		return encrypt(org, defaultKey);
	}

	public static String encrypt(String org, String key) throws Exception {
		String sKey = makeKey(key);
		Key keySpec = new SecretKeySpec(sKey.getBytes(), "AES");
		
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);

		byte[] e=cipher.doFinal(org.getBytes());
		return asHex(e);
	}

	public static String decrypt(String org, String key) throws Exception {
		String sKey = makeKey(key);
		Key keySpec = new SecretKeySpec(sKey.getBytes(), "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);

		byte[] e=cipher.doFinal(hexToByteArray(org));

		return new String(e);
	}

	
	public static String asHex (byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");
				strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public static String makeKey(String keyStr) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<16; i++) {
			if(keyStr.length() > i) sb.append(keyStr.substring(i,i+1));
			else sb.append(" ");
		}
		return sb.toString();
	}
	
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0)  return null;
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}
}
