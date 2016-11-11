package nl.capaxit.androidilib.ui.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import nl.capaxit.androidilib.R;

/**
 * Created by jamiecraane on 10/11/2016.
 */
public class ShadowWrapperFrameLayout extends FrameLayout {
    private static final int DEFAULT_SHADOW_HEIGHT = 3; // dp;
    private Drawable shadowDrawable;
    private int shadowHeight = -1;

    public ShadowWrapperFrameLayout(final Context context) {
        this(context, null);
    }

    public ShadowWrapperFrameLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowWrapperFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShadowWrapperFrameLayout);
        if (attributes != null) {
            shadowHeight = attributes.getDimensionPixelSize(R.styleable.ShadowWrapperFrameLayout_capaxitShadowWrapperShadowHeight, -1);
            attributes.recycle();
        }

        shadowDrawable = ContextCompat.getDrawable(context, R.drawable.shadow_top);
        if (shadowHeight == -1) {
            final float density = context.getResources().getDisplayMetrics().density;
            shadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
        }

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final Rect shadowRect = new Rect(0, 0, getWidth(), shadowHeight);
        shadowDrawable.setBounds(shadowRect);
        Rect newRect = new Rect();
        canvas.getClipBounds(newRect);
        newRect.inset(0, -1 * shadowHeight);  //make the rect larger
        canvas.clipRect(newRect, Region.Op.REPLACE);
        canvas.save();
        canvas.translate(0, -1 * shadowHeight);
        shadowDrawable.draw(canvas);
        canvas.restore();
    }
}
