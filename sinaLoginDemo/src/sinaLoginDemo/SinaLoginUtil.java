package sinaLoginDemo;

import java.io.*;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.apache.http.Header;

import java.nio.charset.Charset;

import org.apache.http.message.BasicNameValuePair;

public class SinaLoginUtil implements LoginConfig {
	
	public String getLoginPage(CloseableHttpClient client) {
		String html = null;
		try{
			HttpGet getMethod = new HttpGet(BASEURL);
			CloseableHttpResponse response = client.execute(getMethod);
			html = EntityUtils.toString(response.getEntity(),"GB2312");
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	
		return html;
	}
	
	public String getLoginPostUrl(Document doc){
		String url = "";
		Elements elements = doc.getElementsByTag("form");
		Element e = elements.get(0);
		url = e.attr("action");
		return POST_URL_PREFIX +  url;
	}
	
	/**
	 *��¼��postForm�ĸ�ʽ���£�
	 *mobile:username
	 *password_6984:password
	 *remember:on
	 *backURL:http%3A%2F%2Fweibo.cn%2F%3Fs2w%3Dlogin
	 *backTitle:����΢��
	 *tryCount:
	 *vk:6984_0b94_2130061072
	 *submit:��¼
	 *@author BAO
	 */
	
	public UrlEncodedFormEntity getUrlEncodedFormEntity(String user, String password, Document doc, Charset charset) {
		
		if(charset == null || charset.equals("")) charset = Charset.defaultCharset();
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		Elements elements = doc.getElementsByTag("input");
		String mobileName = "mobile";
		String mobileValue = user;
		
		String passwordName = elements.get(1).attr("name");
		String passwordValue = password;
		
		String rememberName = "remember";
		String rememberValue = "on";
		
		String backURLName = "backURL";
		String backURLValue = elements.get(3).attr("value");
		
		String backTitleName = elements.get(4).attr("name");
		String backTitleValue = elements.get(4).attr("value");
		
		String tryCountName = "tryCount";
		String tryCountValue = elements.get(5).attr("value");
		
		String vkName = "vk";
		String vkValue = elements.get(6).attr("value");
		
		String submitNmae = "submit";
		String submitValue = "��¼";
		
		/*
		System.out.println(mobileName + ": " + mobileValue);
		System.out.println(passwordName + ": " + passwordValue);
		System.out.println(rememberName + ": " + rememberValue);
		System.out.println(backURLName + ": " + backURLValue);
		System.out.println(backTitleName + ": " + backTitleValue);
		System.out.println(tryCountName + ": " + tryCountValue);
		System.out.println(vkName + ": " + vkValue);
		System.out.println(submitNmae + ": " + submitValue);
		*/
		
		list.add(new BasicNameValuePair(mobileName, mobileValue));
		list.add(new BasicNameValuePair(passwordName, passwordValue));
		list.add(new BasicNameValuePair(rememberName, rememberValue));
		list.add(new BasicNameValuePair(backURLName, backURLValue));
		list.add(new BasicNameValuePair(backTitleName, backTitleValue));
		list.add(new BasicNameValuePair(tryCountName, tryCountValue));
		list.add(new BasicNameValuePair(vkName, vkValue));
		list.add(new BasicNameValuePair(submitNmae, submitValue));
		
		return new UrlEncodedFormEntity(list, charset);
	}
	
	public String getEntityString(CloseableHttpResponse response, String encode) {
		if(encode == null || encode.equals("")) {
			encode = "utf-8";
		}
		String ans = "";
		try{
			ans = EntityUtils.toString(response.getEntity(), encode);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return ans;
	}
	
	public String getHeaderString(CloseableHttpResponse response) {		
		Header[] headers = response.getAllHeaders();
		for(int i = 0; i < headers.length; i++){
			System.out.println("the " + i +"'s header : ");			
			System.out.println( headers[i].getName() + " : " + headers[i].getValue());
		}
		
		try{
			Header h = response.getLastHeader("Location");
			System.out.println(h.getValue());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
}
