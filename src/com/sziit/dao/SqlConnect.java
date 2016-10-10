package com.sziit.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;


public class SqlConnect extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn = null;
    PreparedStatement ps = null;
    /*EternaFactory factory = null;
    DataSourceManager dms = null;*/
    
    private String driverName = "oracle.jdbc.driver.OracleDriver";//加入oracle的驱动，""里面是驱动的路径
    
    //private String url = "jdbc:oracle:thin:@127.0.0.1:1522:orclDB";// 数据库连接，oracle代表链接的是oracle数据库；thin:@MyDbComputerNameOrIP代表的是数据库所在的IP地址（可以保留thin:）；1521代表链接数据库的端口号；ORCL代表的是数据库名称
    private String url = "jdbc:oracle:thin:@url:port:urpdb";
    private String UserName = "name";// 数据库用户登陆名 
    //private String UserName = "usr_test";
    private String Password = "password";// 密码
    //private String Password = "usr_test";
	public SqlConnect(){
		//连接数据库，生成连接对象
        try {
        	//System.out.println("========开始连接====1====");
            Class.forName(driverName);
            //System.out.println("========开始连接====2====");
            conn = DriverManager.getConnection(url, UserName, Password);
            //System.out.println("========连接成功========");
        } catch (ClassNotFoundException e) {
        	System.out.println("========连接失败====1====" + e);
        } catch (SQLException e) {
        	System.out.println("========连接失败====2====" + e);
        }
		
	}
	
	/*
     * 此方法用关闭数据库连接
     */
    /*private void closeConn(){
    	try {
			conn.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }*/
    
    // 释放jdbc 资源
  	public static void free(PreparedStatement ps, ResultSet rs, Connection con) {
  		try {
  			if (ps != null) {
  				ps.close();
  			}
  			if (rs != null) {
  				rs.close();
  			}
  			if (con != null) {
  				con.close();
  			}
  		} catch (Exception e) {
  			System.err.println("释放jdbc连接错误 " + e.getMessage());
  		}
  	}
	public Connection getSqlconn(){
        return conn;
    }
}
