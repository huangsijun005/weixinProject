package com.sziit.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class InsertAndSelect {
	/**
	 * 绑定插入用户名和openid
	 * @param uersName
	 * @param openId
	 * @return
	 */
	public static boolean insert(String uersName, String openId) throws SQLException{
		boolean haveOrNot = select(openId);
		//openid 存在就不执行插入，否则就执行插入
		if(haveOrNot){
			PreparedStatement pstm = null;
			ResultSet rs=null;
			Connection conn=null;
			try {
				//连接数据库
				SqlConnect dbutil=new SqlConnect();   
				conn=dbutil.getSqlconn();
				//插入数据
				pstm  = conn.prepareStatement("INSERT INTO T_SZIIT_WX (USERNAME,OPENID) VALUES (?,?)");
				pstm.setString(1,uersName);
		        pstm.setString(2,openId);
		        pstm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				conn.rollback();  //报错，数据回滚
				return false;
			}finally{
				try {
	                if(null != conn){
	                    conn.commit(); //提交
	                }
	            } catch (SQLException e) {
	            	conn.rollback();  //报错，数据回滚
	            }
	            SqlConnect.free(pstm, rs, conn);
			}
			return true;
		}else{
			return true;
		}
	}
	public static boolean delete(String openid){
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  Connection conn = null;
		  int i = 0;
		  String sql = " delete from t_sziit_wx t where t.openid = ? ";
		  try{
			  SqlConnect dbutil=new SqlConnect();
			  conn=dbutil.getSqlconn();
			  ps=conn.prepareStatement(sql);
			  ps.setString(1,openid);
			  i = ps.executeUpdate();
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  SqlConnect.free(ps, rs, conn);
			  if(i > 0){
				  return true;
			  }
		  }
		  return false;
	}
	/**
	 * 判断是否绑定
	 * @param openid
	 * @return
	 */
	public static boolean select(String openid){
		  PreparedStatement ps=null;
		  ResultSet rs=null;
		  Connection conn=null;
		  int num = 1;
		  String sql="SELECT count(OPENID) AS NUM FROM T_SZIIT_WX T WHERE T.OPENID =? ";
		  try{
			  SqlConnect dbutil=new SqlConnect();
			  conn=dbutil.getSqlconn();
			  ps=conn.prepareStatement(sql);
			  ps.setString(1,openid);
			  rs=ps.executeQuery();
			  while(rs.next()){
				  num = rs.getInt("NUM");
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  SqlConnect.free(ps, rs, conn);
			  if(num == 0){
				  return true;
			  }
		  }
		  return false;
		
	}
	
	
	/**
	 * 应用的数据查询及拼接
	 * @param key
	 * @param openid
	 * @return
	 */
	public String keyForSelect(String key, String openid){
		/*Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);		//当前年
		int month = cal.get(Calendar.MONTH )+1;	//当前月*/
		
		//当前年月日
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);
		
		//存放消息正文信息
		StringBuffer contentSB = new StringBuffer();
		//数据库连接
		PreparedStatement ps=null;
		ResultSet rs=null;
		Connection conn=null;
		//查询sql
		String sql = null;
		//根据openid查询学号
		String sql_1 = "SELECT T.USERNAME AS XH FROM T_SZIIT_WX T WHERE T.OPENID =? ";
		String xh = null;
		try{
			  SqlConnect dbutil=new SqlConnect();
			  conn=dbutil.getSqlconn();
			  ps=conn.prepareStatement(sql_1);
			  ps.setString(1,openid);
			  rs=ps.executeQuery();
			  while(rs.next()){
				  xh = rs.getString("XH");
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  try {
				ps.close();
				rs.close();
			  } catch (SQLException e) {
				e.printStackTrace();
			  }
			  if(xh == null){
				  return xh;
			  }
		  }
		if(key.equals("11")){ //勤工报酬
			sql = " select t.mc as mc,t.gwzz as gwzz,t.nf as nf,t.yf as yf,t.gs as gs,t.bcze as ze from v_bzks_qgbc t where t.xsbh = ? ";
			try{
				  ps=conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  rs=ps.executeQuery();
				  contentSB.append("勤工信息:");
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append("岗位：" + rs.getString(1) + "\n");
					  contentSB.append("岗位职责：" + rs.getString(2) + "\n");
					  contentSB.append("日期：" + rs.getString(3) + "年"+ rs.getString(4) +"\n");
					  contentSB.append("工时：" + rs.getString(5) + "\n");
					  contentSB.append("金额：" + rs.getString(6));
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
			  }
		}else if(key.equals("12")){ //岗位信息
			sql = "select t.gwmc as gwmc,t.dwmc as dwmc,t.gwrs as gwrs,t.sgkssj as kssj,t.sgjssj as jssj from v_bzks_gwxx t"  
				+ " where t.sgjssj >= to_date('"+dateNowStr+"','YYYY-MM-DD')";
			contentSB.append("本学年岗位信息:");
			try{
				  ps=conn.prepareStatement(sql);
				  rs=ps.executeQuery();
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append("岗位名称：" + rs.getString(1) + "\n");
					  contentSB.append("单位名称：" + rs.getString(2) + "\n");
					  contentSB.append("岗位人数：" + rs.getString(3) + "\n");
					  contentSB.append("开始时间：" + rs.getString(4) + "\n");
					  contentSB.append("结束时间：" + rs.getString(5));
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
			  }
		}else if(key.equals("13")){	//助学金
			sql= "select t.xn as xn,t.zxjmc as zxjmc,t.je as je from v_bzks_zxj t where t.xsbh = ?";
			try{
				  ps = conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  rs = ps.executeQuery();
				  contentSB.append("助学金信息:");
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append(rs.getString(1) + "\n");
					  contentSB.append("助学金名称：" + rs.getString(2) + "\n");
					  contentSB.append("金额：" + rs.getString(3));
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
			  }
		}else if(key.equals("14")){	//评奖评优
			PreparedStatement ps_1=null;
			ResultSet rs_1=null;
			sql= "select t.xn as xn,t.jxjmc as jxjmc,t.jxjdjmc as djmc,t.je as je from v_bzks_jxj t where t.xsbh = ?";
			String sql_2 = "select t.xn as xn,t.rychmc as rych from v_bzks_rych t where t.xsbh = ?";
			try{
				  ps = conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  rs = ps.executeQuery();
				  ps_1 = conn.prepareStatement(sql_2);
				  ps_1.setString(1,xh);
				  rs_1 = ps_1.executeQuery();
				  contentSB.append("评奖评优信息:");
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append(rs.getString(1) + "\n");
					  contentSB.append("奖学金名称：" + rs.getString(2) + "\n");
					  contentSB.append("奖学金等级名称：" + rs.getString(3) + "\n");
					  contentSB.append("奖学金金额：" + rs.getString(4));
				  }
				  while(rs_1.next()){
					  contentSB.append("\n");
					  contentSB.append(rs_1.getString(1) + "\n");
					  contentSB.append("荣誉称号：" + rs_1.getString(2));
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
				  SqlConnect.free(ps_1, rs_1, conn);
			  }
		}else if(key.equals("21")){	//违纪处分
			sql= "select t.wjrq as wjrq,t.wjlxmc as wjmc,t.cfrq as cfrq,t.cflxmc as cflxmc from v_bzks_wjcf t where t.xsbh = ?";
			try{
				  ps=conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  rs=ps.executeQuery();
				  contentSB.append("违纪处分:");
				  if(rs.next()){
					  while(rs.next()){
						  contentSB.append("\n");
						  contentSB.append("因" + rs.getString(1));
						  contentSB.append(rs.getString(2) + "，");
						  contentSB.append("于" + rs.getString(3) + "给予");
						  contentSB.append(rs.getString(4) + "处分.");
					  }
				  }else{
					  contentSB.append("无");
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
			  }
		}else if(key.equals("22")){	//宿舍违纪
			sql= "select t.wjlb as wjlb,t.fsrq as fsrq,t.qksm as qksm from v_bzks_sswj t where t.xsbh = ?";
			try{
				  ps=conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  rs=ps.executeQuery();
				  contentSB.append("宿舍违纪:");
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append("违纪类别:" + rs.getString(1) + "\n" );
					  contentSB.append("违纪日期:" + rs.getString(2) + "\n" );
					  contentSB.append("详细：" + rs.getString(3));
				  }
			  }catch(Exception e){
				  e.printStackTrace();
			  }finally{
				  SqlConnect.free(ps, rs, conn);
			  }
		}else if(key.equals("23")){	//成绩查询
			//String xn = null;
			/*if(month >= 8){
				xn = (year-1) + "-" +year;
				sql= "SELECT KCMC,KCLX,ZPCJ,XF FROM V_BZKS_KSCJ WHERE XN=? AND XQDM='2' AND XH=? ";
			}else{
				xn = (year-1) + "-" +year;
				sql= "SELECT KCMC,KCLX,ZPCJ,XF FROM V_BZKS_KSCJ WHERE XN=? AND XQDM='1' AND XH=? ";
			}*/
			sql = " select t.kcmc as kcmc, t.kclx as kclx, t.zpcj as zpcj, t.xf as xf "
				+ " from v_bzks_kscj t where t.xn = (select t11.xn as xn "
                + " from (select distinct (t1.xn) from v_bzks_kscj t1 "
                + " where t1.xh = ? order by t1.xn desc) t11 where rownum = 1) "
                + " and t.xqdm = (select t32.xqdm as xqdm "
                + " from (select distinct (t2.xqdm) from v_bzks_kscj t2 where t2.xh = ? "
                + " and t2.xn = (select t31.xn as xn from (select distinct (t3.xn) "
                + " from v_bzks_kscj t3  where t3.xh = ? order by t3.xn desc) t31 "
                + " where rownum = 1) order by t2.xqdm desc) t32 where rownum = 1) "
                + " and t.xh = ? ";
			try{
				  ps=conn.prepareStatement(sql);
				  ps.setString(1,xh);
				  ps.setString(2,xh);
				  ps.setString(3,xh);
				  ps.setString(4,xh);
				  rs=ps.executeQuery();
				  contentSB.append("本学年成绩:\n");
				  while(rs.next()){
					  contentSB.append("课程：" + rs.getString(1) + "\n");
					  contentSB.append("类型：" + rs.getString(2) + "\n");
					  contentSB.append("成绩：" + rs.getString(3) + "\n");
					  contentSB.append("学分：" + rs.getString(4) + "\n");
				  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  SqlConnect.free(ps, rs, conn);
		  }
		}else if(key.equals("24")){	//一卡通
			sql= "";
		}else if(key.equals("25")){	//课表查询
			sql= "";
		}else if(key.equals("31")){	//学分查询
			sql= "";
		}else if(key.equals("32")){	//公选课查询
			sql= "";
		}else if(key.equals("33")){	//补考查询
			sql= "";
		}else if(key.equals("34")){	//重修查询
			sql= "";
		}else if(key.equals("35")){	//失物招领查询
			sql= "select t.wpms as wpms,t.sqsj as sqsj,t.sqdd as sqdd,t.klqsj as klqsj,t.lqdd as lqdd " +
					" from v_bzks_swzl t where rownum <=10 order by t.id desc";
			try{
				  ps=conn.prepareStatement(sql);
				  rs=ps.executeQuery();
				  contentSB.append("失物招领:");
				  while(rs.next()){
					  contentSB.append("\n");
					  contentSB.append("失物明细：" + rs.getString(1) + "\n");
					  contentSB.append("拾取地点：" + rs.getString(2) + "\n");
					  contentSB.append("拾取时间：" + rs.getString(3) + "\n");
					  contentSB.append("领取时间：" + rs.getString(4) + "\n");
					  contentSB.append("领取地点：" + rs.getString(5));
				  }
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				SqlConnect.free(ps, rs, conn);
			}
		}
		return contentSB.toString();
	}

}
