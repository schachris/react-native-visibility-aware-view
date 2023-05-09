package com.visibilityawareview;

import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.react.uimanager.ViewGroupManager;

public abstract class VisibilityAwareViewManagerSpec<T extends ViewGroup> extends ViewGroupManager<T> {
  public abstract void setTrackingAccuracy(T view, @Nullable double accuracy);
  public abstract void setMinVisibleArea(T view, @Nullable double minVisibleArea);
  public abstract void setIgnoreAppState(T view, @Nullable boolean ignoreAppState);
}
