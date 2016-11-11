package nl.capaxit.androidilib.ui.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import nl.capaxit.androidilib.R;

/**
 * FrameLayout which is able to draw shadows around its outer edges. Example:
 * <p>
 * <code>
 * <nl.capaxit.androidilib.ui.shadow.ShadowWrapperFrameLayout
 * android:layout_marginTop="20dp"
 * android:layout_width="100dp"
 * android:layout_height="100dp"
 * android:padding="8dp"
 * android:layout_gravity="center_horizontal"
 * android:background="#3f50"
 * app:capaxitShadowWrapperShadowSide="bottom|top|left|right"
 * app:capaxitShadowWrapperShadowHeight="4dp">
 * <p>
 * <TextView
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:text="Demo"/>
 * </nl.capaxit.androidilib.ui.shadow.ShadowWrapperFrameLayout>
 * </code>
 * <p>
 * capaxitShadowWrapperShadowHeight controls the size of the shadow.
 * capaxitShadowWrapperShadowSide controls to which sides a shadow is applied.
 * <p>
 * <p>
 * Created by jamiecraane on 10/11/2016.
 */
public class ShadowWrapperFrameLayout extends FrameLayout {
    private static final int DEFAULT_SHADOW_HEIGHT = 3; // dp;
    private int shadowHeight = -1;
    private final EnumSet<Side> sides = EnumSet.noneOf(Side.class);
    private final Map<Side, Drawable> shadowDrawables = new EnumMap<>(Side.class);
    private final Rect clipBounds = new Rect();
    private Paint edgePaint;
    private int[] gradientColors = new int[2];

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
        edgePaint = createEdgePaint();
        gradientColors[0] = ContextCompat.getColor(getContext(), R.color.gradient_start_color);
        gradientColors[1] = ContextCompat.getColor(getContext(), android.R.color.transparent);

        if (shadowHeight == -1) {
            final float density = context.getResources().getDisplayMetrics().density;
            shadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density + 0.5f);
        }

        setWillNotDraw(false);
    }

    private Paint createEdgePaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#ff5000"));
        paint.setStyle(Paint.Style.FILL);
        return paint;
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

        if (sides.containsAll(Arrays.asList(Side.TOP, Side.RIGHT))) {
            final RadialGradient radialGradient = new RadialGradient(getWidth(), 0, shadowHeight * 2, gradientColors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
            edgePaint.setShader(radialGradient);
            canvas.drawArc(new RectF(getWidth() - shadowHeight, shadowHeight * -1, getWidth() + shadowHeight, 0 + shadowHeight), 270, 90, true, edgePaint);
        }
        if (sides.containsAll(Arrays.asList(Side.BOTTOM, Side.RIGHT))) {
            final RadialGradient radialGradient = new RadialGradient(getWidth(), getHeight(), shadowHeight * 2, gradientColors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
            edgePaint.setShader(radialGradient);
            canvas.drawArc(new RectF(getWidth() - shadowHeight, getHeight() - shadowHeight, getWidth() + shadowHeight, getHeight() + shadowHeight), 0, 90, true, edgePaint);
        }
        if (sides.containsAll(Arrays.asList(Side.TOP, Side.LEFT))) {
            final RadialGradient radialGradient = new RadialGradient(0, 0, shadowHeight * 2, gradientColors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
            edgePaint.setShader(radialGradient);
            canvas.drawArc(new RectF(0 - shadowHeight, 0 - shadowHeight, shadowHeight, shadowHeight), 180, 90, true, edgePaint
            );
        }
        if (sides.containsAll(Arrays.asList(Side.BOTTOM, Side.LEFT))) {
            final RadialGradient radialGradient = new RadialGradient(0, getHeight(), shadowHeight * 2, gradientColors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
            edgePaint.setShader(radialGradient);
            canvas.drawArc(new RectF(0 - shadowHeight, getHeight() - shadowHeight, 0 + shadowHeight, getHeight() + shadowHeight), 90, 90, true, edgePaint);
        }
    }

}
