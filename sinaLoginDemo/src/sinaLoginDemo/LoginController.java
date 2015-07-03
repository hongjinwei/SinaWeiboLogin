package sinaLoginDemo;

import java.io.*;
import java.util.*;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.Header;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoginController implements LoginConfig {

	private SinaLoginUtil loginTool = new SinaLoginUtil();
	
	private String getUser() {
		return SINA_USER;
	}
	
	private String getPassword() {
		return SINA_PASSWORD;
	}
	
	public CookieStore login(String u, String p) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		//RegistryBuilder<CookieSpecProvider> builder = RegistryBuilder.create(); 
	
		Builder configBuilder = RequestConfig.custom();
		
		configBuilder.setConnectTimeout(CONNECTION_TIMEOUT);
		configBuilder.setSocketTimeout(SO_TIMEOUT);
		
		HttpClientBuilder clientBuilder = HttpClients.custom();
		clientBuilder.setDefaultRequestConfig(configBuilder.build());
		clientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler());
		clientBuilder.setDefaultCookieStore(cookieStore);
		
		CloseableHttpClient client = clientBuilder.build();
		
		String html = loginTool.getLoginPage(client);
		
		Document doc = Jsoup.parse(html);
		
		String postUrl = loginTool.getLoginPostUrl(doc);
		//System.out.println( "postUrl :" + postUrl + "\n");
		UrlEncodedFormEntity entity = loginTool.getUrlEncodedFormEntity(getUser(), getPassword(), doc, null);
		
		HttpPost postMethod = new HttpPost(postUrl);
		postMethod.setEntity(entity);
		try{
			CloseableHttpResponse response = client.execute(postMethod);
			
			String page = EntityUtils.toString(response.getEntity(), "utf-8");
			
			System.out.println("page :" + page);
			
			loginTool.getHeaderString(response);
			EntityUtils.consume(response.getEntity()); //consume response;
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return cookieStore;
	}
	
	public static void main(String[] args) {
		LoginController login = new LoginController();
		String user = login.getUser();
		String password = login.getPassword();
		login.login(user, password);	
	}
}
