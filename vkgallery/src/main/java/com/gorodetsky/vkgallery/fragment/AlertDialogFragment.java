package com.gorodetsky.vkgallery.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.gorodetsky.vkgallery.listener.DialogClickListener;

/**
 * Created by st on 12/4/14.
 */
public class AlertDialogFragment extends DialogFragment {

    private static final String KEY_TAG = "tag";
    private static final String KEY_TITLE_RESOURCE = "title_resource";
    private static final String KEY_MESSAGE_RESOURCE = "message_resource";
    private static final String KEY_POSITIVE_RESOURCE = "positive_resource";
    private static final String KEY_NEGATIVE_RESOURCE = "negative_resource";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            DialogClickListener listener = (DialogClickListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement DialogClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int tag = args.getInt(KEY_TAG);
        int titleRes = args.getInt(KEY_TITLE_RESOURCE);
        int messageRes = args.getInt(KEY_MESSAGE_RESOURCE);
        int positiveRes = args.getInt(KEY_POSITIVE_RESOURCE);
        int negativeRes = args.getInt(KEY_NEGATIVE_RESOURCE);

        final DialogClickListener listener = (DialogClickListener) getActivity();

        // TODO: is there need for icon?
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert);

        if (positiveRes != 0) {
            builder.setPositiveButton(positiveRes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onPositiveClick(tag);
                    dismiss();
                }
            });
        }

        if (negativeRes != 0) {
            builder.setNegativeButton(negativeRes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onNegativeClick(tag);
                    dismiss();
                }
            });
        }

        if (titleRes != 0) builder.setTitle(titleRes);
        if (messageRes != 0) builder.setMessage(titleRes);
        return builder.create();
    }

    public static class Builder {

        private int tag;
        private int message;
        private int title;
        private int positiveCaption;
        private int negativeCaption;

        public Builder setTag(int tag) {
            this.tag = tag;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = message;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveCaption(int positiveCaption) {
            this.positiveCaption = positiveCaption;
            return this;
        }

        public Builder setNegativeCaption(int negativeCaption) {
            this.negativeCaption = negativeCaption;
            return this;
        }

        public AlertDialogFragment create() {
            AlertDialogFragment fragment = new AlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt(KEY_TAG, tag);
            args.putInt(KEY_TITLE_RESOURCE, title);
            args.putInt(KEY_MESSAGE_RESOURCE, message);
            args.putInt(KEY_POSITIVE_RESOURCE, positiveCaption);
            args.putInt(KEY_NEGATIVE_RESOURCE, negativeCaption);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
