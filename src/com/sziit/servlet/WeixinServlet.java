package com.sziit.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.sziit.dao.InsertAndSelect;
import com.sziit.po.TextMessage;
import com.sziit.util.CheckUtil;
import com.sziit.util.MessageUtil;

public class WeixinServlet extends HttpServlet {
	/**
	 * 接入验证
	 */
	private static final long serialVersionUID = 1L;
	InsertAndSelect insertAndSelect = new InsertAndSelect();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
	}
	/**
	 * 消息的接收与响应
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");  //openid
			String toUserName = map.get("ToUserName");	//公众号
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if("?".equals(content) || "?".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else{
					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType("text");
					text.setCreateTime(new Date().getTime());
					text.setContent("您输入的内容是：" + content);
					message = MessageUtil.textMessageToXml(text);
				}
			}else if(MessageUtil.MESSAGE_EVNET.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){//关注公众号的推送
					//参数信息    公众号，openid，文本消息主题
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){//点击菜单点击事件  click
					//每个点击事件的key值，key值唯一  
					String key = map.get("EventKey");
					//根据key值，调相应的sql查询相应的数据返回
					//存放查询数据，并且已经拼接好了
					String contentSelect = insertAndSelect.keyForSelect(key,fromUserName);
					if(contentSelect == null || "".equals(contentSelect)){
						contentSelect = "请先绑定学号！！！";
					}else if("勤工信息:".equals(contentSelect)||("助学金信息:").endsWith(contentSelect)||("评奖评优信息:").equals(contentSelect)||
							"违纪处分:".equals(contentSelect)||"本学年岗位信息:".equals(contentSelect)||"宿舍违纪:".equals(contentSelect)||"失物招领:".equals(contentSelect)||
							"一卡通信息:".equals(contentSelect)||"课表查询:".equals(contentSelect)||"学分查询:".equals(contentSelect)||"公选课查询:".equals(contentSelect)||
							"补考查询:".equals(contentSelect)||"重修查询:".equals(contentSelect)){
						contentSelect = contentSelect + " 无";
					}
					message = MessageUtil.initText(toUserName, fromUserName, contentSelect);
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){ //菜单   view
					String url = map.get("EventKey")+"?id="+fromUserName;
					message = MessageUtil.initText(toUserName, fromUserName, url);
					
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
					message = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){  //地理位置信息
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
	}
	
}
