package com.zhan.app.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.zhan.app.util.RedisKeys;

@RestController
@RequestMapping("/pub")
public class PubController {
	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	@RequestMapping("topub")
	public String pub(HttpServletRequest request, String content) {

		JSONObject object = new JSONObject();
		String title = "朴有天性侵案A女再翻供 称被男方挡住无法离开包厢";
		object.put("id", "5776326d016d286fa9c71744");
		object.put("token", "1059b49987cbfc7a9baf7afccd796deb9813fbd3597a5f035ce3a51aaa560062");
		object.put("alert", title);

		redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH, object.toJSONString());
		// System.out.println("start:" + new Date().toLocaleString());
		// new Thread() {
		// @Override
		// public void run() {
		// int i = 0;
		// do {
		// JSONObject object = new JSONObject();
		// String title = "《所以和黑粉结婚了》：多少影片都毁在坑爹的电影名上了";
		// object.put("id", "5775b644016dfffe521f90cb");
		// object.put("index", i);
		// object.put("alert", title);
		// redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH,
		// object.toJSONString());
		// i++;
		// } while (i < 10000);
		// }
		// }.start();

		return "";
	}
}
