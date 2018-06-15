package com.oyp.battery;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
/**
 *  自定义View 展示界面
 * </p>
 * created by OuyangPeng at 2018/6/15 下午 03:33
 * @author OuyangPeng
 */
public class MainActivity extends AppCompatActivity implements BaseHandlerCallBack {

    private PowerConsumptionRankingsBatteryView mPowerConsumptionRankingsBatteryView;
    private int power;

    private NoLeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new NoLeakHandler(this);
        mPowerConsumptionRankingsBatteryView = (PowerConsumptionRankingsBatteryView) findViewById(R.id.mPowerConsumptionRankingsBatteryView);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void callBack(Message msg) {
        switch (msg.what) {
            case 0:
                mPowerConsumptionRankingsBatteryView.setLevelHeight(power += 5);
                if (power == 100) {
                    power = 0;
                }
                if (power < 30) {
                    mPowerConsumptionRankingsBatteryView.setLowerPower();
                } else if (power < 60) {
                    mPowerConsumptionRankingsBatteryView.setOffline();
                } else {
                    mPowerConsumptionRankingsBatteryView.setOnline();
                }
                break;
            default:
                break;
        }
    }

    private static class NoLeakHandler<T extends BaseHandlerCallBack> extends Handler {
        private WeakReference<T> wr;

        public NoLeakHandler(T t) {
            wr = new WeakReference<>(t);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T t = wr.get();
            if (t != null) {
                t.callBack(msg);
            }
        }
    }
}
