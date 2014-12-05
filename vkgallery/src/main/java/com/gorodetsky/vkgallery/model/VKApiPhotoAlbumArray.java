package com.gorodetsky.vkgallery.model;

import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiPhotoAlbum;
import com.vk.sdk.api.model.VKList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by st on 12/5/14.
 */
public class VKApiPhotoAlbumArray extends VKList<VKApiPhotoAlbum> {

    @Override
    public VKApiModel parse(JSONObject response) throws JSONException {
        fill(response, VKApiPhotoAlbum.class);
        return this;
    }
}
