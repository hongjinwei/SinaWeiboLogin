package sinaLoginDemo;

public interface LoginConfig {
	public static final int CONNECTION_TIMEOUT = 15*1000;
	public static final int SO_TIMEOUT = 15 * 1000;
	public static final String BASEURL = "http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F%3Fs2w%3Dlogin&backTitle=%D0%C2%C0%CB%CE%A2%B2%A9&vt=4";
	public static final String POST_URL_PREFIX = "http://login.weibo.cn/login/";

	public static final String SINA_USER = "1004781778@qq.com";
	public static final String SINA_PASSWORD = "123qweasd";
}
