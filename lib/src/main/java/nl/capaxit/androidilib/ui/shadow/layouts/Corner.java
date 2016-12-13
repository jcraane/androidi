package nl.capaxit.androidilib.ui.shadow.layouts;

import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by jamiecraane on 11/11/2016.
 */

public enum Corner {
    TOP_RIGHT(270) {
        @Override
        RadialGradient getRadialGradient(final int width, final int height, final int shadowHeight, final int[] colors) {
            return new RadialGradient(width, 0, shadowHeight * 2, colors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
        }

        @Override
        RectF getCornerRect(final int width, final int height, final int shadowHeight) {
            return new RectF(width - shadowHeight, shadowHeight * -1, width + shadowHeight, shadowHeight);
        }
    },
    BOTTOM_RIGHT(0) {
        @Override
        RadialGradient getRadialGradient(final int width, final int height, final int shadowHeight, final int[] colors) {
            return new RadialGradient(width, height, shadowHeight * 2, colors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
        }

        @Override
        RectF getCornerRect(final int width, final int height, final int shadowHeight) {
            return new RectF(width - shadowHeight, height - shadowHeight, width + shadowHeight, height + shadowHeight);
        }
    },
    BOTTOM_LEFT(90) {
        @Override
        RadialGradient getRadialGradient(final int width, final int height, final int shadowHeight, final int[] colors) {
            return new RadialGradient(0, height, shadowHeight * 2, colors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
        }

        @Override
        RectF getCornerRect(final int width, final int height, final int shadowHeight) {
            return new RectF(0 - shadowHeight, height - shadowHeight, shadowHeight, height + shadowHeight);
        }
    },
    TOP_LEFT(180) {
        @Override
        RadialGradient getRadialGradient(final int width, final int height, final int shadowHeight, final int[] colors) {
            return new RadialGradient(0, 0, shadowHeight * 2, colors, new float[]{0, 0.5f}, Shader.TileMode.CLAMP);
        }

        @Override
        RectF getCornerRect(final int width, final int height, final int shadowHeight) {
            return new RectF(0 - shadowHeight, 0 - shadowHeight, shadowHeight, shadowHeight);
        }
    };

    final int startAngle;

    Corner(final int startAngle) {
        this.startAngle = startAngle;
    }

    abstract RadialGradient getRadialGradient(int width, int height, int shadowHeight, int[] colors);

    abstract RectF getCornerRect(int width, int height, int shadowHeight);
}
