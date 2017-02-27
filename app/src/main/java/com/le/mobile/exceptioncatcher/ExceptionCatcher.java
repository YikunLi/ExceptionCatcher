package com.le.mobile.exceptioncatcher;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyikun on 2017/2/16.
 */

public class ExceptionCatcher {

    private static ExceptionCatcher sExceptionCatcher;

    public static void install() {
        if (sExceptionCatcher == null) {
            sExceptionCatcher = new ExceptionCatcher();
        }
    }

    public static void uninstall() {
        if (sExceptionCatcher != null) {
            sExceptionCatcher = new ExceptionCatcher();
        }
    }

    public static void registerExceptionHandler(ExceptionHandler handler) {
        if (sExceptionCatcher.mExceptionHandlers == null) {
            sExceptionCatcher.mExceptionHandlers = new ArrayList<>();
        }
        sExceptionCatcher.mExceptionHandlers.add(handler);
    }

    public static void unregisterExceptionHandler(ExceptionHandler handler) {
        if (sExceptionCatcher == null) {
            return;
        }
        sExceptionCatcher.mExceptionHandlers.remove(handler);
    }

    private Handler mHandler;

    private List<ExceptionHandler> mExceptionHandlers;

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ExceptionCatcher.this.doSomething();
        }
    };

    private ExceptionCatcher() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mHandler.post(this.mRunnable);
    }

    private void doSomething() {
        Exception exception = null;
        do {
            try {
                Looper.loop();
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
                if (this.mUncaughtExceptionHandler != null) {
                    this.mUncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
                }
            }
        } while (this.catchExceptionOrCrash(exception));
    }

    private boolean catchExceptionOrCrash(Exception exception) {
        if (!this.catchException(exception)) {
            // crash
            throw new RuntimeException(exception);
        }
        return true;
    }

    private boolean catchException(Exception exception) {
        if (this.mExceptionHandlers == null) {
            return false;
        }
        for (ExceptionHandler handler : this.mExceptionHandlers) {
            if (handler.handleException(exception)) {
                return true;
            }
        }
        return false;
    }

    public interface ExceptionHandler {

        boolean handleException(Exception exception);
    }
}