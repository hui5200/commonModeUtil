package com.userJoin.util;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import java.net.URL;

public class UIASInterfaceTestDemo {

	 public static void main(String args[]) throws Exception {   
	 	System.out.println("客户端调用查询业务开始");
		 URL url = Thread.currentThread().getContextClassLoader().getResource("hbyzwtest.pfx");
		 String srtStr = url.getPath();
		 System.setProperty("javax.net.ssl.keyStoreType","pkcs12");
		 //测试证书
		 System.setProperty("javax.net.ssl.keyStore",srtStr);
		 System.setProperty("javax.net.ssl.keyStorePassword","111111");
	//	 System.setProperty("javax.net.ssl.trustStoreType","jks");
	//	 System.setProperty("javax.net.ssl.trustStore","d:/jks/127.0.0.1.jks");
	//	 System.setProperty("javax.net.ssl.trustStorePassword","11111111");
		 //测试地址
		 String endpointAddress = "https://202.110.133.69:9092/hbyzw/cxf/Users";

		 Service service = new Service();
		 //方法名
		 String serviceName = "loginUsingActive1Data";
		 Call call = (Call) service.createCall();
		 call.setTargetEndpointAddress(new java.net.URL(endpointAddress) );
		 call.setOperationName(new QName("http://service.hoo.com/",serviceName));
		 call.addParameter("json", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
		 call.setReturnType(org.apache.axis.Constants.XSD_STRING);
		 //密码登陆
		 String result = (String) call.invoke(new Object[] {"{\"userName\":\"20000000000\",\"password\":\"Admin123\",\"type\":\"1\"}"} );

		 serviceName = "findUserByUserid";
		 call.setOperationName(new QName("http://service.hoo.com/",serviceName));
		 //获取用户信息
		 result = (String) call.invoke(new Object[] {"{\"userid\":\"86451e0bd80f7b93deaec3d97925ddd1\"}"} );


		 System.out.println(result);
	 }

}
