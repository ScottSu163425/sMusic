package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/23.
 */
public abstract class CreateBillDialogFragment extends DialogFragment {
    private View mRootView;
    private TextInputLayout mInputLayout;
    private Button mConfirmButton;

    public abstract void onConfirmClick(String text);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_dialog_create_bill, container, false);
            mInputLayout = (TextInputLayout) mRootView.findViewById(R.id.input_layout_bill_name_fragment_dialog_create_bill);
            mConfirmButton = (Button) mRootView.findViewById(R.id.btn_confirm_fragment_dialog_create_bill);

            mConfirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = mInputLayout.getEditText().getText().toString().trim();
                    onConfirmClick(input);
                }
            });
        }
        return mRootView;
    }


}
