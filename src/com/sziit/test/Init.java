package com.sziit.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServlet;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.sziit.po.AccessToken;
import com.sziit.util.WeixinUtil;

import net.sf.json.JSONObject;

public class Init extends HttpServlet{
	public void Init(){
		//读取本地收否已经存了
		String lineTxt = null;
		String text = null;
        try {
            String encoding="UTF-8";
            String filePath = "D:\\access_token.txt";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                while((lineTxt = bufferedReader.readLine()) != null){
                	text = lineTxt;
                }
                if(text == null){
                	AccessToken token = WeixinUtil.getAccessToken();
                	text = token.getToken();
                }
                read.close();
		    }else{
		        System.out.println("找不到指定的文件");
		    }
	    } catch (Exception e) {
	        System.out.println("读取文件内容出错");
	        e.printStackTrace();
	    }
		String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		try {
			int result = WeixinUtil.createMenu(text, menu);
			if(result == 0){
				System.out.println("创建成功" + text);
			}else{
				System.out.println(result);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

