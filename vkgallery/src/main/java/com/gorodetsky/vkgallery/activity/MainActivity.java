package com.gorodetsky.vkgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.adapter.VkAlbumAdapter;
import com.gorodetsky.vkgallery.adapter.VkPhotoAdapter;
import com.gorodetsky.vkgallery.fragment.AlertDialogFragment;
import com.gorodetsky.vkgallery.fragment.PlaceholderFragment;
import com.gorodetsky.vkgallery.listener.AuthHelperListener;
import com.gorodetsky.vkgallery.listener.DialogClickListener;
import com.gorodetsky.vkgallery.listener.VkHelper;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.model.VKApiPhotoAlbum;

public class MainActivity extends ActionBarActivity implements DialogClickListener,
        AuthHelperListener, AdapterView.OnItemClickListener {

    public static final int DIALOG_AUTH = 1;

    public static final String TAG_VK_AUTH = "vk_auth";
    public static final String TAG_VK_ACCESS_DENIED = "access_denied";
    public static final String TAG_PLACEHOLDER = "placeholder";

    private static final String LOG_TAG = "main_activity";

    private static boolean loaded = false;

    private VkHelper helper;
    private DrawerLayout drawer;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        list = (ListView) findViewById(R.id.left_drawer);
        list.setAdapter(new VkAlbumAdapter());
        list.setOnItemClickListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), TAG_PLACEHOLDER)
                    .commit();
        }

        VKUIHelper.onCreate(this);
        helper = new VkHelper(getString(R.string.vk_app_id));
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);

        if (helper.isLoggedIn()) {
            if (!loaded) onAuthSuccess();
        } else {
            onAuthShow();
        }
        helper.setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        helper.setListener(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
        loaded = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPositiveClick(int tag) {
        switch (tag) {
            case DIALOG_AUTH:
                helper.authorize();
                break;

            default:
                break;
        }
    }

    @Override
    public void onNegativeClick(int tag) {
        switch (tag) {
            case DIALOG_AUTH:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onAuthSuccess() {
        VkAlbumAdapter adapter = (VkAlbumAdapter) list.getAdapter();
        adapter.downloadAlbums();
        getPlaceholder().getPhotoAdapter().loadPhotosTagged();
        loaded = true;
    }

    @Override
    public void onAuthError() {
        showAuthErrorDialog();
    }

    @Override
    public void onAuthShow() {
        showAuthorizationDialog();
    }

    private void showAuthErrorDialog() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(TAG_VK_ACCESS_DENIED) == null) {
            new AlertDialogFragment.Builder()
                    .setTitle(R.string.alert_dialog_access_denied_title)
                    .setMessage(R.string.alert_dialog_access_denied_message)
                    .create().show(manager, TAG_VK_ACCESS_DENIED);
        }
    }

    private void showAuthorizationDialog() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentByTag(MainActivity.TAG_VK_AUTH) == null) {
            new AlertDialogFragment.Builder()
                    .setTag(DIALOG_AUTH)
                    .setTitle(R.string.alert_dialog_authorization_title)
                    .setMessage(R.string.alert_dialog_authorization_message)
                    .setPositiveCaption(R.string.alert_dialog_ok)
                    .setNegativeCaption(R.string.alert_dialog_cancel)
                    .create().show(manager, TAG_VK_AUTH);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaceholderFragment placeholder = getPlaceholder();
        if (placeholder == null) return;
        VkPhotoAdapter photoAdapter = placeholder.getPhotoAdapter();

        switch (position) {
            case VkAlbumAdapter.POSITION_PHOTOS_PROFILE:
                photoAdapter.loadPhotosTagged();
                break;

            case VkAlbumAdapter.POSITION_PHOTOS_WALL:
                photoAdapter.loadPhotosWall();
                break;

            case VkAlbumAdapter.POSITION_PHOTOS_SAVED:
                photoAdapter.loadPhotosSaved();
                break;

            default:
                VkAlbumAdapter albumAdapter = (VkAlbumAdapter) parent.getAdapter();
                VKApiPhotoAlbum album = (VKApiPhotoAlbum) albumAdapter.getItem(position);
                photoAdapter.loadPhotosAlbum(album.getId());
                break;
        }
        placeholder.setPagerPosition(0);
        drawer.closeDrawers();
    }

    private PlaceholderFragment getPlaceholder() {
        FragmentManager manager = getSupportFragmentManager();
        return (PlaceholderFragment) manager.findFragmentByTag(TAG_PLACEHOLDER);
    }

    private void logout() {
        VKSdk.logout();
        showAuthorizationDialog();
        getPlaceholder().getPhotoAdapter().clear();
        loaded = false;
    }
}
