package com.userJoin.util;

import com.alibaba.csb.sdk.ContentBody;
import com.alibaba.csb.sdk.HttpCaller;
import com.alibaba.csb.sdk.HttpCallerException;
import com.alibaba.csb.sdk.HttpParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * CsbHttpClient请求客户端
 */
public class CsbUtil {
    private static final Logger log = LoggerFactory.getLogger(CsbUtil.class);

    private static final String API_VVERSION = "1.0.0";


    public static void main(String[] args) throws Exception{
        String access_token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZW50ZXJwcmlzZV9tb2JpbGVfcmVzb3VyY2UiLCJiZmZfYXBpX3Jlc291cmNlIl0sImV4cCI6MTYxNjA3MjkxNSwidXNlcl9uYW1lIjoiYTAyNGNkNjktMjU3NS00NTZjLWE4YzktOWJlZDE0MjFhNDVlIiwianRpIjoiM2I3ZWU2NjEtZGNkNS00ZmZiLWExNTgtZWQzNTg2ZDcyOTcwIiwiY2xpZW50X2lkIjoiMDhmODNjMmVhNTBjMmYzODBmZGUxNWQwNGMyMzhkMzY1SmFZbmVQenJyWCIsInNjb3BlIjpbInJlYWQiXX0.xBcowvIYFTevG9vfVoCUicCT2FTgvOUQBpg7sh3xjYg";
        access_token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZW50ZXJwcmlzZV9tb2JpbGVfcmVzb3VyY2UiLCJiZmZfYXBpX3Jlc291cmNlIl0sImV4cCI6MTYxNjA4OTcwMSwidXNlcl9uYW1lIjoiUXF6ZGV1TWNBYSIsImp0aSI6ImFjYzZlNzNkLWQ5NTItNGViZS1hYmE2LTdhMWNiZTZlOWRkNSIsImNsaWVudF9pZCI6IjA4ZjgzYzJlYTUwYzJmMzgwZmRlMTVkMDRjMjM4ZDM2NUphWW5lUHpyclgiLCJzY29wZSI6WyJyZWFkIl19.mMlDakbHmSMqO3M8-76ka9ParCdrSOLW8RGlKhSKS64";
        String url = "http://42.228.55.197:8086/api/bff/v1.2/mobile/digitalzz/user/details?access_token=" + access_token;
        String apiName = "user_details_csb_cas";
        String apiVersion = "1.0.0";
        String accessKey = "2a2068a4aab443f28a09f385083432ff";
        String secretKry = "pVZDjBS87JioQFTjvSjXQpiQLxA=";
        String jsonCont = "";
        String result = CsbUtil.postJson(url, apiName, apiVersion, accessKey, secretKry, jsonCont);
        System.out.println(result);


        access_token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZW50ZXJwcmlzZV9tb2JpbGVfcmVzb3VyY2UiLCJiZmZfYXBpX3Jlc291cmNlIl0sImV4cCI6MTYxNjA4OTcwMSwidXNlcl9uYW1lIjoiUXF6ZGV1TWNBYSIsImp0aSI6ImFjYzZlNzNkLWQ5NTItNGViZS1hYmE2LTdhMWNiZTZlOWRkNSIsImNsaWVudF9pZCI6IjA4ZjgzYzJlYTUwYzJmMzgwZmRlMTVkMDRjMjM4ZDM2NUphWW5lUHpyclgiLCJzY29wZSI6WyJyZWFkIl19.mMlDakbHmSMqO3M8-76ka9ParCdrSOLW8RGlKhSKS64";
        url = "http://42.228.55.197:8086/api/bff/v1.2/mobile/digitalzz/legal/person/details?access_token=" + access_token;
        apiName = "legal_person_details_csb_cas";
        apiVersion = "1.0.0";
        accessKey = "2a2068a4aab443f28a09f385083432ff";
        secretKry = "pVZDjBS87JioQFTjvSjXQpiQLxA=";
        jsonCont = "";
        result = CsbUtil.postJson(url, apiName, apiVersion, accessKey, secretKry, jsonCont);
        System.out.println(result);
    }


    /**
     * 调用CSB的Get请求
     *
     * @param url        url地址
     * @param apiName    api名称
     * @param apiVersion APi版本
     * @param accessKey  秘钥key
     * @param secretKey  密钥
     * @return
     */
    public static String get(String url, String apiName, String apiVersion, String accessKey, String secretKey) throws Exception {
        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = API_VVERSION;
        }

        String result;
        try {
            HttpParameters.Builder builder = HttpParameters.newBuilder();
            builder.requestURL(url) // 设置请求的URL
                    .api(apiName) // 设置服务名
                    .version(apiVersion) // 设置版本号
                    .method("get");// 设置调用方式, get/post
            if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                builder.accessKey(accessKey)
                        .secretKey(secretKey); // 设置accessKey 和 设置secretKey
            }

