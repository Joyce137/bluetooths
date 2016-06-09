package com.mslibs.utils;

public class Preferences {
	public static final Boolean DEBUG = true;	
	public static class INTENT_EXTRA {	
		public static final String PHOTOURL = "intent.photourl";
	}

	public static class REQUEST_CODE {		
		public static final int TAKE_PHOTO = 990;
		public static final int GET_PHOTO = 991;
		public static final int FEED = 992;
	}
	
	public static class INTENT_ACTION {
		public static final String REFRESH = "action.refresh";
	}

	public static class LOCAL {
		public static final String TOKEN = "local.token";
		
	}


}
