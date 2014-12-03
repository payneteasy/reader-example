package com.payneteasy.example.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 *
 */
public class ActivityUtil {

    private final static Logger LOG = LoggerFactory.getLogger(ActivityUtil.class);

    public static void startActivity(Context aContext, Class<? extends Activity> aActivityToStart) {
        startActivity(aContext, aActivityToStart, null);
    }

    public static void startActivity(final Context aContext, Class<? extends Activity> aActivityToStart, Serializable aParameter) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("parameter-0", aParameter);

        final Intent intent = new Intent(aContext, aActivityToStart);
        intent.putExtras(bundle);

        if(aContext instanceof Activity) {
            final Activity activity = (Activity) aContext;
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {

                // On UI thread.
                aContext.startActivity(intent);

            } else {
                LOG.warn("Starting activity "+activity.getClass().getSimpleName()+" in runOnUiThread", new RuntimeException("runOnUiThread"));

                // Not on UI thread.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aContext.startActivity(intent);
                    }
                });
            }

        } else {
            LOG.warn("Starting activity from non UI thread %s", Thread.currentThread().toString());
            aContext.startActivity(intent);
        }

    }

    public static <T extends Serializable> T getFirstParameter(Activity aContext, Class<T> aParameterClass) {
        if(aContext.getIntent()!=null && aContext.getIntent().getExtras()!=null ) {
            return (T) aContext.getIntent().getExtras().get("parameter-0");
        } else {
            return null;
        }
    }

    public static void startFirstActivityFromStack(Activity aContext, Class<? extends Activity> aActivityToStart, Serializable aParameter) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("parameter-0", aParameter);

        Intent intent = new Intent(aContext, aActivityToStart);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        aContext.startActivity(intent);
    }
}
