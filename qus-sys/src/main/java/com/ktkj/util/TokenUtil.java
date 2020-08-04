package com.ktkj.util;

import com.ktkj.entity.JsapiTicket;
import com.ktkj.entity.Token;
import com.ktkj.util.MyX509TrustManager;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;


public class TokenUtil {

    public final static String appid = "wx5b56683402673307";
    public final static String secret = "fdf11f459d3a7d63ecbbc84bfe711709";

    // 凭证获取（GET）,token_url获取凭证接口的请求地址
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?"
            + "grant_type=client_credential&"
            + "appid=" + appid + "&"
            + "secret=" + secret;


    /**
     * 发送https请求.定义了一个通用的HTTPS请求方法，用于发送HTTPS GET和POST请求
     * 当调用公众平台的开发接口时，都需要传入接口访问凭证access_token
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return
     */
    public static JSONObject getTokenJsonObject(String requestUrl, String requestMethod, String outputStr) {

        JSONObject returnData = null;

        try {

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();

            returnData = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            System.out.printf("连接超时：{}", ce);
        } catch (Exception e) {
            System.out.printf("https请求异常：{}", e);
        }

        return returnData;
    }


    /**
     * 获取接口访问凭证。将得到的json结果转为Token对象
     *
     * @return
     */
    public static Token getToken() {

        Token token = null;
        String requestUrl = token_url;
        // 发起GET请求获取凭证
        JSONObject returnData = getTokenJsonObject(requestUrl, "GET", null);

        if (null != returnData) {

            try {
                token = new Token();
                token.setAccessToken(returnData.getString("access_token"));
                token.setExpiresIn(Integer.parseInt(returnData.getString("expires_in")));

            } catch (Exception e) {
                token = null;
                // 获取token失败
                System.out.printf("获取token失败 errcode:{} errmsg:{}", returnData.get("errcode"),
                        returnData.get("errmsg"));
            }
        }

        return token;
    }


    /**
     * 获取jsapi_ticket
     *
     * @param accessToken 凭证
     * @return
     */
    public static JsapiTicket getJsapiTicket(String accessToken) {

        //获取公众号jsapi_ticket的链接
        String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
                + "access_token=ACCESS_TOKEN&type=jsapi";

        //ticket分享值
        JsapiTicket jsapiticket = null;

        if (accessToken != null) {
            String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
            JSONObject jsonObject = getTokenJsonObject(requestUrl, "GET", null);
            // 如果请求成功
            if (null != jsonObject) {
                try {
                    jsapiticket = new JsapiTicket();
                    jsapiticket.setTicket(jsonObject.getString("ticket"));
                    jsapiticket.setExpiresIn(jsonObject.getInt("expires_in"));
                } catch (JSONException e) {
                    jsapiticket = null;
                    // 获取ticket失败
                    System.out.printf("获取ticket失败 errcode:{} errmsg:{}",
                            jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
                }
            }
        } else {
            System.out.println("*****token为空 获取ticket失败******");
        }

        return jsapiticket;
    }

}