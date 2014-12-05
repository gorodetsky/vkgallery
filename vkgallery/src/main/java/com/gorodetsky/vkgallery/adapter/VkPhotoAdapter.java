package com.gorodetsky.vkgallery.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.gorodetsky.vkgallery.fragment.VkPhotoFragment;
import com.gorodetsky.vkgallery.utility.Configuration;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPhotoArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by st on 12/4/14.
 */
public class VkPhotoAdapter extends FragmentStatePagerAdapter {

    private static final String LOG_TAG = "vk_photo_adapter";

    private static final String VK_API_PHOTOS_GET = "photos.get";
    private static final String VK_API_PHOTOS_TAGGED = "photos.getUserPhotos";

    private static final String VK_ALBUM_PROFILE = "profile";
    private static final String VK_ALBUM_WALL = "wall";
    private static final String VK_ALBUM_SAVED = "saved";

    private List<VKApiPhoto> items;
    private RequestListener listener;

    public VkPhotoAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<VKApiPhoto>();
        listener = new RequestListener();
    }

    @Override
    public Fragment getItem(int position) {
        VKApiPhoto photo = items.get(position);
        //TODO: process screen size
        return VkPhotoFragment.newInstance(photo.photo_604);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    //http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view/7287121#7287121
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void loadPhotosTagged() {
        VKRequest request = new VKRequest(VK_API_PHOTOS_TAGGED);
        executeRequest(request);
    }

    public void loadPhotosWall() {
        loadAlbum(VK_ALBUM_WALL);
    }

    public void loadPhotosSaved() {
        loadAlbum(VK_ALBUM_SAVED);
    }

    public void loadPhotosAlbum(int id) {
        loadAlbum(id);
    }

    private void loadAlbum(Object album) {
        VKRequest request = new VKRequest(VK_API_PHOTOS_GET,
                VKParameters.from(VKApiConst.OWNER_ID, Configuration.VK_USER_ID,
                        VKApiConst.ALBUM_ID, album));
        executeRequest(request);
    }

    private void executeRequest(VKRequest request) {
        request.setModelClass(VKPhotoArray.class);
        request.executeWithListener(listener);
    }

    public class RequestListener extends VKRequest.VKRequestListener {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            VKPhotoArray array = (VKPhotoArray) response.parsedModel;
            items.clear();
            notifyDataSetChanged();

            items.addAll(array);
            notifyDataSetChanged();
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
            Log.e(LOG_TAG, "attempt failed");
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            Log.e(LOG_TAG, error.toString());
        }
    }
}
