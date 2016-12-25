package com.su.scott.slibrary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.su.scott.slibrary.R;

/**
 * Created by Administrator on 2016/8/12.
 */
public class AnimUtil {

    public static final long DURATION_SHORT = 400;
    public static final long DURATION_NORMAL = 600;
    public static final long DURATION_LONG = 800;
    public static final long DURATION_XLONG = 1200;
    public static final long DURATION_SHORT_HALF = DURATION_SHORT / 2;
    public static final long DURATION_NORMAL_HALF = DURATION_NORMAL / 2;
    public static final long DURATION_LONG_HALF = DURATION_LONG / 2;

    public static final long ROTATION_DEGREE_ROUND_ZERO = 0;
    public static final long ROTATION_DEGREE_ROUND = 360;
    public static final long ROTATION_DEGREE_ROUND_HALF = ROTATION_DEGREE_ROUND / 2;
    public static final long ROTATION_DEGREE_ROUND_QUARTER = ROTATION_DEGREE_ROUND / 4;


    public interface SimpleAnimListener {
        void onAnimStart();

        void onAnimEnd();
    }

    public enum ACTION {
        IN,
        OUT,
    }

    public static void scaleIn(@NonNull View view) {
        scale(view, ACTION.IN, 1, 1, DURATION_NORMAL, null, null);
    }

    public static void scaleIn(@NonNull View view, long duration) {
        scale(view, ACTION.IN, 1, 1, duration, null, null);
    }

    public static void scaleIn(@NonNull View view, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        scale(view, ACTION.IN, 1, 1, duration, interpolator, listener);
    }

    public static void scaleIn(@NonNull View view, long duration, @Nullable TimeInterpolator interpolator) {
        scale(view, ACTION.IN, 1, 1, duration, interpolator, null);
    }

    public static void scaleOut(@NonNull View view) {
        scale(view, ACTION.OUT, 0, 0, DURATION_NORMAL, null, null);
    }

    public static void scaleOut(@NonNull View view, long duration) {
        scale(view, ACTION.OUT, 0, 0, duration, null, null);
    }

    public static void scaleOut(@NonNull View view, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        scale(view, ACTION.OUT, 0, 0, duration, interpolator, listener);
    }

    public static void alphaIn(@NonNull View view) {
        alpha(view, ACTION.IN, 0, 1.0f, DURATION_NORMAL, null, null);
    }

    public static void alphaIn(@NonNull View view, long duration) {
        alpha(view, ACTION.IN, 0, 1.0f, duration, null, null);
    }

    public static void alphaOut(@NonNull View view) {
        alpha(view, ACTION.OUT, 1.0f, 0, DURATION_NORMAL, null, null);
    }

    public static void alphaOut(@NonNull View view, long duration) {
        alpha(view, ACTION.OUT, 1.0f, 0, duration, null, null);
    }

