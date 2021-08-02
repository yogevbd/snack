package com.reactnativenavigation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.devsupport.JSDevSupport;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.reactnativenavigation.react.JsDevReloadHandlerFacade;
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
import androidx.appcompat.app.AppCompatActivity;

public class NavigationActivity extends ReactActivity implements JsDevReloadHandler.ReloadListener {
    private NavigationActivityDelegate activityDelegate;
    protected Navigator navigator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        navigator = new Navigator(this,
                new ChildControllersRegistry(),
                new ModalStack(this),
                new OverlayManager(),
                new RootPresenter()
        );
        navigator.bindViews();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getReactGateway().onConfigurationChanged(this, newConfig);
    }

    public ReactGateway getReactGateway() {
        return app().getReactGateway();
    }

    private NavigationApplication app() {
        return (NavigationApplication) getApplication();
    }

    public Navigator getNavigator() {
        return navigator;
    }

    @Override
    public void onReload() {
        getNavigator().destroyViews();
    }

    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        activityDelegate = new NavigationActivityDelegate(this);
        return activityDelegate;
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getNavigator().setContentLayout(findViewById(android.R.id.content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigator != null) {
            navigator.destroy();
        }
    }

    public void onCatalystInstanceDestroy() {
        runOnUiThread(() -> getNavigator().destroyViews());
    }
}
