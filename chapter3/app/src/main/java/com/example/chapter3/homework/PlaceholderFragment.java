package com.example.chapter3.homework;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlaceholderFragment extends Fragment {

    private View anim;
    private View text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件

        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        anim = view.findViewById(R.id.animation_view);
        text = view.findViewById(R.id.textt);
        text.setAlpha(0);
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入

                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(anim, "alpha", 1.0f, 0.0f, 0.0f);
                alphaAnimator.setDuration(2000);
                alphaAnimator.start();

                ObjectAnimator alphaAnimator2 = ObjectAnimator.ofFloat(text, "alpha", 0.0f, 1.0f, 1.0f);
                alphaAnimator2.setDuration(2000);
                alphaAnimator2.start();
            }
        }, 5000);
    }
}
