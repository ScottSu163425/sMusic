package com.su.scott.slibrary.manager;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by asus on 2016/9/2.
 */
public class AsyncTaskHelper {
    public interface AsyncTaskCallback {
        void onPreExecute();

        void onPostExecute();
    }

    public static void excuteSimpleTask(@NonNull final Runnable runnable, @Nullable final AsyncTaskCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (callback != null) {
                    callback.onPreExecute();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                runnable.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (callback != null) {
                    callback.onPostExecute();
                }
            }
        }.execute();
    }


}
