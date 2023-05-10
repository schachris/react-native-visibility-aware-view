package com.visibilityawareview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;

import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

public class VisibilityAwareView extends ReactViewGroup implements View.OnAttachStateChangeListener, View.OnLayoutChangeListener, LifecycleEventListener {
  private static final long TIMER_ACCURACY = 250;

  private boolean isAppInBackground = false;
  private Boolean _attachedToWindow = false;

  private long _interval_accuracy;
  private double _minVisibleArea;
  private Handler _intervalHandler;
  private boolean _isVisible;
  private boolean _ignoreAppState;

  protected ReactApplicationContext reactApplicationContext;

  /**
   * Getters
   */

  public Boolean isViewVisible() {
    return _isVisible;
  }

  /**
   * Setters
   */

  public VisibilityAwareView(ThemedReactContext context, @Nullable ReactApplicationContext reactApplicationContext) {
    super(context);

    if(reactApplicationContext != null) {
      this.reactApplicationContext = reactApplicationContext;
      this.reactApplicationContext.addLifecycleEventListener(this);
    }
    _interval_accuracy = TIMER_ACCURACY;
    _minVisibleArea = 0.01;
    _ignoreAppState = false;

    this.addOnLayoutChangeListener(this);
    this.addOnAttachStateChangeListener(this);

    this.updateVisibilityIfNeeded();
  }

  public void setTrackingAccuracy(@Nullable double accuracy) {
    if (accuracy > 0) {
      _interval_accuracy = (long) accuracy;
    }
  }

  public void  setMinVisibleArea(@Nullable double minVisibleArea) {
    _minVisibleArea = minVisibleArea;
  }

  public void  setIgnoreAppState(@Nullable boolean ignoreAppState) {
    _ignoreAppState = ignoreAppState;
  }

  public void start() {
    this.startIntervalTrackingIfOnWindow();
  }

  public void stop(){
    this.stopIntervalForVisibilityCheck();
  }


  /**
   * Update / Check Visibility
   */

  private Rect getRectForViewInWindow(View v) {
    int[] locationOnWindow = new int[2];
    v.getLocationInWindow(locationOnWindow);
    Rect r = new Rect();
    v.getDrawingRect(r);
    int x = locationOnWindow[0];
    int y = locationOnWindow[1];
    int w = v.getMeasuredWidth();
    int h = v.getMeasuredHeight();
    return new Rect(x, y, x + w, y + h);
  }

  private boolean isViewVisibleWithinView(View view, View inView, int depth) {
    if (inView == null) {
      return true;
    }

    Rect viewR = this.getRectForViewInWindow(view);
    Rect inR = this.getRectForViewInWindow(inView);

    Boolean intersects = viewR.intersect(inR);
    if(intersects){
      Rect overlappingRect = new Rect();
      overlappingRect.setIntersect(viewR, inR);
      int overlappingArea = overlappingRect.width() * overlappingRect.height();
      int totalArea = view.getMeasuredWidth() * view.getMeasuredHeight();
      int totalArea_viewR = viewR.width() * viewR.height();
      float overlapPercentage = (float) overlappingArea / totalArea;
      if (overlapPercentage < _minVisibleArea) {
        return false;
      }
    }
    if (intersects) {
      if (inView.getParent() instanceof View) {
        return this.isViewVisibleWithinView(view, (View) inView.getParent(), depth + 1);
      } else {
        return true;
      }
    }
    return false;
  }

  private Boolean isViewVisibleWithinScreen() {
    Boolean hasWindow = getWindowToken() != null;
    if (!hasWindow) {
      return false;
    }

    boolean visible = this.isViewVisibleWithinView(this, (View) this.getParent(), 0);
    return visible;
  }

  public void updateVisibilityIfNeeded() {
    Boolean attached = this._attachedToWindow;
    Boolean frameVisible = attached && this.isViewVisibleWithinScreen();
    Boolean nextState;
    if(_ignoreAppState){
      nextState = attached && frameVisible;
    }else {
      nextState = !this.isAppInBackground && attached && frameVisible;
    }
    if (this.isViewVisible() != nextState) {
      this.updateVisibility(nextState, frameVisible);
    }
  }

  private void updateVisibility(Boolean nextState, Boolean frameVisible) {
    if (this.isViewVisible() != nextState) {
      _isVisible = nextState;
      if (_isVisible) {
        this.startIntervalTrackingIfNotRunningAlready();
      } else {
      }

      this.fireOnChangeVisibilityEvent(frameVisible);
    }
  }


