package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayManager;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.react.JsDevReloadHandler;
import com.reactnativenavigation.react.ReactGateway;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NavigationActivityDelegate extends ReactActivityDelegate {
    private NavigationActivity activity;
    @Nullable
    private PermissionListener mPermissionListener;

    public NavigationActivityDelegate(NavigationActivity a) {
        super(a, null);
        activity = a;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity.isFinishing()) {
            return;
        }
        addDefaultSplashLayout();
        getReactGateway().onActivityCreated((NavigationActivity) activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReactGateway().onActivityResumed(activity);
    }

    @Override
    public boolean onNewIntent(Intent intent) {
        if (!getReactGateway().onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        getReactGateway().onActivityPaused(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getReactGateway().onActivityDestroyed(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getReactGateway().onActivityResult(this.getPlainActivity(), requestCode, resultCode, data);
    }

    @Override
    public boolean onBackPressed() {
        getReactGateway().onBackPressed();
        return false;
    }

    @Override
    public boolean onKeyUp(final int keyCode, final KeyEvent event) {
        return getReactGateway().onKeyUp(this.getPlainActivity(), keyCode) || super.onKeyUp(keyCode, event);
    }



    public ReactGateway getReactGateway() {
        return app().getReactGateway();
    }

    private NavigationApplication app() {
        return (NavigationApplication) getPlainActivity().getApplication();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        requestPermissions(permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        NavigationApplication.instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionListener != null && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            mPermissionListener = null;
        }
    }

    protected void addDefaultSplashLayout() {
        View view = new View(this.getContext());
        view.setBackgroundColor(Color.WHITE);
        activity.setContentView(view);
    }
}
