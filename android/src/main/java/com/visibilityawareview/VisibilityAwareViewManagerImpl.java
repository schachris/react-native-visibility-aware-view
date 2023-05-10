package com.visibilityawareview;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class VisibilityAwareViewManagerImpl {
  public static final String NAME = "VisibilityAwareView";

  public static VisibilityAwareView createViewInstance(ThemedReactContext context, ReactApplicationContext reactContext, VisibilityAwareView.VisibilityAwareViewHandler handler) {
    VisibilityAwareView view = new VisibilityAwareView(context,reactContext);
    view.handler = handler;
    return view;
  }

  public static void setTrackingAccuracy(VisibilityAwareView view, double accuracy) {
    view.setTrackingAccuracy(accuracy);
  }

  public static void setMinVisibleArea(VisibilityAwareView view, double minVisibleArea) {
    view.setMinVisibleArea(minVisibleArea);
  }

  public static void setIgnoreAppState(VisibilityAwareView view, boolean ignoreAppState) {
    view.setIgnoreAppState(ignoreAppState);
  }

}
