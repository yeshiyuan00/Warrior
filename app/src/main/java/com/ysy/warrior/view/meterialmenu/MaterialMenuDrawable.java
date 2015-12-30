package com.ysy.warrior.view.meterialmenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.util.Property;

/**
 * User: ysy
 * Date: 2015/12/30
 * Time: 16:56
 */
public class MaterialMenuDrawable extends Drawable implements Animatable {
    public enum IconState {
        BURGER, ARROW, X, CHECK
    }

    public enum AnimationState {
        BURGER_ARROW, BURGER_X, ARROW_X, ARROW_CHECK, BURGER_CHECK, X_CHECK;

        public IconState getFirstState() {
            switch (this) {
                case BURGER_ARROW:
                    return IconState.BURGER;
                case BURGER_X:
                    return IconState.BURGER;
                case ARROW_X:
                    return IconState.ARROW;
                case ARROW_CHECK:
                    return IconState.ARROW;
                case BURGER_CHECK:
                    return IconState.BURGER;
                case X_CHECK:
                    return IconState.X;
                default:
                    return null;
            }
        }

        public IconState getSecondState() {
            switch (this) {
                case BURGER_ARROW:
                    return IconState.ARROW;
                case BURGER_X:
                    return IconState.X;
                case ARROW_X:
                    return IconState.X;
                case ARROW_CHECK:
                    return IconState.CHECK;
                case BURGER_CHECK:
                    return IconState.CHECK;
                case X_CHECK:
                    return IconState.CHECK;
                default:
                    return null;
            }
        }
    }


    public enum Stroke {
        /**
         * 3 dip
         */
        REGULAR(3),
        /**
         * 2 dip
         */
        THIN(2),
        /**
         * 1 dip
         */
        EXTRA_THIN(1);

        private final int strokeWidth;

        Stroke(int strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        protected static Stroke valueOf(int strokeWidth) {
            switch (strokeWidth) {
                case 3:
                    return REGULAR;
                case 2:
                    return THIN;
                case 1:
                    return EXTRA_THIN;
                default:
                    return THIN;
            }
        }
    }

    public static final int DEFAULT_COLOR = Color.WHITE;
    public static final int DEFAULT_SCALE = 1;
    public static final int DEFAULT_TRANSFORM_DURATION = 800;
    public static final int DEFAULT_PRESSED_DURATION = 400;

    private static final int BASE_DRAWABLE_WIDTH = 40;
    private static final int BASE_DRAWABLE_HEIGHT = 40;
    private static final int BASE_ICON_WIDTH = 20;
    private static final int BASE_CIRCLE_RADIUS = 18;

    private static final float ARROW_MID_LINE_ANGLE = 180;
    private static final float ARROW_TOP_LINE_ANGLE = 135;
    private static final float ARROW_BOT_LINE_ANGLE = 225;
    private static final float X_TOP_LINE_ANGLE = 44;
    private static final float X_BOT_LINE_ANGLE = -44;
    private static final float X_ROTATION_ANGLE = 90;
    private static final float CHECK_MIDDLE_ANGLE = 135;
    private static final float CHECK_BOTTOM_ANGLE = -90;

    private static final float TRANSFORMATION_START = 0;
    private static final float TRANSFORMATION_MID = 1.0f;
    private static final float TRANSFORMATION_END = 2.0f;

    private static final int DEFAULT_CIRCLE_ALPHA = 200;

    private final float diph;
    private final float dip1;
    private final float dip2;
    private final float dip3;
    private final float dip4;
    private final float dip6;
    private final float dip8;

    private final int width;
    private final int height;
    private final float strokeWidth;
    private final float iconWidth;
    private final float topPadding;
    private final float sidePadding;
    private final float circleRadius;

    private final Stroke stroke;

    private final Object lock = new Object();

    private final Paint iconPaint = new Paint();
    private final Paint circlePaint = new Paint();

    private float transformationValue = 0f;
    private float pressedProgressValue = 0f;
    private boolean transformationRunning = false;

    private IconState currentIconState = IconState.BURGER;
    private AnimationState animationState = AnimationState.BURGER_ARROW;

    private IconState animatingIconState;
    private boolean drawTouchCircle;
    private boolean neverDrawTouch;
    private boolean rtlEnabled;

    private ObjectAnimator transformation;
    private ObjectAnimator pressedCircle;

    //TODO
    //private MaterialMenuState materialMenuState;


}
