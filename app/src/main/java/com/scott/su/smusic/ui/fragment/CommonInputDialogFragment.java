package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.su.scott.slibrary.util.CirclarRevealUtil;

/**
 * Created by asus on 2016/8/23.
 */
public class CommonInputDialogFragment extends DialogFragment {
    private View mRootView;
    private TextView mTitleTextView;
    private TextInputLayout mInputLayout;
    private Button mConfirmButton;
    private CommonInputDialogCallback mCallback;

    private String mTitle="";
    private String mHint="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_dialog_common_input, container, false);
            mTitleTextView= (TextView) mRootView.findViewById(R.id.tv_title_fragment_dialog_common_input);
            mInputLayout = (TextInputLayout) mRootView.findViewById(R.id.input_layout_fragment_dialog_common_input);
            mConfirmButton = (Button) mRootView.findViewById(R.id.btn_confirm_fragment_dialog_common_input);
            mTitleTextView.setText(mTitle);
            mInputLayout.getEditText().setHint(mHint);

            if (mInputLayout.getEditText() != null) {
                mInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() > mInputLayout.getCounterMaxLength()) {
                            mInputLayout.setErrorEnabled(true);
                            mInputLayout.setError(getResources().getString(R.string.error_text_length_overflow));
                        } else {
                            mInputLayout.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            mConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = mInputLayout.getEditText().getText().toString().trim();
                    if (TextUtils.isEmpty(input)) {
                        mInputLayout.setErrorEnabled(true);
                        mInputLayout.setError(getResources().getString(R.string.error_input_empty));
                        return;
                    }

                    if (mCallback != null) {
                        mCallback.onConfirmClick(input);
                    }
                }
            });
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                CirclarRevealUtil.revealIn(mRootView, CirclarRevealUtil.DIRECTION.LEFT_TOP);
            }
        });

    }

    public void setHint(String mHint) {
        this.mHint = mHint;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setCallback(CommonInputDialogCallback mCallback) {
        this.mCallback = mCallback;
    }

    public interface CommonInputDialogCallback {
        void onConfirmClick(String text);
    }

}
