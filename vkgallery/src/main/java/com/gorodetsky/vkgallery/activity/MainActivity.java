package com.gorodetsky.vkgallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.gorodetsky.vkgallery.R;
import com.gorodetsky.vkgallery.listener.DialogClickListener;
import com.gorodetsky.vkgallery.listener.VkHelper;
import com.vk.sdk.VKUIHelper;

public class MainActivity extends ActionBarActivity implements DialogClickListener {

    public static final int DIALOG_AUTH = 1;

    public static final String TAG_VK_AUTH = "vk_auth";
    public static final String TAG_VK_ACCESS_DENIED = "access_denied";

    private VkHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        VKUIHelper.onCreate(this);
        helper = new VkHelper(this);
        helper.initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
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
}
