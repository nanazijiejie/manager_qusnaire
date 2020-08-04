package com.ktkj.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 */
public class SecurityUtil {
	//换行符
	public static final String ENTER = "(\r\n|\r|\n|\n\r)";
	
	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch(Exception e) {
            return "";
        }

        byte[] byteArray = text.getBytes();
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for(int i=0;i<md5Bytes.length;i++) {
            int val = ((int)md5Bytes[i]) & 0xff;
            if(val < 16) hexValue.append("0");
            
            hexValue.append(Integer.toHexString(val));
        }
        
        return hexValue.toString();
	}
	
	/**
	 * SHA-1加密
	 * @param text 要加密的文本
	 * @return
	 */
	public static String sha1(String text) {
		return new SimpleHash("SHA-1", text).toString();
	}
	
	/**
	 * SHA-1加密
	 * @param text 要加密的文本
	 * @param salt
	 * @return
	 */
	public static String sha1(String text, String salt) {
		return new SimpleHash("SHA-1", text, salt).toString();
	}
	
	
	/**
	 * MD5加密
	 * @param content
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String content, String key) throws NoSuchAlgorithmException {
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		
		content = content.replaceAll(ENTER, "");
		messageDigest.update((key + content + key).getBytes());
		byte[] md = messageDigest.digest();

		int k = 0, j = md.length;
		char str[] = new char[j * 2];
		for(int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
	
	
	/**
	 * BASE64编码
	 * @param bytes
	 * @return
	 */
	public static String encode(byte[] bytes) {
		return new String(Base64.encode(bytes));
	}
	
	/**
	 * BASE64解码
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] decode(String str) throws IOException {
		return Base64.decode(str);
	}
	
	/***
     *  利用Apache的工具类实现SHA-256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }
}