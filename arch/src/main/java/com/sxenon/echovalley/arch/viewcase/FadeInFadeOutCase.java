package com.sxenon.echovalley.arch.viewcase;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

public class FadeInFadeOutCase implements IViewCase {
    private View view;
    private float inAlpha = 1.0f;
    private float outAlpha = 0.0f;
    private long duration = 1000;
    private int translationX = 0;
    private int translationY = 0;

    public FadeInFadeOutCase(View view) {
        this.view = view;
    }

    public void setInAlpha(float inAlpha) {
        this.inAlpha = inAlpha;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setOutAlpha(float outAlpha) {
        this.outAlpha = outAlpha;
    }

    public void setTranslationX(int translationX) {
        this.translationX = translationX;
    }

    public void setTranslationY(int translationY) {
        this.translationY = translationY;
    }

    public void fadeIn(){
        if ( view.getVisibility() == View.VISIBLE ) {
            return;
        }

        PropertyValuesHolder translationX1 = PropertyValuesHolder.ofFloat("translationX", translationX);
        PropertyValuesHolder translationY1 = PropertyValuesHolder.ofFloat("translationY", translationY);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", outAlpha, inAlpha);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, translationX1, translationY1);
        view.setVisibility(View.VISIBLE);
        objectAnimator.setDuration(duration).start();
    }

    public void fadeOut(){
        PropertyValuesHolder translationX1 = PropertyValuesHolder.ofFloat("translationX", translationX);
        PropertyValuesHolder translationY1 = PropertyValuesHolder.ofFloat("translationY", -translationY);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", inAlpha, outAlpha);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, translationX1, translationY1);
        objectAnimator.setDuration(duration).start();

        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
