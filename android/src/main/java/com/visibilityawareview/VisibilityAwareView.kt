package com.visibilityawareview

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.View
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.views.view.ReactViewGroup

class VisibilityAwareView(
  context: ThemedReactContext,
  private val reactContext: ReactApplicationContext?
) : ReactViewGroup(context),
  View.OnAttachStateChangeListener,
  View.OnLayoutChangeListener,
  LifecycleEventListener {

  companion object {
    private const val TIMER_ACCURACY = 250L
  }

  private var isAppInBackground = false
  private var attachedToWindow = false

  private var intervalAccuracy = TIMER_ACCURACY
  private var minVisibleArea = 0.01
  private var intervalHandler: Handler? = null
  private var isVisible = false
  private var ignoreAppState = false

  var handler: VisibilityAwareViewHandler? = null

  init {
    reactContext?.addLifecycleEventListener(this)
    addOnLayoutChangeListener(this)
    addOnAttachStateChangeListener(this)
    updateVisibilityIfNeeded()
  }

  fun start() = startIntervalTrackingIfOnWindow()
  fun stop() = stopInterval()

  fun setTrackingAccuracy(value: Double?) {
    if (value != null && value > 0) {
      intervalAccuracy = value.toLong()
    }
  }

  fun setMinVisibleArea(value: Double?) {
    if (value != null) minVisibleArea = value
  }

  fun setIgnoreAppState(value: Boolean?) {
    ignoreAppState = value ?: false
  }

  private fun getRect(view: View): Rect {
    val loc = IntArray(2)
    view.getLocationInWindow(loc)
    return Rect(loc[0], loc[1], loc[0] + view.width, loc[1] + view.height)
  }

  private fun isVisibleInParent(view: View, parent: View?): Boolean {
    parent ?: return true

    val viewRect = getRect(view)
    val parentRect = getRect(parent)

    if (!viewRect.intersect(parentRect)) return false

    val overlap = Rect()
    overlap.setIntersect(viewRect, parentRect)

    val overlapArea = overlap.width() * overlap.height()
    val totalArea = view.width * view.height

    if (totalArea > 0) {
      val percent = overlapArea.toFloat() / totalArea
      if (percent < minVisibleArea) return false
    }

    return if (parent.parent is View) {
      isVisibleInParent(view, parent.parent as View)
    } else true
  }

  private fun isVisibleOnScreen(): Boolean {
    if (windowToken == null) return false
    return isVisibleInParent(this, parent as? View)
  }

  fun updateVisibilityIfNeeded() {
    val frameVisible = attachedToWindow && isVisibleOnScreen()

    val next = if (ignoreAppState) {
      attachedToWindow && frameVisible
    } else {
      !isAppInBackground && attachedToWindow && frameVisible
    }

    if (isVisible != next) {
      isVisible = next
      fireEvent(frameVisible)
    }
  }

  private fun fireEvent(frameVisible: Boolean) {
    val map = Arguments.createMap().apply {
      putBoolean("frame_visible", frameVisible)
      if (isVisible) {
        putBoolean("app_open", !isAppInBackground)
      } else {
        putBoolean("app_closed", isAppInBackground)
      }
    }

    if (isVisible) {
      handler?.viewDidEnterVisibleArea(this, map)
    } else {
      handler?.viewDidLeaveVisibleArea(this, map)
    }
  }

  private fun startIntervalTrackingIfOnWindow() {
    if (windowToken != null && intervalHandler == null) {
      intervalHandler = Handler(Looper.getMainLooper())
      intervalHandler?.post(object : Runnable {
        override fun run() {
          updateVisibilityIfNeeded()
          intervalHandler?.postDelayed(this, intervalAccuracy)
        }
      })
    }
  }

  private fun stopInterval() {
    intervalHandler?.removeCallbacksAndMessages(null)
    intervalHandler = null
  }

  override fun onViewAttachedToWindow(v: View) {
    attachedToWindow = true
    updateVisibilityIfNeeded()
    startIntervalTrackingIfOnWindow()
  }

  override fun onViewDetachedFromWindow(v: View) {
    attachedToWindow = false
    stopInterval()
    updateVisibilityIfNeeded()
  }

  override fun onLayoutChange(
    v: View, l: Int, t: Int, r: Int, b: Int,
    oldL: Int, oldT: Int, oldR: Int, oldB: Int
  ) {
    updateVisibilityIfNeeded()
  }

  override fun onHostResume() {
    isAppInBackground = false
    updateVisibilityIfNeeded()
    startIntervalTrackingIfOnWindow()
  }

  override fun onHostPause() {
    isAppInBackground = true
    stopInterval()
    updateVisibilityIfNeeded()
  }

  override fun onHostDestroy() {
    stopInterval()
    removeOnLayoutChangeListener(this)
    removeOnAttachStateChangeListener(this)
    reactContext?.removeLifecycleEventListener(this)
  }

  interface VisibilityAwareViewHandler {
    fun viewDidEnterVisibleArea(view: VisibilityAwareView, data: WritableMap)
    fun viewDidLeaveVisibleArea(view: VisibilityAwareView, data: WritableMap)
  }
}