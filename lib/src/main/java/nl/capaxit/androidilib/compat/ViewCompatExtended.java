package nl.capaxit.androidilib.compat;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by jamiecraane on 21/01/15.
 */
public class ViewCompatExtended {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(final View view, final Drawable background) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(background);
        } else {
            view.setBackground(background);
        }
    }
}
