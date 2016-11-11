package nl.capaxit.androidilib.ui.shadow;

import android.graphics.Rect;

/**
 * Created by jamiecraane on 11/11/2016.
 */
enum Side {
    TOP(1) {
        @Override
        Rect getShadowRect(final int width, final int height, final int shadowHeight) {
            return new Rect(0, -1 * shadowHeight, width, 0);
        }

        @Override
        void inset(final Rect rect, final int shadowHeight) {
            rect.inset(0, -1 * shadowHeight);
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
    };

    final int value;

    Side(final int value) {
        this.value = value;
    }

    abstract Rect getShadowRect(int width, int height, int shadowHeight);

    abstract void inset(Rect rect, int shadowHeight);
}
