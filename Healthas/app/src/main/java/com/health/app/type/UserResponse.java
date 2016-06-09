package com.health.app.type;

import java.io.Serializable;

public class UserResponse implements Serializable{
	public String token = null;
	// "avatar": "",
	// "nick": "你猜呢",
	// "sex": "1",
	// "sign": "",
	// "intro": "",
	// "tel": "15052506610",
	// "uid": "1001007"
	public String id, uid, intro, sex, sign, nick, avatar, tel;
	public String content;
	public String account;

}
