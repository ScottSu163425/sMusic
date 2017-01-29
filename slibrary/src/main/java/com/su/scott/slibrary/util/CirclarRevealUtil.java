package com.su.scott.slibrary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Administrator on 2016/8/11.
 */
public class CirclarRevealUtil {
    private static final int START_RADIUS_DEFAULT = 33;
    private static final float END_RADIUS_RATIO_DEFAULT = 1.5f;


    public enum DIRECTION {
        LEFT_TOP,
        CENTER_TOP,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM,
        CENTER_BOTTOM,
        LEFT_BOTTOM,
        LEFT_CENTER,
        CENTER
    }

    public enum ACTION {
        REVEAL_IN,
        REVEAL_OUT,
    }

    public static final long DURATION_REVEAL_LONG = AnimUtil.DURATION_LONG;
    public static final long DURATION_REVEAL_NORMAL = AnimUtil.DURATION_NORMAL;
    public static final long DURATION_REVEAL_SHORT = AnimUtil.DURATION_SHORT;

    public static Animator createReveal(@NonNull View view, DIRECTION direction, ACTION action) {
        return createCirclarReveal(view, direction, action);
    }

    public static void revealIn(@NonNull View view, DIRECTION direction) {
        revealIn(view, direction, DURATION_REVEAL_SHORT, null, null);
    }

    public static void revealIn(@NonNull View view, DIRECTION direction, long duration) {
        revealIn(view, direction, duration, null, null);
    }

    public static void revealIn(@NonNull View view, DIRECTION direction, long duration, @Nullable TimeInterpolator interpolator) {
        revealIn(view, direction, duration, interpolator, null);
    }

    public static void revealOut(@NonNull View view, DIRECTION direction, boolean autoHide) {
        revealOut(view, direction, DURATION_REVEAL_SHORT, null, null, autoHide);
    }

    public static void revealOut(@NonNull View view, DIRECTION direction, long duration, boolean autoHide) {
        revealOut(view, direction, duration, null, null, autoHide);
    }

    public static void revealOut(@NonNull View view, DIRECTION direction, boolean autoHide, @Nullable final AnimUtil.SimpleAnimListener listener) {
        revealOut(view, direction, DURATION_REVEAL_SHORT, null, listener, autoHide);
    }

    public static void revealOut(@NonNull View view, DIRECTION direction, long duration, @Nullable TimeInterpolator interpolator, boolean autoHide) {
        revealOut(view, direction, duration, interpolator, null, autoHide);
    }

    public static void revealIn(@NonNull View view, DIRECTION direction, long duration, @Nullable TimeInterpolator interpolator, @Nullable final AnimUtil.SimpleAnimListener listener) {
        reveal(view, direction, ACTION.REVEAL_IN, duration, interpolator, listener, false);
    }

    public static void revealOut(@NonNull View view, DIRECTION direction, long duration, @Nullable TimeInterpolator interpolator, @Nullable final AnimUtil.SimpleAnimListener listener, boolean autoHide) {
        reveal(view, direction, ACTION.REVEAL_OUT, duration, interpolator, listener, autoHide);
    }

    private static void reveal(@NonNull final View view, DIRECTION direction, final ACTION action, long duration, @Nullable TimeInterpolator interpolator, @Nullable final AnimUtil.SimpleAnimListener listener, final boolean autoHide) {
        Animator animator = createCirclarReveal(view, direction, action);
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (action == ACTION.REVEAL_IN) {
                    view.setVisibility(View.VISIBLE);
                }

                if (listener != null) {
                    listener.onAnimStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (action == ACTION.REVEAL_OUT) {
                    if (autoHide) {
                        view.setVisibility(View.GONE);
                    }
                }

                if (listener != null) {
                    listener.onAnimEnd();
                }
            }
        });
        animator.setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator);
        animator.start();
    }

    private static Animator createCirclarReveal(View view, DIRECTION direction, ACTION action) {
        if (!SdkUtil.isLolipopOrLatter()) {
            return ObjectAnimator.ofFloat(view, "alpha", 1, 1);
        }
        int cx = 0;
        int cy = 0;
        float startRadius = 0;
        float endRadius = 0;
        float viewMaxRadius =  (float) (Math.hypot(view.getWidth(), view.getHeight()));

        if (action == ACTION.REVEAL_IN) {
            startRadius = START_RADIUS_DEFAULT;
            endRadius =  viewMaxRadius* END_RADIUS_RATIO_DEFAULT;
        } else if (action == ACTION.REVEAL_OUT) {
            startRadius = viewMaxRadius* END_RADIUS_RATIO_DEFAULT;
            endRadius = 0;
        }

        switch (direction) {
            case LEFT_TOP:
                cx = getLeft(view);
                cy = getTop(view);
                break;

            case CENTER_TOP:
                cx = getCenterX(view);
                cy = getTop(view);
                break;

            case RIGHT_TOP:
                cx = getRight(view);
                cy = getTop(view);
                break;

            case RIGHT_CENTER:
                cx = getRight(view);
                cy = getCenterY(view);
                break;

            case RIGHT_BOTTOM:
                cx = getRight(view);
                cy = getBottom(view);
                break;

            case CENTER_BOTTOM:
                cx = getCenterX(view);
                cy = getBottom(view);
                break;

            case LEFT_BOTTOM:
                cx = getLeft(view);
                cy = getBottom(view);
                break;

            case LEFT_CENTER:
                cx = getLeft(view);
                cy = getCenterY(view);
                break;

            case CENTER:
                cx = getCenterX(view);
                cy = getCenterY(view);
                break;
        }
        return ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
    }

    private static int getCenterX(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return (rect.centerX());
    }

    private static int getCenterY(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return (rect.centerY());
    }

    private static int getLeft(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect.left;
    }

    private static int getRight(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect.right;
    }

    private static int getTop(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect.top;
    }

    private static int getBottom(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        return rect.bottom;
    }


}
