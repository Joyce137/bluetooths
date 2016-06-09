package com.health.app.type;

public class HomeBanner {
	// "id": "7",
	// "url": "http://www.360.cn",
	// "picture": "http://192.168.0.106/upload/2/ec/55/2ec551d033c5408d.jpg
	public int id;
	public int type;
	public int shop_id;
	public int activity_id;
	public String url;
	public String city_id;
	public String picture;
	public String image;

	public int status;
	public String remark;
	public String activity_type;
	public String title;

	public HomeBanner() {

	}

	public HomeBanner(String image) {
		this.image = image;
	}
}
