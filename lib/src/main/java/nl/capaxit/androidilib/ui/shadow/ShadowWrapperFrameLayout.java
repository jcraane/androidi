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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import nl.capaxit.androidilib.R;

/**
 * todo shadow is not present on the corners at the moment when two shadows come together. Perhaps we can solve this by drawing a radial gradient at the corners where two shadows collide.
 *
 * Created by jamiecraane on 10/11/2016.
 */
public class ShadowWrapperFrameLayout extends FrameLayout {
    private static final int DEFAULT_SHADOW_HEIGHT = 3; // dp;
    private int shadowHeight = -1;
    private final EnumSet<Side> sides = EnumSet.noneOf(Side.class);
    private final Map<Side, Drawable> shadowDrawables = new EnumMap<>(Side.class);
    private final Rect clipBounds = new Rect();

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
            final int shadowSides = attributes.getInt(R.styleable.ShadowWrapperFrameLayout_capaxitShadowWrapperShadowSide, -1);
//            Convert attribute flags to enumset. The values of the enum correspond to the values in the attrs.xml.
            for (final Side side : Side.values()) {
                if ((shadowSides & side.value) == side.value) {
                    sides.add(side);
                }
            }
            attributes.recycle();
        }

        shadowDrawables.put(Side.TOP, ContextCompat.getDrawable(context, R.drawable.shadow_top));
        shadowDrawables.put(Side.LEFT, ContextCompat.getDrawable(context, R.drawable.shadow_left));
        shadowDrawables.put(Side.BOTTOM, ContextCompat.getDrawable(context, R.drawable.shadow_bottom));
        shadowDrawables.put(Side.RIGHT, ContextCompat.getDrawable(context, R.drawable.shadow_right));

        if (shadowHeight == -1) {
            final float density = context.getResources().getDisplayMetrics().density;
            shadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
        }

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (final Side side : sides) {
            final Rect shadowRect = side.getShadowRect(getWidth(), getHeight(), shadowHeight);
            final Drawable shadowDrawable = shadowDrawables.get(side);
            shadowDrawable.setBounds(shadowRect);
            canvas.getClipBounds(clipBounds);
            side.inset(clipBounds, shadowHeight);
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
            shadowDrawable.draw(canvas);
        }
    }

    private enum Side {
        TOP(1) {
            @Override
            Rect getShadowRect(final int width, final int height, final int shadowHeight) {
                return new Rect(0, -1 * shadowHeight, width, 0);
            }

            @Override
            void inset(final Rect rect, int shadowHeight) {
                rect.inset(0, -1 * shadowHeight);
            }
        }, LEFT(2) {
            @Override
            Rect getShadowRect(final int width, final int height, final int shadowHeight) {
                return new Rect(0 - shadowHeight, 0, 0, height);
            }

            @Override
            void inset(final Rect rect, int shadowHeight) {
                rect.inset(-1 * shadowHeight, 0);
            }
        }, BOTTOM(4) {
            @Override
            Rect getShadowRect(final int width, final int height, final int shadowHeight) {
                return new Rect(0, height, width, height + shadowHeight);
            }

            @Override
            void inset(final Rect rect, int shadowHeight) {
                rect.inset(0, -1 * shadowHeight);
            }
        }, RIGHT(8) {
            @Override
            Rect getShadowRect(final int width, final int height, final int shadowHeight) {
                return new Rect(width, 0, width + shadowHeight, height);
            }

            @Override
            void inset(final Rect rect, int shadowHeight) {
                rect.inset(-1 * shadowHeight, 0);
            }
        };

        private final int value;

        Side(final int value) {
            this.value = value;
        }

        abstract Rect getShadowRect(int width, int height, int shadowHeight);

        abstract void inset(Rect rect, int shadowHeight);
    }
}
