package com.visibilityawareview

import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.*
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.EventDispatcher

@ReactModule(name = VisibilityAwareViewManager.NAME)
class VisibilityAwareViewManager(
  private val reactContext: ReactApplicationContext
) : ViewGroupManager<VisibilityAwareView>() {

  companion object {
    const val NAME = "VisibilityAwareView"
  }

  override fun getName() = NAME

  override fun createViewInstance(context: ThemedReactContext): VisibilityAwareView {
    return VisibilityAwareView(context, reactContext).apply {
      handler = eventHandler
    }
  }

  private val eventHandler = object : VisibilityAwareView.VisibilityAwareViewHandler {
    override fun viewDidEnterVisibleArea(view: VisibilityAwareView, data: WritableMap) {
      dispatch(view, true, data)
    }

    override fun viewDidLeaveVisibleArea(view: VisibilityAwareView, data: WritableMap) {
      dispatch(view, false, data)
    }

    private fun dispatch(view: VisibilityAwareView, visible: Boolean, data: WritableMap) {
      val dispatcher: EventDispatcher? =
        UIManagerHelper.getEventDispatcherForReactTag(
          view.context as ReactContext,
          view.id
        )

      dispatcher?.dispatchEvent(
          VisibilityEvent(view.id, visible, data)
      )
    }
  }

  // ✅ modern command handling
  override fun getCommandsMap(): Map<String, Int> =
    mapOf("start" to 1, "stop" to 2)

  override fun receiveCommand(view: VisibilityAwareView, commandId: Int, args: ReadableArray?) {
    when (commandId) {
      1 -> view.start()
      2 -> view.stop()
    }
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any> =
    MapBuilder.builder<String, Any>()
      .put(
        "onBecomeVisible",
        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onBecomeVisible"))
      )
      .put(
        "onBecomeInvisible",
        MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onBecomeInvisible"))
      )
      .build()

  @ReactProp(name = "accuracy", defaultFloat = 250f)
  fun setAccuracy(view: VisibilityAwareView, value: Float) {
    view.setTrackingAccuracy(value.toDouble())
  }

  @ReactProp(name = "minVisibleArea", defaultFloat = 0.01f)
  fun setMinVisibleArea(view: VisibilityAwareView, value: Float) {
    view.setMinVisibleArea(value.toDouble())
  }

  @ReactProp(name = "ignoreAppState", defaultBoolean = false)
  fun setIgnoreAppState(view: VisibilityAwareView, value: Boolean) {
    view.setIgnoreAppState(value)
  }
}
