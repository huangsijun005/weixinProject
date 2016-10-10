package com.sziit.test;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/*import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.sziit.po.AccessToken;
import com.sziit.util.WeixinUtil;*/

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Properties prop = new Properties();
		try {
			// 读取属性文件test.properties   配置文件要class 同一文件夹下
			InputStream in = new BufferedInputStream(Test.class.getResourceAsStream(
					"test.properties"));
			prop.load(in); 
			// 加载属性列表
			Iterator<String> it = prop.stringPropertyNames().iterator();
			while (it.hasNext()) {
				String key = it.next();
				System.out.println(key + ":" + prop.getProperty(key));
			}
			in.close();
			
			} catch (Exception e) {
				System.out.println(e);
			}

		/*
		 * try { AccessToken token = WeixinUtil.getAccessToken();
		 * System.out.println("access_token:" + token.getToken());
		 * System.out.println("过期时间:" + token.getExpiresIn()); } catch
		 * (ParseException e) { e.printStackTrace(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */

	}

}