    public static void scale(@NonNull final View view, final ACTION action, float toX, float toY, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        view.animate()
                .scaleX(toX)
                .scaleY(toY)
                .setDuration(duration)
                .setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        if (action == ACTION.IN) {
                            view.setVisibility(View.VISIBLE);
                        }
                        if (listener != null) {
                            listener.onAnimStart();
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (action == ACTION.OUT) {
                            view.setVisibility(View.GONE);
                        }
                        if (listener != null) {
                            listener.onAnimEnd();
                        }
                    }
                })
                .start();
    }

    public static void alpha(@NonNull final View view, final ACTION action, float fromAlpha, float toAlpha, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ViewUtil.setViewVisiable(view);
                if (listener != null) {
                    listener.onAnimStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (action == ACTION.OUT) {
                    ViewUtil.setViewGone(view);
                }

                if (listener != null) {
                    listener.onAnimEnd();
                }

            }
        });
        animator.start();
    }

    public static void rotate2DPositive(@NonNull final View view, float to, long duration) {
        rotate2D(view, 0, Math.abs(to), duration, null, null);
    }

    public static void rotate2DNegative(@NonNull final View view, float to, long duration) {
        rotate2D(view, 0, -Math.abs(to), duration, null, null);
    }

    public static void rotateX3DPositive(@NonNull final View view, float to, long duration) {
        rotate3D(view, true, 0, Math.abs(to), duration, null, null);
    }

    public static void rotateX3DNegative(@NonNull final View view, float to, long duration) {
        rotate3D(view, true, 0, -Math.abs(to), duration, null, null);
    }

    public static void rotateX3DPositive(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator) {
        rotate3D(view, true, 0, Math.abs(to), duration, interpolator, null);
    }

    public static void rotateX3DNegative(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator) {
        rotate3D(view, true, 0, -Math.abs(to), duration, interpolator, null);
    }

    public static void rotateX3DPositive(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, true, 0, Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateX3DNegative(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, true, 0, -Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateX3DPositive(@NonNull final View view, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, true, from, Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateX3DNegative(@NonNull final View view, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, true, from, -Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateY3DPositive(@NonNull final View view, float to, long duration) {
        rotate3D(view, false, 0, Math.abs(to), duration, null, null);
    }

    public static void rotateY3DNegative(@NonNull final View view, float to, long duration) {
        rotate3D(view, false, 0, -Math.abs(to), duration, null, null);
    }

    public static void rotateY3DPositive(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator) {
        rotate3D(view, false, 0, Math.abs(to), duration, interpolator, null);
    }

    public static void rotateY3DNegative(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator) {
        rotate3D(view, false, 0, -Math.abs(to), duration, interpolator, null);
    }

    public static void rotateY3DPositive(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, false, 0, Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateY3DNegative(@NonNull final View view, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, false, 0, -Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateY3DPositive(@NonNull final View view, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, false, from, Math.abs(to), duration, interpolator, listener);
    }

    public static void rotateY3DNegative(@NonNull final View view, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable SimpleAnimListener listener) {
        rotate3D(view, false, from, -Math.abs(to), duration, interpolator, listener);
    }


    public static void rotate2D(@NonNull final View view, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", from, to);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ViewUtil.setViewVisiable(view);
                if (listener != null) {
                    listener.onAnimStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onAnimEnd();
                }
            }
        });
        animator.start();
    }

    private static void rotate3D(@NonNull final View view, boolean isRotateX, float from, float to, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, (isRotateX ? "rotationX" : "rotationY"), from, to);
        animator.setDuration(duration);
        animator.setInterpolator(interpolator == null ? new AccelerateDecelerateInterpolator() : interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                ViewUtil.setViewVisiable(view);
                if (listener != null) {
                    listener.onAnimStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onAnimEnd();
                }
            }
        });
        animator.start();
    }

    /**
     * 左右晃动
     *
     * @return
     */
    public static void shake(View target) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0);
        oa.setDuration(DURATION_NORMAL);
        oa.start();
    }

    /**
     * 橡皮筋
     *
     * @param target
     */
    public static void rubberBand(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 1, 1.25f, 0.75f, 1.15f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.75f, 1.25f, 0.85f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(DURATION_NORMAL);
        set.start();
    }

    /**
     * 嗒哒！
     *
     * @param target
     */
    public static void tada(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleX", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleY", 1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        ObjectAnimator oa3 = ObjectAnimator.ofFloat(target, "rotation", 0, -3, -3, 3, -3, 3, -3, 3, -3, 0);
        set.playTogether(oa1, oa2, oa3);
        set.setDuration(1000);
        set.start();
    }

    /**
     * 心跳
     *
     * @param target
     */
    public static void pulseBounce(View target) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(target, "scaleY", 1, 1.05f, 1);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(target, "scaleX", 1, 1.05f, 1);
        set.playTogether(oa1, oa2);
        set.setDuration(1000);
        set.setInterpolator(new BounceInterpolator());
        set.start();
    }

    /**
     * 将View的背景颜色更改，使背景颜色转换更和谐的过渡动画
     *
     * @param view       要改变背景颜色的View
     * @param startColor 上个颜色值
     * @param endColor   当前颜色值
     * @param duration   动画持续时间
     */
    public static void transformColor(View view, int startColor, int endColor, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofInt(view, "backgroundColor", new int[]{startColor, endColor});
        animator.setDuration(duration);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }



}
