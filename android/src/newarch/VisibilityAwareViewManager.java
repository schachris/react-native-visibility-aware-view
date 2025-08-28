package com.visibilityawareview;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.EventDispatcher;

import java.util.HashMap;
import java.util.Map;

@ReactModule(name = VisibilityAwareViewManager.NAME)
public class VisibilityAwareViewManager extends ViewGroupManager<VisibilityAwareView> {

  public static final String NAME = "VisibilityAwareView";

  private final ReactApplicationContext applicationContext;

  private static final VisibilityAwareView.VisibilityAwareViewHandler ON_CHANGE_HANDLER = new VisibilityAwareView.VisibilityAwareViewHandler() {
    @Override
    public void viewDidEnterVisibleArea(VisibilityAwareView customView, WritableMap eventData) {
      ReactContext reactContext = (ReactContext) customView.getContext();
      int reactTag = customView.getId();

      EventDispatcher dispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, reactTag);
      dispatcher.dispatchEvent(
        new VisibilityAwareView.ChangeVisibilityEvent(reactTag, customView.isViewVisible(), eventData)
      );
//      reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher().dispatchEvent(
//        new VisibilityAwareView.ChangeVisibilityEvent(reactTag, customView.isViewVisible(), eventData)
//      );
    }

    @Override
    public void viewDidLeaveVisibleArea(VisibilityAwareView customView, WritableMap eventData) {
      ReactContext reactContext = (ReactContext) customView.getContext();
      int reactTag = customView.getId();
      UIManagerHelper.getEventDispatcherForReactTag(reactContext, reactTag).dispatchEvent(
        new VisibilityAwareView.ChangeVisibilityEvent(reactTag, customView.isViewVisible(), eventData)
      );
    }
  };

//  @Nullable
//  @Override
//  public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
//    return VisibilityAwareViewManagerImpl.getExportedBubblingEventTypeConstants();
//  }

  @Nullable
  public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
    Map<String, Object> baseEventTypeConstants = super.getExportedCustomBubblingEventTypeConstants();
    Map<String, Object> eventTypeConstants = baseEventTypeConstants == null ? new HashMap() : baseEventTypeConstants;
    ((Map)eventTypeConstants).putAll(
      MapBuilder.builder()
        .put("onBecomeVisible",
          MapBuilder.of("phasedRegistrationNames",
            MapBuilder.of("bubbled", "onBecomeVisible")))
        .put("onBecomeInvisible",
          MapBuilder.of("phasedRegistrationNames",
            MapBuilder.of("bubbled", "onBecomeInvisible"))).build());
          return (Map)eventTypeConstants;
  }

  public VisibilityAwareViewManager(ReactApplicationContext context) {
    super();
    this.applicationContext = context;
  }

  @Override
  public String getName() {
    return VisibilityAwareViewManagerImpl.NAME;
  }

  @NonNull
  @Override
  public VisibilityAwareView createViewInstance(@NonNull ThemedReactContext context) {
    return VisibilityAwareViewManagerImpl.createViewInstance(context, this.applicationContext, ON_CHANGE_HANDLER);
  }

  @Override
  public void receiveCommand(@NonNull VisibilityAwareView root, String commandId, @Nullable ReadableArray args) {
    switch (commandId){
      case "start":
        root.start();
        break;
      case "stop":
        root.stop();
        break;
    }
  }

  @Override
  protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull VisibilityAwareView view) {
    view.handler = ON_CHANGE_HANDLER;
  }

  @ReactProp(name = "accuracy", defaultFloat = 250f)
  public void setAccuracy(VisibilityAwareView view, float value) {
    VisibilityAwareViewManagerImpl.setTrackingAccuracy(view, value);
  }

  @ReactProp(name = "minVisibleArea", defaultFloat = 0.01f)
  public void setMinVisibleArea(VisibilityAwareView view, float value) {
    VisibilityAwareViewManagerImpl.setMinVisibleArea(view, value);
  }

  @ReactProp(name = "ignoreAppState", defaultBoolean = false)
  public void setIgnoreAppState(VisibilityAwareView view, boolean value) {
    VisibilityAwareViewManagerImpl.setIgnoreAppState(view, value);
  }


}
