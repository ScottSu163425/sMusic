package com.scott.su.smusic.callback;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by asus on 2016/11/20.
 */

public interface MusicPlayMainFragmentCallback {
    void onBlurCoverChanged(Bitmap bitmap);

    void onCoverClick(View view);
}
