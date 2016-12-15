package nl.capaxit.androidilib.ui.shadow.layouts;

import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by jamiecraane on 11/11/2016.
 */
public enum Side {
    TOP(1) {
        @Override
        Rect getShadowRect(final int width, final int height, final int shadowHeight) {
            return new Rect(0, -1 * shadowHeight, width, 0);
        }

        @Override
        void inset(final Rect rect, final int shadowHeight) {
            rect.inset(0, -1 * shadowHeight);
        }

        @Override
        LinearGradient getShadowGradient(final int shadowHeight, final int width, final int height, final int[] gradientColors) {
            return new LinearGradient(0, 0, 0, -1 * shadowHeight, gradientColors[0], gradientColors[1], Shader.TileMode.CLAMP);
        }
    },
    LEFT(2) {
        @Override
        Rect getShadowRect(final int width, final int height, final int shadowHeight) {
            return new Rect(0 - shadowHeight, 0, 0, height);
        }

        @Override
        void inset(final Rect rect, final int shadowHeight) {
            rect.inset(-1 * shadowHeight, 0);
        }

        @Override
        LinearGradient getShadowGradient(final int shadowHeight, final int width, final int height, final int[] gradientColors) {
            return new LinearGradient(0, 0, -1 * shadowHeight, 0, gradientColors[0], gradientColors[1], Shader.TileMode.CLAMP);
        }
    },
    BOTTOM(4) {
        @Override
        Rect getShadowRect(final int width, final int height, final int shadowHeight) {
            return new Rect(0, height, width, height + shadowHeight);
        }

        @Override
        void inset(final Rect rect, final int shadowHeight) {
            rect.inset(0, -1 * shadowHeight);
        }

        @Override
        LinearGradient getShadowGradient(final int shadowHeight, final int width, final int height, final int[] gradientColors) {
            return new LinearGradient(0, height, 0, height + shadowHeight, gradientColors[0], gradientColors[1], Shader.TileMode.CLAMP);
        }
    },
    RIGHT(8) {
        @Override
        Rect getShadowRect(final int width, final int height, final int shadowHeight) {
            return new Rect(width, 0, width + shadowHeight, height);
        }

        @Override
        void inset(final Rect rect, final int shadowHeight) {
            rect.inset(-1 * shadowHeight, 0);
        }

        @Override
        LinearGradient getShadowGradient(final int shadowHeight, final int width, final int height, final int[] gradientColors) {
            return new LinearGradient(width, 0, width + shadowHeight, 0, gradientColors[0], gradientColors[1], Shader.TileMode.CLAMP);
        }
    };

    final int value;

    Side(final int value) {
        this.value = value;
    }

    abstract Rect getShadowRect(int width, int height, int shadowHeight);

    abstract void inset(Rect rect, int shadowHeight);

    abstract LinearGradient getShadowGradient(int shadowHeight, int width, int height, int[] gradientColors);
}
