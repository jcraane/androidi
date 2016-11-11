package nl.capaxit.androidi;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;

import nl.capaxit.androidilib.ui.shadow.ShadowWrapperFrameLayout;

public class DemoActivity extends BaseAppCompatActivity {
    private ShadowWrapperFrameLayout shadowWrapperFrameLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startShadowAnimation(view);
            }
        });
        shadowWrapperFrameLayout = (ShadowWrapperFrameLayout) findViewById(R.id.shadowFrameLayout);
    }

    private void startShadowAnimation(final View view) {
        final ValueAnimator animator = ObjectAnimator.ofInt(1, 100);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                shadowWrapperFrameLayout.setShadowHeight((Integer) valueAnimator.getAnimatedValue());
            }
        });
        view.post(new Runnable() {
            @Override
            public void run() {
                animator.start();
            }
        });
    }
}
