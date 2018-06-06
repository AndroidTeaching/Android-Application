package com.android.teaching.miprimeraapp.reactivex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.teaching.miprimeraapp.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ReactiveXActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reactive_x2);

        Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                int count;
                for(count = 0; count <= 10; count++) {
                    emitter.onNext(count);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        });

        Observer subscriber = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object value) {
                Log.d("RxAndroid","Subscriber:onNext(" +value.toString() + ")");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d("RxAndroid","Subscriber:onComplete()");
            }
        };

        integerObservable.subscribe(subscriber);
    }
}














