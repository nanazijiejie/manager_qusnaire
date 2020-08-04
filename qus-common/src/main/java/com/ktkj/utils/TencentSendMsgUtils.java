package com.ktkj.utils;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;

public class TencentSendMsgUtils {
	
	private static Logger logger = LoggerFactory.getLogger(TencentSendMsgUtils.class);


	// 短信应用SDK AppID
	private static final int appid = 1400267073; // 1400开头
	// 短信应用SDK AppKey
	private static final String appkey = "510e140cf331fe993a39a0d81c0c3553";

	public static SmsSingleSenderResult sendMsg(String phoneNumbers,String[] params,int templateId,String smsSign)throws Exception{
		try {
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers,
		        templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
		    logger.info("result===="+result);
		    System.out.print(result);
		    return result;
		} catch (Exception e) {
		    // 网络IO错误
		    e.printStackTrace();
		    return null;
		}
	}
	public static void main(String[] args) {
		try {
//			String[] params={"889900"};
//			String ret = TencentSendMsgUtils.sendMsg("18059046640", params, 474035, "继续想");
//            System.out.println("ret:"+ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
