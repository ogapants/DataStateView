package com.github.ogapants.loadstateview.sample;

import android.os.Handler;

import com.github.ogapants.loadstateview.LoadState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SampleApi {

    //get dummy response
    public void load(final LoadState expectState, final Callback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (expectState) {
                    case LOADED_EMPTY:
                        callback.onSuccess(Collections.<String>emptyList());
                        break;
                    case ERROR:
                        callback.onError();
                        break;
                    case DISABLE:
                        callback.onSuccess(Arrays.asList(
                                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"));
                        break;
                    case LOADING:
                    default:
                        //nop
                }
            }
        }, 1000);
    }

    public interface Callback {
        void onSuccess(List<String> result);

        void onError();
    }

}
