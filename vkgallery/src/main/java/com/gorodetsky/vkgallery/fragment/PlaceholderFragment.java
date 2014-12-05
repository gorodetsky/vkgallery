package com.gorodetsky.vkgallery.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gorodetsky.vkgallery.R;

/**
 * Created by st on 12/4/14.
 */
public class PlaceholderFragment extends Fragment {

    public PlaceholderFragment() {
        super();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewPager pager = (ViewPager) inflater.inflate(R.layout.fragment_placeholder, container, false);

        return pager;
    }
}
