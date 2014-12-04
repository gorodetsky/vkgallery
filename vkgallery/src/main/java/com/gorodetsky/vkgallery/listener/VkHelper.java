package com.gorodetsky.vkgallery.listener;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.activity.MainActivity;
import com.gorodetsky.vkgallery.fragment.AlertDialogFragment;
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

    private FragmentActivity activity;
    private boolean pending = false;

    public VkHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    public void initialize() {
        VKSdk.initialize(this, activity.getString(R.string.vk_app_id));
        if (VKSdk.wakeUpSession()) {
            setPendingStopped();
        }
    }

    public void authorize() {
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

        setPendingStopped();

        FragmentManager manager = activity.getSupportFragmentManager();
        if (manager.findFragmentByTag(MainActivity.TAG_VK_ACCESS_DENIED) == null) {
            new AlertDialogFragment.Builder()
                    .setTitle(R.string.alert_dialog_access_denied_title)
                    .setMessage(R.string.alert_dialog_access_denied_message)
                    .create().show(manager, MainActivity.TAG_VK_ACCESS_DENIED);
        }
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

    private void showAuthorizationDialog() {
        FragmentManager manager = activity.getSupportFragmentManager();
        if (manager.findFragmentByTag(MainActivity.TAG_VK_AUTH) == null) {
            new AlertDialogFragment.Builder()
                    .setTag(MainActivity.DIALOG_AUTH)
                    .setTitle(R.string.alert_dialog_authorization_title)
                    .setMessage(R.string.alert_dialog_authorization_message)
                    .setPositiveCaption(R.string.alert_dialog_ok)
                    .setNegativeCaption(R.string.alert_dialog_cancel)
                    .create().show(manager, MainActivity.TAG_VK_AUTH);
        }
    }

// TODO: delete
//    private void closeAuthorizationDialog() {
//        FragmentManager manager = activity.getSupportFragmentManager();
//        Fragment fragment = manager.findFragmentByTag(MainActivity.TAG_VK_AUTH);
//        if (fragment != null) ((DialogFragment) fragment).dismiss();
//    }

    private void setPending() {
        pending = true;
    }

    private void setPendingStopped() {
        pending = false;
        if (!VKSdk.isLoggedIn()) {
            showAuthorizationDialog();
        }
    }
}
