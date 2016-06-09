package com.mslibs.widget;


import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.os.Bundle;

import com.health.app.R;
import com.mslibs.utils.ImageUtils;

public class MSPhotoViewActivity extends Activity {
	
	private String TAG = "PhotoViewActivity";
	//private String photourl=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    public void loadPhoto(int contentView, int iv_photo, String photourl, int defaultDrawable) {
    	
    	setContentView(contentView);
    	
        //photourl = getIntent().getStringExtra(Preferences.INTENT_EXTRA.PHOTOURL);        
    
        PhotoView photoView = (PhotoView) findViewById(iv_photo);       
        ImageUtils.setImage(getBaseContext(), photoView, photourl, R.drawable.ic_launcher);
    }
}
