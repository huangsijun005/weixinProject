package com.sziit.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.sziit.dao.InsertAndSelect;

public class AjaxServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String openid = req.getParameter("od");
		boolean result = InsertAndSelect.select(openid);
		PrintWriter out = resp.getWriter();
		out.print(result);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String key = req.getParameter("key");
		PrintWriter out = resp.getWriter();
		boolean ok = false;
		if("cancel".equals(key)){
			String odname = req.getParameter("odname");
			ok = InsertAndSelect.delete(odname);
		}else if("login".equals(key)){
			String name = req.getParameter("un");
			String pass = req.getParameter("ps");
			try {
				ok = AjaxToLogin(name, pass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.print(ok);
	}
	/**
	 * 验证账号，密码正确性
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	protected boolean AjaxToLogin(String name, String pass) throws Exception{
		String surl = "http://my.sziit.edu.cn/userPasswordValidate.portal"; 
		OutputStreamWriter out;
		URL url = new URL(surl);  
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
		connection.setDoOutput(true);
		out = new OutputStreamWriter(connection  
		        .getOutputStream(), "UTF-8");
		String params = "Login.Token1="+name+"+&Login.Token2="+pass+"&goto=http://my.sziit.edu.cn/loginSuccess.portal&gotoOnFail=http://my.sziit.edu.cn/loginFailure.portal";
		//其中的memberName和password也是阅读html代码得知的，即为表单中对应的参数名称  
		out.write(params);
		out.flush();  
		out.close();
		StringBuffer bufferRes = new StringBuffer();
		InputStream inw = connection.getInputStream();
		//判断链接成功与否
		BufferedReader read = new BufferedReader(new InputStreamReader(inw,
				"UTF-8"));
		String valueString = null;
		while ((valueString = read.readLine()) != null) {
			bufferRes.append(valueString);
		}
		if(bufferRes.toString().indexOf("handleLoginSuccessed")>=0){
			return true;
		}
		// 取得cookie，相当于记录了身份，供下次访问时使用  
		//String cookieVal = connection.getHeaderField("Set-Cookie");
//		String okOrNot = connection.getResponseMessage();
//		System.out.println(okOrNot + "-==cookieVal=" + cookieVal);
		return false;
	}
}
