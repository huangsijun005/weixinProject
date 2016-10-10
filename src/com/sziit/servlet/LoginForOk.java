package com.sziit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.sziit.dao.InsertAndSelect;

public class LoginForOk extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("userName");  //用户id
		String fromUserName = req.getParameter("odname");//关注者的openid
		boolean insertRuslt = false;
		try {
			insertRuslt =  InsertAndSelect.insert(username,fromUserName);
		}  catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(insertRuslt);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doGet(req, resp);
	}
}
