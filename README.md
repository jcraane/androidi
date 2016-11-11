# androidi
Common Android utilities library.

Contains the following:
* ShadowWrapperFrameLayout
    FrameLayout extention which supports drawing gradient shadows around the edges. When two edges have joining shadows, for example top|left, a shadow is rendered in the corresponding corner.

Example code:
```
<nl.capaxit.androidilib.ui.shadow.ShadowWrapperFrameLayout
        android:layout_marginTop="20dp"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:padding="8dp"
        android:layout_gravity="center_horizontal"
        android:background="#3f50"
        app:capaxitShadowWrapperShadowSide="bottom|top|left|right"
        app:capaxitShadowWrapperShadowHeight="4dp">
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="all"/>
</nl.capaxit.androidilib.ui.shadow.ShadowWrapperFrameLayout>
```

Output: <img src="/docs/img/dynamicShadows.png">
