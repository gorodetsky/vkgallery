package com.gorodetsky.vkgallery.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.toolbox.NetworkImageView;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.utility.MyVolley;

/**
 * Created by st on 12/4/14.
 */
public class VkPhotoFragment extends Fragment {

    private static final String KEY_IMAGE_URL = "image_url";

    private String url;

    public static VkPhotoFragment newInstance(String imageUrl) {
        VkPhotoFragment fragment = new VkPhotoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public VkPhotoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(KEY_IMAGE_URL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        NetworkImageView imageView = (NetworkImageView) inflater
                .inflate(R.layout.fragment_vk_photo, container, false);
        imageView.setImageUrl(url, MyVolley.getImageLoader());
        return imageView;
    }
}
