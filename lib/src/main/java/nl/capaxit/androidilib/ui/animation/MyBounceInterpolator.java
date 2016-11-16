/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.capaxit.androidilib.ui.animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * An interpolator with simulates a bounce.
 */
public class MyBounceInterpolator implements Interpolator {
    private final float[] values = new float[]{2.0F, 2.8F, 1.7F, 2.3F, 1.9F, 2.04F, 2.0F};

    @SuppressWarnings({"UnusedDeclaration"})
    public MyBounceInterpolator(final Context context, final AttributeSet attrs) {
    }

    private static float bounce(final float t) {
        return t * t * 8.0f;
    }

    @Override
    public float getInterpolation(final float t) {
        if (t < 0.14) {
            return t * (values[1] - values[0]);
        } else if (t < 0.28) {
            return t * (values[1] - values[2]);
        } else if (t < 0.36) {
            return t * (values[2] - values[3]);
        } else if (t < 0.52) {
            return t * (values[2] - values[3]);
        } else if (t < 0.68) {
            return t * (values[4] - values[5]);
        } else if (t < 0.84) {
            return t * (values[5] - values[6]);
        } else {
            return t * values[6] - values[5];
        }
    }
}