  /**
   * Fire Callbacks + helper
   */

  public VisibilityAwareViewHandler handler;

  interface VisibilityAwareViewHandler {
    void viewDidEnterVisibleArea(VisibilityAwareView customView, WritableMap eventData);
    void viewDidLeaveVisibleArea(VisibilityAwareView customView, WritableMap eventData);
  }

  public static class ChangeVisibilityEvent extends Event<ChangeVisibilityEvent> {
    public static final String BECOME_VISIBLE_EVENT_NAME = "topOnBecomeVisible";
    public static final String BECOME_INVISIBLE_EVENT_NAME = "topOnBecomeInvisible";

    private final boolean isVisible;
    private final WritableMap eventData;

    public ChangeVisibilityEvent(int viewId, boolean isVisible, WritableMap data) {
      super(viewId);
      this.isVisible = isVisible;
      this.eventData = data;
    }

    @Override
    public String getEventName() {
      return this.isVisible ? BECOME_VISIBLE_EVENT_NAME : BECOME_INVISIBLE_EVENT_NAME;
    }

    @Override
    public short getCoalescingKey() {
      return 0;
    }

    @Override
    public boolean canCoalesce() {
      return false;
    }

    @Nullable
    @Override
    protected WritableMap getEventData() {
      return serializeEventData();
    }

    private WritableMap serializeEventData() {
      eventData.putInt("target", getViewTag());
      return eventData;
    }
  }



  private void fireOnChangeVisibilityEvent(Boolean frameVisible) {
    WritableMap eventData = Arguments.createMap();
    eventData.putBoolean("frame_visible", frameVisible);
    if (this.isViewVisible()){
      eventData.putBoolean("app_open", !this.isAppInBackground);
    }else{
      eventData.putBoolean("app_closed", this.isAppInBackground);
    }

    if(this.handler != null){
      if (this.isViewVisible()){
        this.handler.viewDidEnterVisibleArea(this, eventData);
      }else{
        this.handler.viewDidLeaveVisibleArea(this, eventData);
      }
    }

  }

  /**
   * Timer Handling
   */
  private void startIntervalForVisibilityCheck() {

    if (this._intervalHandler != null) {
      this._intervalHandler.removeCallbacksAndMessages(null);
      this._intervalHandler = null;
    }
    this._intervalHandler = new Handler();
    this._intervalHandler.post(new Runnable() {
      @Override
      public void run() {
        updateVisibilityIfNeeded();
        if (_intervalHandler != null) {
          _intervalHandler.postDelayed(this, _interval_accuracy);
        }
      }
    });

  }

  private void startIntervalTrackingIfNotRunningAlready() {
    if (this._intervalHandler == null) {
      this.startIntervalForVisibilityCheck();
    }
  }

  private void startIntervalTrackingIfOnWindow() {
    if (getWindowToken() != null) {
      this.startIntervalTrackingIfNotRunningAlready();
    }
  }

  private void stopIntervalForVisibilityCheck() {
    if (this._intervalHandler != null) {
      this._intervalHandler.removeCallbacksAndMessages(null);
    }
    this._intervalHandler = null;
  }

  /**
   * Functions which should cause a visibility check
   */

  @Override
  public void onViewAttachedToWindow(@NonNull View view) {
    this._attachedToWindow = true;
    this.updateVisibilityIfNeeded();
    this.startIntervalTrackingIfOnWindow();
  }

  @Override
  public void onViewDetachedFromWindow(@NonNull View view) {
    this._attachedToWindow = false;
    this.updateVisibilityIfNeeded();
    this.stopIntervalForVisibilityCheck();
  }

  @Override
  public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
    this.updateVisibilityIfNeeded();
  }

  @Override
  public void onHostResume() {
    this.isAppInBackground = false;
    this.updateVisibilityIfNeeded();
    this.startIntervalTrackingIfOnWindow();
  }

  @Override
  public void onHostPause() {
    this.isAppInBackground = true;
    this.stopIntervalForVisibilityCheck();
    this.updateVisibilityIfNeeded();
  }

  @Override
  public void onHostDestroy() {
    this.stopIntervalForVisibilityCheck();
    this.removeOnLayoutChangeListener(this);
    this.removeOnAttachStateChangeListener(this);
    if(this.reactApplicationContext != null){
      this.reactApplicationContext.removeLifecycleEventListener(this);
    }
  }
}

