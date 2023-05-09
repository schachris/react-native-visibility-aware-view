package com.visibilityawareview;

import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.viewmanagers.VisibilityAwareViewManagerDelegate;
import com.facebook.react.viewmanagers.VisibilityAwareViewManagerInterface;
import com.facebook.soloader.SoLoader;

public abstract class VisibilityAwareViewManagerSpec<T extends View> extends SimpleViewManager<T> implements VisibilityAwareViewManagerInterface<T> {
  static {
    if (BuildConfig.CODEGEN_MODULE_REGISTRATION != null) {
      SoLoader.loadLibrary(BuildConfig.CODEGEN_MODULE_REGISTRATION);
    }
  }

  private final ViewManagerDelegate<T> mDelegate;

  public VisibilityAwareViewManagerSpec() {
    mDelegate = new VisibilityAwareViewManagerDelegate(this);
  }

  @Nullable
  @Override
  protected ViewManagerDelegate<T> getDelegate() {
    return mDelegate;
  }
}
