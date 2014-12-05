package com.gorodetsky.vkgallery.listener;

import android.util.Log;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKSdkListener;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

/**
 * Created by st on 12/4/14.
 */
public class VkHelper extends VKSdkListener {

    private static final String LOG_TAG = "vk_helper";
    private static final String[] SCOPE = {
            VKScope.PHOTOS
    };

    private String appToken;
    private AuthHelperListener listener;
    private boolean pending = false;

    public VkHelper(String appToken) {
        this.appToken = appToken;
        initialize();
    }

    public void setListener(AuthHelperListener listener) {
        this.listener = listener;
    }

    public void authorize() {
        if (isPending()) return;

        setPending();
        VKSdk.authorize(SCOPE);
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isLoggedIn() {
        return VKSdk.isLoggedIn();
    }

    @Override
    public void onCaptchaError(VKError captchaError) {
        new VKCaptchaDialog(captchaError).show();
    }

    @Override
    public void onTokenExpired(VKAccessToken expiredToken) {
        authorize();
    }

    @Override
    public void onAccessDenied(VKError authorizationError) {
        Log.e(LOG_TAG, authorizationError.toString());
        if (listener != null) listener.onAuthError();
        setPendingStopped();
    }

    @Override
    public void onReceiveNewToken(VKAccessToken newToken) {
        super.onReceiveNewToken(newToken);
        setPendingStopped();
    }

    @Override
    public void onAcceptUserToken(VKAccessToken token) {
        super.onAcceptUserToken(token);
        setPendingStopped();
    }

    @Override
    public void onRenewAccessToken(VKAccessToken token) {
        super.onRenewAccessToken(token);
        setPendingStopped();
    }

    private void initialize() {
        VKSdk.initialize(this, appToken);
        if (VKSdk.wakeUpSession()) {
            setPendingStopped();
        } else {
            if (listener != null) listener.onAuthShow();
        }
    }

    private void setPending() {
        pending = true;
    }

    private void setPendingStopped() {
        pending = false;

        if (VKSdk.isLoggedIn()) {
            if (listener != null) listener.onAuthSuccess();
        } else {
            if (listener != null) listener.onAuthShow();
        }
    }
}
