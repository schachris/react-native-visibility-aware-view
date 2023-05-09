package com.visibilityawareview;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

@ReactModule(name = VisibilityAwareViewManager.NAME)
public class VisibilityAwareViewManager extends VisibilityAwareViewManagerSpec<VisibilityAwareView> {

  public static final String NAME = "VisibilityAwareView";

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public VisibilityAwareView createViewInstance(ThemedReactContext context) {
    return new VisibilityAwareView(context);
  }

  @Override
  @ReactProp(name = "color")
  public void setColor(VisibilityAwareView view, @Nullable String color) {
    view.setBackgroundColor(Color.parseColor(color));
  }
}
