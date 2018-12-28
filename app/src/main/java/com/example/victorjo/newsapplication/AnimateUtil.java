package com.example.victorjo.newsapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.google.android.material.animation.AnimatorSetCompat;

import androidx.recyclerview.widget.RecyclerView;

public class AnimateUtil {

    public static void animate(RecyclerView.ViewHolder viewHolder, boolean goesdown){
        AnimatorSet animatorSetCompat = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewHolder.itemView, "translationY", goesdown ? 200 : -200, 0);
        objectAnimator.setDuration(1000);

        animatorSetCompat.playTogether(objectAnimator);
        animatorSetCompat.start();

    }
}
