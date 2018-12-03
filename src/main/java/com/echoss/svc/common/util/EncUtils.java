package com.echoss.svc.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncUtils {

	public static String getEncrypt(String source, String salt) {
		String result = "";

		try {
			byte[] a = source.getBytes();
			byte[] b = salt.getBytes();

			byte[] bytes = new byte[a.length + b.length];

			System.arraycopy(a, 0, bytes, 0, a.length);
			System.arraycopy(b, 0, bytes, a.length, b.length);

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(bytes);

			byte[] byteData = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; ++i) {
				sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
			}

			result = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return result;
	}
}
