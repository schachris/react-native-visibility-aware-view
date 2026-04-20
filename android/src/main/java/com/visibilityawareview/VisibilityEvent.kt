package com.visibilityawareview

import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.events.Event

class VisibilityEvent(
  viewId: Int,
  private val visible: Boolean,
  private val data: WritableMap
) : Event<VisibilityEvent>(viewId) {

  override fun getEventName(): String =
    if (visible) "onBecomeVisible" else "onBecomeInvisible"

  override fun canCoalesce() = false

  override fun getEventData(): WritableMap {
    data.putInt("target", viewTag)
    return data
  }
}