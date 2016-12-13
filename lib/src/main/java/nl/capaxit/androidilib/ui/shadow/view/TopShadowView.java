package nl.capaxit.androidilib.ui.shadow.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import nl.capaxit.androidilib.R;
import nl.capaxit.androidilib.compat.ViewCompatExtended;

/**
 * Simple view which only displays a simple top-shadow.
 *
 * Created by jamiecraane on 11/11/2016.
 */
public class TopShadowView extends View {
    public TopShadowView(final Context context) {
        this(context, null);
    }

    public TopShadowView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        ViewCompatExtended.setBackground(this, ContextCompat.getDrawable(context, R.drawable.shadow_top));
    }
}