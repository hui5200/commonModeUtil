package com.ailin.util;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.juli.logging.Log;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpUtil {

    public static void main(String[] args) {
        testUploadImage();
    }

    /**
     * 测试上传图片
     *
     */
    public static String testUploadImage(){
        String url = "http://127.0.0.1:8882/upload/image";
        String fileName = "F:\\Files\\1.jpg";
        Map<String, String> textMap = new HashMap<String, String>();
        JsonObject jsonObject = new JsonObject();
        //可以设置多个input的name，value
        textMap.put("name", "测试");
        textMap.put("age", "1115");
        jsonObject.addProperty("name", "测试");
        jsonObject.addProperty("age", "1123");
        //设置file的name，路径
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("upfile", fileName);
        String contentType = "";//image/png
//        String ret = formUpload(url, jsonObject, fileMap,contentType);
        String ret = createUserCre(url, jsonObject);
        System.out.println(ret);
        return ret;
        //{"status":"0","message":"add succeed","baking_url":"group1\/M00\/00\/A8\/CgACJ1Zo-LuAN207AAQA3nlGY5k151.png"}
    }

    /**
     * 上传图片
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     * contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUpload(String urlStr, JsonObject textMap,
                                    Map<String, String> fileMap,String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        String uuid = UUID.randomUUID().toString();
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------" + uuid;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            // conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = ((JsonElement)entry.getValue()).getAsString();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    //contentType非空采用filename匹配默认的图片类型
                    if(!"".equals(contentType)){
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        }else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        }else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        }else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    public static String postFormData(String path, HashMap<String, String> Headers, JsonObject formMap) {
        String result = "";
        HttpURLConnection connection = null;
        String boundary = "--------------------------132183525382215881770481";
        try {
            URL url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 不使用缓存
            connection.setUseCaches(false);
            if (Headers != null) {
                if (Headers.size() > 0) {
                    for (Map.Entry<String, String> entry : Headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
            }
            StringBuffer formSB = new StringBuffer();
            if (formMap != null) {
                Iterator iter = formMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = ((JsonElement)entry.getValue()).getAsString();
                    formSB.append("\r\n").append("--").append(boundary).append("\r\n");
                    formSB.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    formSB.append(inputValue);
                }
                formSB.append("\r\n").append("--").append(boundary).append("--");
            }
            connection.connect();
            //OutputStream out = new DataOutputStream(connection.getOutputStream());
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            out.print(formSB.toString());
            out.flush();
            //获得响应状态
            int resultCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                formSB = new StringBuffer();
                String readLine;
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    formSB.append(readLine).append("\n");
                }
                responseReader.close();
                result = formSB.toString();
            } else {
                result = "{\"code\":\"" + resultCode + "\"}";
            }
            out.close();
        } catch (Exception e) {
        } finally {
            assert connection != null;
            connection.disconnect();
        }
        return result;
    }

    public static String createUserCre(String url, JsonObject params) {
        HttpPost httpPost = new HttpPost(url);

        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ContentType strContent=ContentType.create("text/plain", StandardCharsets.UTF_8);

        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String inputName = (String) entry.getKey();
            String inputValue = ((JsonElement)entry.getValue()).getAsString();
            builder.addTextBody(inputName,inputValue,strContent);
        }

        HttpEntity multipart = builder.build();

        HttpResponse resp = null;
        try {
            httpPost.setEntity(multipart);
            resp = client.execute(httpPost);

            //注意，返回的结果的状态码是302，非200
            if (resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he, "UTF-8");
            }
        } catch (Exception e) {
        }

        return respContent;
    }
}
