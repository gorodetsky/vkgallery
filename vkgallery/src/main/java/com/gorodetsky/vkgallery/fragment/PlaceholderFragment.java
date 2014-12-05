package com.gorodetsky.vkgallery.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.adapter.VkPhotoAdapter;
import com.vk.sdk.api.model.VKApiPhoto;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by st on 12/4/14.
 */
public class PlaceholderFragment extends Fragment {

    private static final String LOG_TAG = "placeholder";
    private static final String KEY_PHOTOS = "photos";
    private static final int OFFSCREEN_PAGE_COUNT = 3;

    private VkPhotoAdapter photoAdapter;
    private ViewPager pager;

    public PlaceholderFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoAdapter = new VkPhotoAdapter(getFragmentManager());

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_PHOTOS)) {
            List<VKApiPhoto> list = savedInstanceState.getParcelableArrayList(KEY_PHOTOS);
            photoAdapter.setItems(list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        pager = (ViewPager) inflater.inflate(R.layout.fragment_placeholder, container, false);
        pager.setOffscreenPageLimit(OFFSCREEN_PAGE_COUNT);
        pager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.image_vk_photo));
        pager.setAdapter(photoAdapter);
        return pager;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_PHOTOS, photoAdapter.getItems());
    }

    public void setPagerPosition(int position) {
        pager.setCurrentItem(0);
    }

    public VkPhotoAdapter getPhotoAdapter() {
        return photoAdapter;
    }
}
