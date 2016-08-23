package com.su.scott.slibrary.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Administrator on 2016/8/12.
 */
public class AnimUtil {

    public static final long DEFAULT_DURATION = 400;

    public interface SimpleAnimListener {
        void onAnimStart();

        void onAnimEnd();
    }

    public enum ACTION {
        IN,
        OUT,
    }

    public static void scaleIn(@NonNull View view) {
        scale(view, ACTION.IN, 1, 1, DEFAULT_DURATION,null, null);
    }

    public static void scaleIn(@NonNull View view, long duration) {
        scale(view, ACTION.IN, 1, 1, duration, null, null);
    }


    public static void scaleIn(@NonNull View view, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        scale(view, ACTION.IN, 1, 1, duration, interpolator, listener);
    }

    public static void scaleOut(@NonNull View view) {
        scale(view, ACTION.OUT, 0, 0, DEFAULT_DURATION, null, null);
    }

    public static void scaleOut(@NonNull View view, long duration) {
        scale(view, ACTION.OUT, 0, 0, duration, null, null);
    }

    public static void scaleOut(@NonNull View view, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        scale(view, ACTION.OUT, 0, 0, duration, interpolator, listener);
    }

    public static void alphaIn(@NonNull View view) {
        alpha(view, ACTION.IN, 1, DEFAULT_DURATION, null, null);
    }

    public static void alphaIn(@NonNull View view, long duration) {
        alpha(view, ACTION.IN, 1, duration, null, null);
    }

    public static void alphaOut(@NonNull View view) {
        alpha(view, ACTION.OUT, 0, DEFAULT_DURATION, null, null);
    }

    public static void alphaOut(@NonNull View view, long duration) {
        alpha(view, ACTION.OUT, 0, duration, null, null);
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

    public static void alpha(@NonNull final View view, final ACTION action, float toAlpha, long duration, @Nullable TimeInterpolator interpolator, @Nullable final SimpleAnimListener listener) {
        view.animate()
                .alpha(toAlpha)
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

    public static void rotate2DPositive(@NonNull final View view, float to, long duration) {
        rotate2D(view, 0, Math.abs(to), duration, null, null);
    }

    public static void rotate2DNegative(@NonNull final View view, float to, long duration) {
        rotate2D(view, 0, -Math.abs(to), duration, null, null);
    }

    public static void rotateX3DPositive(@NonNull final View view, float to, long duration) {
        rotate3D(view,true, 0, Math.abs(to), duration, null, null);
    }

    public static void rotateX3DNegative(@NonNull final View view, float to, long duration) {
        rotate3D(view,true, 0, -Math.abs(to), duration, null, null);
    }

    public static void rotateY3DPositive(@NonNull final View view, float to, long duration) {
        rotate3D(view,false, 0, Math.abs(to), duration, null, null);
    }

    public static void rotateY3DNegative(@NonNull final View view, float to, long duration) {
        rotate3D(view,false, 0, -Math.abs(to), duration, null, null);
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
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,(isRotateX ? "rotationX" : "rotationY"), from, to);
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


}
