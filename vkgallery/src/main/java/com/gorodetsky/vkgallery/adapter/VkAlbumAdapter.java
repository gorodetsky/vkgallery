package com.gorodetsky.vkgallery.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.model.VKApiPhotoAlbumArray;
import com.gorodetsky.vkgallery.utility.Configuration;
import com.vk.sdk.api.*;
import com.vk.sdk.api.model.VKApiPhotoAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by st on 12/5/14.
 */
public class VkAlbumAdapter extends BaseAdapter {

    public static final int DEFAULT_ITEMS_COUNT = 3;

    public static final int POSITION_PHOTOS_PROFILE = 0;
    public static final int POSITION_PHOTOS_WALL = 1;
    public static final int POSITION_PHOTOS_SAVED = 2;

    private static final String LOG_TAG = "vk_album_adapter";
    private static final String VK_API_PHOTOS_GET_ALBUMS = "photos.getAlbums";

    private List<VKApiPhotoAlbum> items;

    public VkAlbumAdapter() {
        items = new ArrayList<VKApiPhotoAlbum>();
    }

    public void downloadAlbums() {
        VKRequest request = new VKRequest(VK_API_PHOTOS_GET_ALBUMS,
                VKParameters.from(VKApiConst.USER_ID, Configuration.VK_USER_ID));
        request.setModelClass(VKApiPhotoAlbumArray.class);

        request.executeWithListener(new VKRequest.VKRequestListener() {

            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiPhotoAlbumArray array = (VKApiPhotoAlbumArray) response.parsedModel;
                items.clear();
                items.addAll(array);
                notifyDataSetChanged();
                Log.d(LOG_TAG, response.responseString);
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
        });
    }

    @Override
    public int getCount() {
        return DEFAULT_ITEMS_COUNT + items.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= DEFAULT_ITEMS_COUNT) {
            return items.get(position - DEFAULT_ITEMS_COUNT);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        switch (position) {
            case POSITION_PHOTOS_PROFILE:
                textView.setText(R.string.photos_profile);
                break;

            case POSITION_PHOTOS_WALL:
                textView.setText(R.string.photos_wall);
                break;

            case POSITION_PHOTOS_SAVED:
                textView.setText(R.string.photos_saved);
                break;

            default:
                VKApiPhotoAlbum album = (VKApiPhotoAlbum) getItem(position);
                textView.setText(album.title);
                break;
        }
        return convertView;
    }
}
