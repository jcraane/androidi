package nl.capaxit.androidilib.ui.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamiecraane on 25/02/16.
 */
public class AnimationChain {
    private final List<AnimationSpec> spec = new ArrayList<AnimationSpec>();

    public AnimationChain add(final AnimationSpec spec) {
        this.spec.add(spec);
        return this;
    }

    public void start() {
        animate(0, spec.toArray(new AnimationSpec[spec.size()]));
    }

    private void animate(final int index, final AnimationSpec[] animationSpecs) {
        final AnimationListenerChain listenerChain = new AnimationListenerChain();
        if (index < animationSpecs.length) {
            final AnimationSpec spec = animationSpecs[index];
            final int nextIndex = index + 1;
            if (nextIndex < animationSpecs.length) {
                final AnimationSpec nextSpec = animationSpecs[nextIndex];
                if (nextSpec.startAfter == AnimationSpec.StartAfter.PREVIOUS_END) {
                    listenerChain.addListener(new EmptyAnimationListener() {
                        @Override
                        public void onAnimationEnd(final Animation animation) {
                            if (spec.animationListener != null) {
                                spec.animationListener.onAnimationEnd(animation);
                            }

                            animate(nextIndex, animationSpecs);
                        }
                    });
                } else {
                    spec.viewToAnimate.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animate(nextIndex, animationSpecs);
                        }
                    }, nextSpec.delay);
                }
            }

            listenerChain.addListener(spec.animationListener);
            spec.animation.setAnimationListener(listenerChain);
            spec.viewToAnimate.startAnimation(spec.animation);
            spec.viewToAnimate.setVisibility(View.VISIBLE);
        }
    }

    public static class AnimationListenerChain implements Animation.AnimationListener {
        private final List<Animation.AnimationListener> listeners = new ArrayList<Animation.AnimationListener>();

        public AnimationListenerChain(final Animation.AnimationListener... listener) {
            for (final Animation.AnimationListener animationListener : listener) {
                if (animationListener != null) {
                    listeners.add(animationListener);
                }
            }
        }

        public void addListener(final Animation.AnimationListener listener) {
            if (listener != null) {
                listeners.add(listener);
            }
        }

        @Override
        public void onAnimationStart(final Animation animation) {
            for (final Animation.AnimationListener listener : listeners) {
                listener.onAnimationStart(animation);
            }
        }

        @Override
        public void onAnimationEnd(final Animation animation) {
            for (final Animation.AnimationListener listener : listeners) {
                listener.onAnimationEnd(animation);
            }
        }

        @Override
        public void onAnimationRepeat(final Animation animation) {
            for (final Animation.AnimationListener listener : listeners) {
                listener.onAnimationRepeat(animation);
            }
        }
    }

    public static class AnimationSpec {
        private final View viewToAnimate;
        private final Animation animation;
        private final StartAfter startAfter;
        private final int delay;
        private final Animation.AnimationListener animationListener;

        private AnimationSpec(final Builder builder) {
            this.viewToAnimate = builder.viewToAnimate;
            this.animation = builder.animation;
            this.startAfter = builder.startAfter;
            this.delay = builder.delay;
            this.animationListener = builder.animationListener;
        }

        public enum StartAfter {
            PREVIOUS_END(), DELAY()
        }

        public static class Builder {
            private final View viewToAnimate;
            private final Animation animation;
            private StartAfter startAfter = StartAfter.PREVIOUS_END;
            private int delay;
            private int repeatCount;
            private int repeatMode;
            private Animation.AnimationListener animationListener;
            private boolean fillAfter;

            public Builder(final Context context, final View viewToAnimate, final int animationResource, final int duration) {
                this.viewToAnimate = viewToAnimate;
                this.animation = AnimationUtils.loadAnimation(context, animationResource);
                this.animation.setStartOffset(0); // Reset startoffset for animations loaded via xml.
//                this.animation.setRepeatCount(repeatCount);
//                this.animation.setRepeatMode(repeatMode);
                this.animation.setDuration(duration);
            }

            public Builder startAfter(final StartAfter startAfter, final int delay) {
                if (startAfter != null) {
                    this.startAfter = startAfter;
                    this.delay = delay;
                }

                return this;
            }

            public Builder repeatCount(final int repeatMode) {
                this.repeatCount = repeatMode;
                return this;
            }

            public Builder repeatMode(final int repeatMode) {
                this.repeatMode = repeatMode;
                return this;
            }

            public Builder interpolator(final Interpolator interpolator) {
                if (interpolator != null) {
                    this.animation.setInterpolator(interpolator);
                }
                return this;
            }

            public Builder listener(final Animation.AnimationListener listener) {
                this.animationListener = listener;
                return this;
            }

            public Builder multiplier(final double multiplier) {
                this.animation.setDuration((long) (this.animation.getDuration() * multiplier));
                return this;
            }

            public Builder fillAfter(final boolean fillAfter) {
                this.fillAfter = fillAfter;
                return this;
            }

            public AnimationSpec build() {
                this.animation.setFillAfter(this.fillAfter);
                return new AnimationSpec(this);
            }
        }
    }
}