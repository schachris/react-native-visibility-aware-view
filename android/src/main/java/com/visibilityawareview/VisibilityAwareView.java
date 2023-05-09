package com.visibilityawareview;

import androidx.annotation.Nullable;

import android.content.Context;
import android.util.AttributeSet;

import android.view.View;

public class VisibilityAwareView extends View {

  public VisibilityAwareView(Context context) {
    super(context);
  }

  public VisibilityAwareView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public VisibilityAwareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

}
