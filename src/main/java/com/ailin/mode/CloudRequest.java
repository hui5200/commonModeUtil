package com.ailin.mode;

import java.io.Serializable;

/**
 * AOP处理对象
 * @author cjf
 *
 */
public class CloudRequest implements Serializable{

	private static final long serialVersionUID = 456753221234457L;
	/**
	 * 登录token
	 */
	private String token;

	/**
	 * 设备IP
	 */
	private String IP;

	/**
	 * 设备操作系统
	 */
	private String osType;

	/**
	 * 浏览器
	 */
	private String userAgent;
	
	/**
	 * 包名
	 */
	private String packageName;

	/**
	 * 访问资源地址
	 */
	private String url;
	
	/**
	 * 功能编码
	 */
	private String functionCode;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public CloudRequest() {
	}

	public CloudRequest(String token) {
		this.token = token;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}

	@Override
	public String toString() {
		return "CloudRequest{" +
				"token='" + token + '\'' +
				", IP='" + IP + '\'' +
				", osType='" + osType + '\'' +
				", userAgent='" + userAgent + '\'' +
				", packageName='" + packageName + '\'' +
				", url='" + url + '\'' +
				", functionCode='" + functionCode + '\'' +
				'}';
	}
}
