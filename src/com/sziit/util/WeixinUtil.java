package com.sziit.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sziit.menu.Button;
import com.sziit.menu.ClickButton;
import com.sziit.menu.Menu;
import com.sziit.menu.ViewButton;
import com.sziit.po.AccessToken;

/**微信工具类
 * @author Stephen
 *
 */
public class WeixinUtil {
	private static final String APPID = "wx1739e1ae1ec203fc"; //"wx071676c20816ebc6";
	private static final String APPSECRET = "a8725bcecd1abb32e1764a64f3af78dc";//"41184050da24219709168efb10e84fe1";
	//获取ACCESS_TOKEN
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//上传
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	//创建菜单 CREATE_MENU
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//查询菜单
	//private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	//删除菜单
	//private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	//网页获取授权
	//private static final String AUTH_OPENID_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
	
	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 
		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");
		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);
		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try{
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	/**
	 * 获取accessToken
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ParseException, IOException{
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
			try {
				//把最新获取到的access_token存入本地
				FileWriter fileWriter=new FileWriter("D:\\access_token.txt");
				fileWriter.write(jsonObject.getString("access_token"));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	/**
	 * 组装菜单
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		//第一列菜单
		ClickButton button11 = new ClickButton();
		button11.setName("勤工报酬");
		button11.setType("click");
		button11.setKey("11");
		ClickButton button12 = new ClickButton();
		button12.setName("岗位信息");
		button12.setType("click");
		button12.setKey("12");
		ClickButton button13 = new ClickButton();
		button13.setName("助学金");
		button13.setType("click");
		button13.setKey("13");
		ClickButton button14 = new ClickButton();
		button14.setName("评奖评优");
		button14.setType("click");
		button14.setKey("14");
		ViewButton button15 = new ViewButton();
		button15.setName("绑定学号");
		button15.setType("view");
		button15.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx1739e1ae1ec203fc&" +
				"redirect_uri=http://sziit.ngrok.natapp.cn/sziitWeixinProject/index.jsp&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect");
		
		//第二列菜单
		ClickButton button21 = new ClickButton();
		button21.setName("违纪处分");
		button21.setType("click");
		button21.setKey("21");
		ClickButton button22 = new ClickButton();
		button22.setName("宿舍违纪");
		button22.setType("click");
		button22.setKey("22");
		ClickButton button23 = new ClickButton();
		button23.setName("成绩查询");
		button23.setType("click");
		button23.setKey("23");
		ClickButton button24 = new ClickButton();
		button24.setName("一卡通");
		button24.setType("click");
		button24.setKey("24");
		ClickButton button25 = new ClickButton();
		button25.setName("课表查询");
		button25.setType("click");
		button25.setKey("25");
		
		//第三列菜单
		ClickButton button31 = new ClickButton();
		button31.setName("学分查询");
		button31.setType("click");
		button31.setKey("31");
		ClickButton button32 = new ClickButton();
		button32.setName("公选课查询");
		button32.setType("click");
		button32.setKey("32");
		ClickButton button33 = new ClickButton();
		button33.setName("补考查询");
		button33.setType("click");
		button33.setKey("33");
		ClickButton button34 = new ClickButton();
		button34.setName("重修查询");
		button34.setType("click");
		button34.setKey("34");
		ClickButton button35 = new ClickButton();
		button35.setName("失物招领查询");
		button35.setType("click");
		button35.setKey("35");
		
		Button button1 = new Button();
		button1.setName("菜单一");
		button1.setSub_button(new Button[]{button15,button11,button12,button13,button14});
		
		Button button2 = new Button();
		button2.setName("菜单二");
		button2.setSub_button(new Button[]{button21,button22,button23,button24,button25});
		
		Button button3 = new Button();
		button3.setName("菜单三");
		button3.setSub_button(new Button[]{button31,button32,button33,button34,button35});
		
		menu.setButton(new Button[]{button1,button2,button3});
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws ParseException, IOException{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
			if(result == 42001){
				AccessToken new_token = WeixinUtil.getAccessToken();
				url = CREATE_MENU_URL.replace("ACCESS_TOKEN",new_token.getToken());
				jsonObject = doPostStr(url, menu);
			}
		}
		return result;
	}
	
	
	
		
}
