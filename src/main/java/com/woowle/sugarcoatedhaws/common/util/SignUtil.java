package com.woowle.sugarcoatedhaws.common.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zhang
 */
public class SignUtil {

	//main调用方法
	public static void main(String[] args) {

		String secretKey = "01INTEMY6xxxxxxxx";//私钥, paas产品提供
		int application = 10001;//服务器id, paas产品提供
		long timestamp = System.currentTimeMillis()/1000;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("application", application);
		map.put("timestamp", timestamp);
		String digest = sign(map, secretKey);
		System.out.println(digest);

	}

	// 得到签名值
	public static String sign(Map<String, Object> map, String secretKey) {
		StringBuilder contentBuffer = new StringBuilder();
		Object[] signParamArray = map.keySet().toArray();
		Arrays.sort(signParamArray);
		for (Object key : signParamArray) {
			Object value = map.get(key);
			if (!"digest".equals(key) && value != null) {
				contentBuffer.append(key + "=" + value + "&");
			}
		}

		return CertUtil.Md5(contentBuffer.substring(0, contentBuffer.length() - 1) + secretKey);
	}

}