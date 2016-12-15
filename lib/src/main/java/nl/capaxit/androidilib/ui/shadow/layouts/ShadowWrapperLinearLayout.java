package nl.capaxit.androidilib.ui.shadow.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import nl.capaxit.androidilib.R;

import java.util.EnumSet;

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
 * <p>
 * todo: small optimization. Divide arcs in two so we get nice 45 degrees edges.
 * <p>
 * Created by jamiecraane on 10/11/2016.
 */
public class ShadowWrapperLinearLayout extends LinearLayout {
    private static final int DEFAULT_SHADOW_HEIGHT = 3; // dp;
    private static final int CORNER_SHADOW_SWEEP_ANGLE = 90;
    private int shadowHeight = -1;
    private final EnumSet<Side> sides = EnumSet.noneOf(Side.class);
    private final Rect clipBounds = new Rect();
    private final Paint cornerPaint;
    private final Paint shadowEdgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int[] gradientColors = new int[2];

    public ShadowWrapperLinearLayout(final Context context) {
        this(context, null);
    }

    public ShadowWrapperLinearLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowWrapperLinearLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ShadowWrapperLinearLayout);
        if (attributes != null) {
            shadowHeight = attributes.getDimensionPixelSize(R.styleable.ShadowWrapperLinearLayout_capaxitShadowWrapperLinearShadowHeight, -1);
            final int shadowSides = attributes.getInt(R.styleable.ShadowWrapperLinearLayout_capaxitShadowWrapperLinearShadowSide, -1);
//            Convert attribute flags to enumset. The values of the enum correspond to the values in the attrs.xml.
            for (final Side side : Side.values()) {
                if ((shadowSides & side.value) == side.value) {
                    sides.add(side);
                }
            }
            attributes.recycle();
        }

        cornerPaint = createCornerPaint();
        gradientColors[0] = ContextCompat.getColor(getContext(), R.color.gradient_start_color);
        gradientColors[1] = ContextCompat.getColor(getContext(), android.R.color.transparent);

        if (shadowHeight == -1) {
            final float density = context.getResources().getDisplayMetrics().density;
            shadowHeight = (int) (DEFAULT_SHADOW_HEIGHT * density);
        }

        setWillNotDraw(false);
    }

    private Paint createCornerPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (final Side side : sides) {
            final Rect shadowRect = side.getShadowRect(getWidth(), getHeight(), shadowHeight);
            canvas.getClipBounds(clipBounds);
            side.inset(clipBounds, shadowHeight);
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
            shadowEdgePaint.setShader(side.getShadowGradient(shadowHeight, getWidth(), getHeight(), gradientColors));
            canvas.drawRect(shadowRect, shadowEdgePaint);
        }

        drawCorners(canvas);
    }

    private void drawCorners(final Canvas canvas) {
        final EnumSet<Corner> cornersToDraw = EnumSet.noneOf(Corner.class);
        if (sides.contains(Side.TOP) && sides.contains(Side.RIGHT)) {
            cornersToDraw.add(Corner.TOP_RIGHT);
        }
        if (sides.contains(Side.BOTTOM) && sides.contains(Side.RIGHT)) {
            cornersToDraw.add(Corner.BOTTOM_RIGHT);
        }
        if (sides.contains(Side.TOP) && sides.contains(Side.LEFT)) {
            cornersToDraw.add(Corner.TOP_LEFT);
        }
        if (sides.contains(Side.BOTTOM) && sides.contains(Side.LEFT)) {
            cornersToDraw.add(Corner.BOTTOM_LEFT);
        }

        for (final Corner corner : cornersToDraw) {
            drawCornerShadow(canvas, corner, corner.startAngle);
        }
    }

    private void drawCornerShadow(final Canvas canvas, final Corner corner, final float startAngle) {
        cornerPaint.setShader(corner.getRadialGradient(getWidth(), getHeight(), shadowHeight, gradientColors));
        canvas.drawArc(corner.getCornerRect(getWidth(), getHeight(), shadowHeight), startAngle, (float) ShadowWrapperLinearLayout.CORNER_SHADOW_SWEEP_ANGLE, true, cornerPaint);
    }

    public void setShadowHeight(final int shadowHeight) {
        this.shadowHeight = shadowHeight;
        invalidate();
        requestLayout();
    }

    public void setSides(final EnumSet<Side> sides) {
        if (sides != null) {
            this.sides.clear();
            this.sides.addAll(sides);
            invalidate();
            requestLayout();
        }
    }
}