            result = HttpCaller.invoke(builder.build());
        } catch (HttpCallerException e) {
            log.error("调用CSB的get请求方法出错!",e);
            return null;
        }

        return result;
    }


    /**
     * 调用CSB的Post请求
     *
     * @param url         url地址
     * @param apiName     api名称
     * @param apiVersion  APi版本
     * @param accessKey   秘钥key
     * @param secretKey   密钥
     * @param jsonContent json数据
     * @return
     */
    public static String postJson(String url, String apiName, String apiVersion, String accessKey,
                                  String secretKey, String jsonContent) throws Exception {
        String result;
        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = API_VVERSION;
        }
        try {
            HttpParameters.Builder builder = HttpParameters.newBuilder();
            builder.requestURL(url) // 设置请求的URL
                    .api(apiName) // 设置服务名
                    .version(apiVersion) // 设置版本号
                    .method("post");// 设置调用方式, get/post
            if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                builder.accessKey(accessKey)
                        .secretKey(secretKey); // 设置accessKey 和 设置secretKey
            }
            ContentBody cb = new ContentBody(jsonContent);
            builder.contentBody(cb);
            //定义json提交方式
            builder.putHeaderParamsMap("Content-Type", "application/json");
            //CSB请求设置超时时间
            Map sysParams = new HashMap();
            sysParams.put("http.caller.connection.timeout","10000"); //设置连接超时为10秒
            HttpCaller.setConnectionParams(sysParams); //注意：本次设置只对本线程起作用
            result = HttpCaller.invoke(builder.build());
        } catch (HttpCallerException e) {
            log.error("调用CSB的postJson请求方法出错!",e);
            return null;
        }

        return result;

    }

    /**
     * 调用CSB的Post请求
     *
     * @param url        url地址
     * @param apiName    api名称
     * @param apiVersion APi版本
     * @param accessKey  秘钥key
     * @param secretKey  密钥
     * @param map        json数据
     * @return
     */
    public static String postMap(String url, String apiName, String apiVersion, String accessKey,
                                 String secretKey, Map<String, String> map) throws Exception {
        String result;
        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = API_VVERSION;
        }
        try {
            HttpParameters.Builder builder = HttpParameters.newBuilder();
            builder.requestURL(url) // 设置请求的URL
                    .api(apiName) // 设置服务名
                    .version(apiVersion) // 设置版本号
                    .method("post");// 设置调用方式, get/post
            if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                builder.accessKey(accessKey)
                        .secretKey(secretKey); // 设置accessKey 和 设置secretKey
            }
            //csb的http请求需这样封装参数
            Iterator<String> keys = map.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) map.get(key);
                //System.out.println(key+":"+value);
                builder.putParamsMap(key, value);
            }
            //定义提交方式
            builder.putHeaderParamsMap("Content-Type", "application/x-www-form-urlencoded");
            result = HttpCaller.invoke(builder.build());
        } catch (HttpCallerException e) {
            log.error("调用CSB的postMap请求方法出错!",e);
            return null;
        }
        return result;
    }

    /**
     * oss调用csb
     * @param url
     * @param apiName
     * @param apiVersion
     * @param accessKey
     * @param secretKey
     * @return
     */
    public static String postJson1(String url, String apiName, String apiVersion, String accessKey, String secretKey,
                                   String access_Id, String access_Key,String access_Tocken) throws Exception {
        String result;
        if (StringUtils.isEmpty(apiVersion)) {
            apiVersion = API_VVERSION;
        }
        try {
            HttpParameters.Builder builder = HttpParameters.newBuilder();
            builder.requestURL(url) // 设置请求的URL
                    .api(apiName) // 设置服务名
                    .version(apiVersion) // 设置版本号
                    .method("post");// 设置调用方式, get/post
            if (StringUtils.isNotEmpty(accessKey) && StringUtils.isNotEmpty(secretKey)) {
                builder.accessKey(accessKey)
                        .secretKey(secretKey); // 设置accessKey 和 设置secretKey
            }
//            ContentBody cb = new ContentBody(jsonContent);
//            builder.contentBody(cb);
            //定义json提交方式
            builder.putHeaderParamsMap("access_Id",access_Id);
            builder.putHeaderParamsMap("access_Key",access_Key);
            builder.putHeaderParamsMap("access_Tocken",access_Tocken);
            System.out.println(access_Tocken);
            String s = "D:/xxxx.png";

            String fileStr = FileUtils.readFileToString(new File(s));
            builder.putHeaderParamsMap("file",fileStr);
            builder.putHeaderParamsMap("folder","upload/bdzx_interface/");
            builder.putHeaderParamsMap("Content-Type", "application/json");
            result = HttpCaller.invoke(builder.build());
        } catch (HttpCallerException e) {
            log.error("调用CSB的postJson请求方法出错!",e);
            return null;
        }

        return result;

    }

}
