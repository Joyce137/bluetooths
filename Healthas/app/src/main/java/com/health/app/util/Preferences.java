package com.health.app.util;


public class Preferences {
	public static final Boolean DEBUG = true;

	public static class BROADCAST_ACTION {

		public static final String USERSIGNUP = "health.user.signup";
		public static final String USER_UPDATE = "health.user.info.update";
		public static final String USERSIGNIN = "health.user.USERSIGNIN";
		public static final String REFRESH = "health.user.REFRESH";
		public static final String GOTOMAIN = "health.user.GOTOMAIN";
	}

	public static class INTENT_EXTRA {

		public static final String TOKEN = "intent.health.extra.push.token";
		public static final String MAP_LAT = "intent.health.map.lat";
		public static final String MAP_LNG = "intent.health.map.lng";
		public static final String MAP_ZOOM = "intent.health.map.zoom";
		public static final String MAP_MARKER_TIP = "intent.health.map.tip";
	}

	public static class REQUEST_CODE {
	}

	public static class LOCAL {
		public static final String TOKEN = "local.health.token";
		public static final String PHONE = "local.health.phone";
		public static final String IMEI = "local.health.imei";
		public static final String USERID = "local.health.USERID";
	}

	public static class FILE {
	}

}
