package com.visibilityawareview;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

@ReactModule(name = VisibilityAwareViewManager.NAME)
public class VisibilityAwareViewManager extends com.visibilityawareview.VisibilityAwareViewManagerSpec<VisibilityAwareView> {

  public static final String NAME = "VisibilityAwareView";

  private final ReactApplicationContext applicationContext;

  public VisibilityAwareViewManager(ReactApplicationContext context) {
    super();
    this.applicationContext = context;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public VisibilityAwareView createViewInstance(ThemedReactContext context) {
    return new VisibilityAwareView(context, applicationContext);
  }

  public static final int COMMAND_START = 1;
  public static final int COMMAND_STOP = 2;

  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of("start", COMMAND_START, "stop", COMMAND_STOP);
  }

  @Override
  public void receiveCommand(@NonNull VisibilityAwareView view, int commandId, @Nullable ReadableArray args) {
    super.receiveCommand(view, commandId, args);
    switch (commandId) {
      case COMMAND_START:
        view.start();
        break;
      case COMMAND_STOP:
        view.stop();
        break;
    }
  }

  @Nullable
  @Override
  public Map getExportedCustomBubblingEventTypeConstants() {
    MapBuilder.Builder builder = MapBuilder.builder();

    builder.put(
      VisibilityAwareView.ChangeVisibilityEvent.BECOME_VISIBLE_EVENT_NAME,
      MapBuilder.of(
        "phasedRegistrationNames",
        MapBuilder.of("bubbled", VisibilityAwareView.ChangeVisibilityEvent.BECOME_VISIBLE_EVENT_NAME)
      )
    );
    builder.put(
      VisibilityAwareView.ChangeVisibilityEvent.BECOME_INVISIBLE_EVENT_NAME,
      MapBuilder.of(
        "phasedRegistrationNames",
        MapBuilder.of("bubbled", VisibilityAwareView.ChangeVisibilityEvent.BECOME_INVISIBLE_EVENT_NAME)
      )
    );

    return builder.build();
  }

  @Override
  @ReactProp(name = "accuracy")
  public void setTrackingAccuracy(VisibilityAwareView view, @Nullable double accuracy) {
    view.setTrackingAccuracy(accuracy);
  }

  @Override
  @ReactProp(name = "minVisibleArea")
  public void setMinVisibleArea(VisibilityAwareView view, @Nullable double minVisibleArea) {
    view.setMinVisibleArea(minVisibleArea);
  }

  @Override
  @ReactProp(name = "ignoreAppState")
  public void setIgnoreAppState(VisibilityAwareView view, @Nullable boolean ignoreAppState) {
    view.setIgnoreAppState(ignoreAppState);
  }
}
