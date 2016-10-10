package com.sziit.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;

public class Ajax extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String AjaxToLogin(String id, String password) throws Exception{
		String surl = "http://my.sziit.edu.cn/userPasswordValidate.portal"; 
		OutputStreamWriter out;
		URL url = new URL(surl);  
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
		connection.setDoOutput(true);
		out = new OutputStreamWriter(connection  
		        .getOutputStream(), "UTF-8");
		//其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称  
		out.write("Login.Token1=1403220321+&Login.Token2=146369&goto=http://my.sziit.edu.cn/loginSuccess.portal&gotoOnFail=http://my.sziit.edu.cn/loginFailure.portal");
		out.flush();  
		out.close();
		// 取得cookie，相当于记录了身份，供下次访问时使用  
		//String cookieVal = connection.getHeaderField("Set-Cookie");
		String okOrNot = connection.getResponseMessage();
		System.out.println(okOrNot + "-==cookieVal=");
		return okOrNot;
	}
	/**
	 * @param args
	 */
	/*public static void main(String[] args) {
        // 连接地址（通过阅读html源代码获得，即为登陆表单提交的URL）  
		String surl = "http://my.sziit.edu.cn/userPasswordValidate.portal";  
		  
		*//** 
		 * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ... 
		 *//*  
		OutputStreamWriter out;
		try {
			*//** 
			 * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using 
			 * java.net.URL and //java.net.URLConnection 
			 *//*  
			URL url = new URL(surl);  
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			*//** 
			 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。 
			 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做： 
			 *//*  
			connection.setDoOutput(true);
			out = new OutputStreamWriter(connection  
			        .getOutputStream(), "UTF-8");
			//其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称  
			out.write("Login.Token1=1403220321+&Login.Token2=146369&goto=http://my.sziit.edu.cn/loginSuccess.portal&gotoOnFail=http://my.sziit.edu.cn/loginFailure.portal"); // post的关键所在！  
			// remember to clean up  
			out.flush();  
			out.close();
			// 取得cookie，相当于记录了身份，供下次访问时使用  
			String cookieVal = connection.getHeaderField("Set-Cookie");
			String okOrNot = connection.getResponseMessage();
			System.out.println(okOrNot + "-==cookieVal=" + cookieVal);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		
		  
	}*/

}
