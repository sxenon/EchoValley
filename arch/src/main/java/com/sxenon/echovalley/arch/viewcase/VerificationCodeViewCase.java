package com.sxenon.echovalley.arch.viewcase;

import android.view.View;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VerificationCodeViewCase implements IViewCase {
    private Disposable mCountDownDisposable;

    public VerificationCodeViewCase(final View btnSendCode, final CountDownListener listener, final int seconds) {

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSendCode.setClickable(false);
                mCountDownDisposable = Flowable.intervalRange(0, seconds + 1, 0, 1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) {
                                listener.onStart();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<Long>() {
                            @Override
                            public void accept(Long elapsed) {
                                listener.onTick(seconds - elapsed);
                            }
                        })
                        .doOnComplete(new Action() {
                            @Override
                            public void run() {
                                listener.onFinish();
                                btnSendCode.setClickable(true);
                            }
                        }).subscribe();
            }
        });
    }

    public void onDestroy() {
        mCountDownDisposable.dispose();
    }

    public interface CountDownListener {
        void onStart();

        void onTick(long secondsUntilFinished);

        void onFinish();
    }
}
