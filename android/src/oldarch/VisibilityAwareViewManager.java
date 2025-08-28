package com.visibilityawareview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = VisibilityAwareViewManager.NAME)
public class VisibilityAwareViewManager extends ViewGroupManager<VisibilityAwareView> {

  public static final String NAME = VisibilityAwareViewManagerImpl.NAME;

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
    return VisibilityAwareViewManagerImpl.createViewInstance(context, this.applicationContext, ON_CHANGE_HANDLER);
  }

  private static final VisibilityAwareView.VisibilityAwareViewHandler ON_CHANGE_HANDLER = new VisibilityAwareView.VisibilityAwareViewHandler() {
    @Override
    public void viewDidEnterVisibleArea(VisibilityAwareView customView, WritableMap eventData) {
      ReactContext reactContext = (ReactContext) customView.getContext();
      int reactTag = customView.getId();
      reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
        new VisibilityAwareView.ChangeVisibilityEvent(reactTag, customView.isViewVisible(), eventData)
      );
    }

    @Override
    public void viewDidLeaveVisibleArea(VisibilityAwareView customView, WritableMap eventData) {
      ReactContext reactContext = (ReactContext) customView.getContext();
      int reactTag = customView.getId();
      reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
        new VisibilityAwareView.ChangeVisibilityEvent(reactTag, customView.isViewVisible(), eventData)
      );
    }
  };

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
    Map<String, Object> baseEventTypeConstants = super.getExportedCustomBubblingEventTypeConstants();
    Map<String, Object> eventTypeConstants = baseEventTypeConstants == null ? new HashMap() : baseEventTypeConstants;
    ((Map)eventTypeConstants).putAll(MapBuilder.builder()
      .put(
        VisibilityAwareView.ChangeVisibilityEvent.BECOME_VISIBLE_EVENT_NAME,
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onBecomeVisible")
        )
      )
      .put(
        VisibilityAwareView.ChangeVisibilityEvent.BECOME_INVISIBLE_EVENT_NAME,
        MapBuilder.of(
          "phasedRegistrationNames",
          MapBuilder.of("bubbled", "onBecomeInvisible")
        )
      )
      .build());
    return (Map)eventTypeConstants;
  }

  @ReactProp(name = "accuracy")
  public void setTrackingAccuracy(VisibilityAwareView view, @Nullable double accuracy) {
    VisibilityAwareViewManagerImpl.setTrackingAccuracy(view, accuracy);
  }

  @ReactProp(name = "minVisibleArea")
  public void setMinVisibleArea(VisibilityAwareView view, @Nullable double minVisibleArea) {
    VisibilityAwareViewManagerImpl.setMinVisibleArea(view, minVisibleArea);
  }

  @ReactProp(name = "ignoreAppState")
  public void setIgnoreAppState(VisibilityAwareView view, @Nullable boolean ignoreAppState) {
    VisibilityAwareViewManagerImpl.setIgnoreAppState(view, ignoreAppState);
  }

}
