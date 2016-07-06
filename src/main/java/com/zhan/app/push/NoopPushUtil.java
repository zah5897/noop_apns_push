package com.zhan.app.push;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.zhan.app.util.TextUtils;

public class NoopPushUtil {

	private boolean isProduction = true;

	private int poolSize = 10;
	// *.p12文件位置
	// private String iphoneCertPath = "D:/push_token/DEV/dev.p12";
	private String iphoneCertPath = "D:/push_token/DIS/dis.p12";
	// *.p12文件密码
	private String iphoneCertPassword = "magicpush";

	private ApnsService iphoneApnsService;
	private String MESSAGE_KEY = "info";

	/**
	 * @param args
	 */
	public void init() {
		// iphoneCertPath =
		// getClass().getClassLoader().getResource("").toString()+"dis.p12";

		poolSize = getPoolSize();
		// 底层的代码根据isProduction来选择是生产环境的地址和端口还是测试环境的地址和端口
		iphoneApnsService = APNS.newService().withCert(iphoneCertPath, iphoneCertPassword)
				.withAppleDestination(isProduction).asPool(poolSize).asBatched().build();
		// iphoneApnsService = APNS.newService().withCert(iphoneCertPath,
		// iphoneCertPassword)
		// .withAppleDestination(isProduction).build();
	}

	private int getPoolSize() {
		int numberOfCores = Runtime.getRuntime().availableProcessors(); // 获得核心数
		double blockingCoefficient = 0.9;// 阻塞系数
		int poolSize = (int) (numberOfCores / (1 - blockingCoefficient)); // 求得线程数大小
		return poolSize;
	}

	public boolean pushNews(PushMsg msg) {

		if (msg == null) {
			return false;
		}
		String title = msg.alert;
		String id = msg.id;
		Map<String, String> jsonObject = new HashMap<String, String>();
		jsonObject.put("type", "0");
		// jsonObject.put("text", title);
		jsonObject.put("id", id);
		boolean sound = true;
		String deviceToken = msg.token;
		String alert = title;
		int badge = 1;
		pushIphoneNotification(deviceToken, alert, badge, sound, jsonObject);
		return true;
	}

	/**
	 * 推送单个iphone消息
	 */
	public void pushIphoneNotification(String deviceToken, String alert, int badge, boolean soundTip, Object message) {
		if (TextUtils.isEmpty(deviceToken)) {
			throw new RuntimeException("deviceToken为空");
		}

		String sound = null;
		if (soundTip) {
			sound = "default";
		}
		this.sendIphoneNotification(alert, badge, sound, message, deviceToken);
	}

	/**
	 * 推送多个设备
	 * 
	 * @param deviceTokens
	 * @param alert
	 * @param badge
	 * @param soundTip
	 * @param message
	 */
	public void pushIphoneNotification(Collection<String> deviceTokens, String alert, int badge, boolean soundTip,
			Object message) {
		if (deviceTokens == null || deviceTokens.size() == 0) {
			throw new RuntimeException("deviceToken为空");
		}

		String sound = null;
		if (soundTip) {
			sound = "default";
		}
		this.sendIphoneNotifications(alert, badge, sound, message, deviceTokens);
	}

	/**
	 * 推送iphone消息
	 * 
	 * @param payload
	 * @param tokenList
	 * @param password
	 * @param production
	 */
	private void sendIphoneNotification(String alert, int badge, String sound, Object message, String deviceToken) {
		PayloadBuilder payloadBuilder = APNS.newPayload();
		if (TextUtils.isNotEmpty(alert)) {
			payloadBuilder.alertBody(alert);
		}
		if (badge > 0) {
			payloadBuilder.badge(badge);
		}
		if (TextUtils.isNotEmpty(sound)) {
			payloadBuilder.sound(sound);
		}
		if (message != null) {
			payloadBuilder.customField(MESSAGE_KEY, message);
		}

		if (payloadBuilder.isTooLong()) {
			throw new RuntimeException("信息过长");
		}
		// 次数发送给单个设备，也可以同时发给多个设备
		iphoneApnsService.push(deviceToken, payloadBuilder.build());
	}

	private void sendIphoneNotifications(String alert, int badge, String sound, Object message,
			Collection<String> deviceTokens) {
		PayloadBuilder payloadBuilder = APNS.newPayload();
		if (TextUtils.isNotEmpty(alert)) {
			payloadBuilder.alertBody(alert);
		}
		if (badge > 0) {
			payloadBuilder.badge(badge);
		}
		if (TextUtils.isNotEmpty(sound)) {
			payloadBuilder.sound(sound);
		}
		if (message != null) {
			payloadBuilder.customField(MESSAGE_KEY, message);
		}

		if (payloadBuilder.isTooLong()) {
			throw new RuntimeException("信息过长");
		}
		iphoneApnsService.push(deviceTokens, payloadBuilder.build());
	}

}
