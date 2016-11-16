package nl.capaxit.androidilib.ui.animation;

import android.view.animation.Interpolator;

/**
 * Interpolator which simulates a Spring.
 *
 * Created by jamiecraane on 04/08/16.
 */
public class SpringInterpolator implements Interpolator {
    @Override
    public float getInterpolation(final float input) {
        final double factor = 0.4;
        return (float) (Math.pow(2, -13 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
    }
}
