package com.gorodetsky.vkgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.vk.sdk.api.*;

/**
 * Created by st on 12/4/14.
 */
public class VkPhotoAdapter extends FragmentStatePagerAdapter {

    public VkPhotoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
