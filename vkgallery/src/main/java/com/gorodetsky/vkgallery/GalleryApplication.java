package com.gorodetsky.vkgallery;

import android.app.Application;
import com.gorodetsky.vkgallery.utility.MyVolley;

/**
 * Created by st on 12/4/14.
 */
public class GalleryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MyVolley.init(this);
    }
}
