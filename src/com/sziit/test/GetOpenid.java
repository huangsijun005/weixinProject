package com.sziit.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GetOpenid {
    public static String  GetCodeRequest = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"; 
    

    public static String getCodeRequest() throws IOException{ 


        String result = null; 


        GetCodeRequest  = GetCodeRequest.replace("APPID", urlEnodeUTF8("wx1739e1ae1ec203fc")); 


        GetCodeRequest  = GetCodeRequest.replace("REDIRECT_URI",urlEnodeUTF8("http://sziit.ngrok.natapp.cn/sziitWeixinProject/index.html")); 


        GetCodeRequest = GetCodeRequest.replace("SCOPE", "snsapi_base"); 


        result = GetCodeRequest; 
        URL url = new URL(result);  
		HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
		connection.setDoOutput(true);
        return result; 


    } 


    public static String urlEnodeUTF8(String str){ 


        String result = str; 


        try { 


            result = URLEncoder.encode(str,"UTF-8"); 


        } catch (Exception e) { 


            e.printStackTrace(); 


        } 


        return result; 


    } 


   public static void main(String[] args) throws IOException { 
        System.out.println(getCodeRequest()); 


    }

}
